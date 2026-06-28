# Wavy Cards — Azure DevOps Deployment Plan

## Context

Wavy Cards is a multiplayer blackjack game with a Java WebSocket backend (uber-JAR) and a React/TypeScript SPA frontend (Vite), plus a Capacitor Android wrapper. Currently there is no containerisation, no production config, and the only CI is a dependency-review workflow. The goal is to add a full DevOps showcase: Terraform for Azure infrastructure, Docker for the backend, and three GitHub Actions pipelines (CI, Release, Deploy).

---

## Target Architecture

```
GitHub Actions
  ├── ci.yml            → test + lint + build on every push/PR
  ├── release.yml       → Docker image + APK + GitHub Release on tag v*
  └── deploy.yml        → terraform apply → ACA (staging then prod, with approval gate)

Azure (per environment: staging / production)
  ├── Resource Group
  ├── Azure Container Registry (ACR)         ← shared across envs
  ├── Container App Environment
  ├── Container App (backend WS server)      ← port 2346, WebSocket ingress
  └── Static Web App (React SPA)             ← CDN-backed, free tier

Azure Blob Storage (shared) ← Terraform remote state
```

---

## Phase 1 — Backend: make ports env-var configurable

**File:** `src/main/java/blackjack/MultiserverManager.java`

The server currently prompts interactively for a port. Add a non-interactive server-mode path:

```java
// If SERVER_PORT env var is set, skip interactive setup and start server directly
String envPort = System.getenv("SERVER_PORT");
if (envPort != null) {
    PORT = Integer.parseInt(envPort);
    // start server + websocket server directly, no stdin
}
```

This is the minimal change needed; leave the interactive path intact for local dev.

---

## Phase 2 — Containerise the backend

### New files:

**`Dockerfile`** (project root)
```dockerfile
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/wavy-cards-1.0-SNAPSHOT.jar app.jar
ENV SERVER_PORT=2345
EXPOSE 2345 2346
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**`.dockerignore`** (project root)
```
target/
frontend/
.git/
*.md
```

---

## Phase 3 — Frontend: inject backend URL at build time

**File:** `frontend/src/components/ConnectScreen.tsx`

Replace the hardcoded `10.0.2.2` default with a Vite env var:

```tsx
const defaultHost = import.meta.env.VITE_WS_HOST ?? '10.0.2.2';
const defaultPort = import.meta.env.VITE_WS_PORT ?? '2346';
```

**`frontend/.env.production`** (gitignored template; real values injected in CI):
```
VITE_WS_HOST=<container-app-fqdn>
VITE_WS_PORT=443
```

---

## Phase 4 — Terraform

### Directory structure:
```
terraform/
├── providers.tf          # azurerm + terraform version pinning
├── backend.tf            # Azure Blob Storage state backend
├── variables.tf          # env, location, acr_name, image_tag, etc.
├── main.tf               # wires modules together
├── outputs.tf            # container app FQDN, static web app URL
├── modules/
│   ├── container-app/
│   │   ├── main.tf       # ACR, Container App Env, Container App
│   │   ├── variables.tf
│   │   └── outputs.tf    # fqdn
│   └── static-web-app/
│       ├── main.tf       # azurerm_static_web_app
│       ├── variables.tf
│       └── outputs.tf    # default_host_name, api_key
└── environments/
    ├── staging.tfvars
    └── production.tfvars
```

### Key resources per module:

**container-app module:**
- `azurerm_resource_group`
- `azurerm_container_registry` (shared, Basic SKU)
- `azurerm_container_app_environment`
- `azurerm_container_app` — ingress on port 2346, `transport = "http"` (enables WebSocket passthrough), env var `SERVER_PORT`

**static-web-app module:**
- `azurerm_static_web_app` (Free SKU) — React SPA deployed via GitHub Actions token

**backend.tf:**
```hcl
terraform {
  backend "azurerm" {
    resource_group_name  = "tfstate-rg"
    storage_account_name = "wavycardstfstate"
    container_name       = "tfstate"
    key                  = "${var.environment}.terraform.tfstate"
  }
}
```

### tfvars difference between envs:

| Variable | staging | production |
|----------|---------|------------|
| `environment` | `staging` | `production` |
| `min_replicas` | `0` (scale to zero) | `1` |
| `cpu` | `0.25` | `0.5` |
| `memory` | `0.5Gi` | `1Gi` |

---

## Phase 5 — GitHub Actions Pipelines

### `.github/workflows/ci.yml`
- **Trigger:** push + PR to `main` and `feat/**`
- **Jobs:**
  1. `test` — `mvn test` (Java 21, Maven cache)
  2. `build-frontend` — `npm ci && npm run build` (Node 20, npm cache)
  3. `lint` — `npm run lint`
- All must pass; blocks merge.

### `.github/workflows/release.yml`
- **Trigger:** push of tag matching `v*.*.*`
- **Jobs (sequential):**
  1. `build-jar` — `mvn clean package -DskipTests` → upload JAR artifact
  2. `build-docker` — build & push image to ACR (`<acr>.azurecr.io/wavy-cards:<tag>`)
  3. `build-apk` — `npm run build` → `npx cap update android` → `./gradlew assembleRelease` → sign with keystore secret → upload APK artifact
  4. `build-frontend` — `npm run build` (with `VITE_WS_HOST` injected from secret) → upload dist artifact
  5. `github-release` — create GitHub Release, attach JAR + APK as assets

### `.github/workflows/deploy.yml`
- **Trigger:** manual `workflow_dispatch` with `environment` input (`staging` | `production`), OR automatically after `release.yml` completes on `main`
- **Jobs:**
  1. `terraform-plan` — `terraform init` + `terraform plan -var-file=environments/${{ env.ENV }}.tfvars -out=plan.tfplan`
  2. `deploy-infra` — `terraform apply plan.tfplan` (production requires GitHub Environment protection rule with manual reviewer)
  3. `deploy-frontend` — deploy frontend dist to Azure Static Web App using `azure/static-web-apps-deploy` action with the SWA deployment token (from Terraform output)
  4. `smoke-test` — `curl` the Container App health endpoint + WebSocket handshake check

### Required GitHub Secrets:

| Secret | Used by |
|--------|---------|
| `AZURE_CREDENTIALS` | Terraform (service principal JSON) |
| `ACR_USERNAME` / `ACR_PASSWORD` | Docker push in release |
| `ANDROID_KEYSTORE_BASE64` | APK signing |
| `ANDROID_KEY_ALIAS` | APK signing |
| `ANDROID_KEY_PASSWORD` | APK signing |
| `ANDROID_STORE_PASSWORD` | APK signing |
| `VITE_WS_HOST_STAGING` | Frontend build (staging) |
| `VITE_WS_HOST_PROD` | Frontend build (production) |

---

## Execution Order

1. `MultiserverManager.java` — env var support
2. `Dockerfile` + `.dockerignore`
3. `ConnectScreen.tsx` — VITE env var for WS host
4. `terraform/` — all Terraform files
5. `.github/workflows/ci.yml`
6. `.github/workflows/release.yml`
7. `.github/workflows/deploy.yml`
8. `mvn test` + `npm run build` — verify nothing is broken

---

## Verification

- `mvn test` — 39 tests pass
- `cd frontend && npm run build` — zero errors
- `docker build -t wavy-cards:local .` — image builds
- `docker run -e SERVER_PORT=2345 -p 2345:2345 -p 2346:2346 wavy-cards:local` — server starts without interactive prompt
- `terraform validate && terraform plan` — no errors (requires Azure credentials)
- GitHub Actions CI passes on a test PR
