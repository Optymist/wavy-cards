# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build (produces shaded fat JAR)
mvn package

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=HandTest

# Run a single test method
mvn test -Dtest=HandTest#testAddCard

# Run the application (after building)
java -jar target/wavy-cards-1.0-SNAPSHOT.jar

# Run without building a JAR
mvn exec:java -Dexec.mainClass=blackjack.MultiserverManager
```

The application is interactive: it prompts to either host (option 1) or join (option 2) a game. Hosting requires a player count (1–8) and a port; joining requires the host's IP and port.

## Architecture

### Client-Server over TCP Sockets

The game is split into a server process and one or more client processes that communicate over raw TCP sockets. All messages are newline-delimited JSON strings. Both ends use Jackson to produce/consume JSON.

**Server side** (`blackjack` package):
- `MultiserverManager` — entry point; forks into `Server.main()` or `Client.main()`.
- `Server` — opens a `ServerSocket`, spawns one `PlayerManager` thread per accepted connection, and runs a `serverConsole` thread for admin commands.
- `PlayerManager` — one instance per connected client. Reads raw lines from the socket and routes them to `player.setTurnResponse()` or `player.setBetResponse()` depending on which phase the player is in. Starts the `Play` game thread when enough players are connected.
- `Play` — implements `Runnable`; orchestrates rounds: collect bets → deal → each player's turn → dealer's turn → payouts → reset → repeat.

**Client side** (`blackjack.Client` package):
- `Client` — connects to the server socket. Spawns a read thread (`handleResponse`) that receives and prints server messages, while the main thread reads stdin and sends responses. Message routing is driven by `serverRequestType`, which is updated on each incoming message.
- `GuiClient` — stub; not yet implemented or wired into the menu.

### JSON Protocol

`protocolType` field determines the message kind:

| protocolType | Direction | Purpose |
|---|---|---|
| `general` | S→C | Plain text notification |
| `connectedUpdate` | S→C | Sent after name is accepted; includes starting money |
| `turnRequest` | S→C | Sent to the active player; lists available actions |
| `update` | S→C | Full game state snapshot (all players + dealer) |
| `betRequest` | S→C | Prompts the player for a bet amount |
| `turnResponse` | C→S | Player's chosen action (`{"action": "hit"}`) |
| `betResponse` | C→S | Player's bet (`{"bet": 100}`) |

Example messages are in `example/protocol/`.

**Server protocol classes:** `blackjack.protocol.GenerateJson` (produce) and `DecryptJson` (consume).  
**Client protocol classes:** `blackjack.Client.protocol.Generate` (produce) and `Decrypt` (consume).

### Game State Machine

`Hand` holds a `handState` (interface in `blackjack.player.state`). The state controls which actions are available and transitions happen automatically when cards are added:

- `Normal` → the only state where player actions are accepted. `getActions(Hand)` returns Hit, Stand, Double, Surrender, and optionally Split (only when the two starting cards have equal value and the hand hasn't already been split).
- `Bust` → hand value exceeded 21; turn ends immediately.
- `BlackJack` → exactly 21 on the first two cards; automatic payout at 1.5×.
- `Stand` → player chose to stand; turn ends.
- `Surrender` → player surrendered; half-bet returned.

### Actions (Strategy Pattern)

`BlackJackAction` is an abstract class with `execute(Hand, Player, Play)`. Concrete actions are in `blackjack.actions`. `BlackJackAction.create(String)` is the factory used by `DecryptJson` to instantiate the player's chosen action.

### Turn Polling (Known Design Issue)

`Player.manageTurn()` and `manageBet()` use a busy-wait `Thread.sleep(1000)` loop, spinning until `turnResponse` / `betResponse` is set by `PlayerManager`'s read thread. This is the current synchronization mechanism — no `wait()`/`notify()` or `BlockingQueue` is used.

### Known Static Field Bugs

- `Deck.PLAY_DECK` is `static` — shared across all `Deck` instances.
- `Play.players` is `static` — shared across all `Play` instances (rounds).

Both are latent correctness issues when multiple games/rounds are involved.

## Package Layout

```
blackjack/
  MultiserverManager.java     # Entry point
  Server.java                 # Server socket + admin console
  PlayerManager.java          # Per-client socket handler
  Play.java                   # Round orchestration
  Client/
    Client.java               # Terminal client
    GuiClient.java            # Swing GUI stub (empty)
    protocol/
      Generate.java           # Client → server JSON builders
      Decrypt.java            # Server → client JSON parsers
    Util/
      Atlas.java              # Sprite-sheet loader (for GUI)
      PreloadedAtlas.java
  actions/                    # BlackJackAction subclasses
  deck/                       # Card, Deck, UnicodeCards
  player/
    Player.java
    Dealer.java
    Hand.java
    state/                    # handState interface + implementations
  protocol/                   # Server-side JSON builders/parsers
    Exceptions/               # InvalidAction, InvalidBet
```

Card sprites are in `src/main/resources/Deck/` (PNG + XML atlas).
