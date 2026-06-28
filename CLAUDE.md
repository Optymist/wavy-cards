# Wavy Cards — Development Notes

## After every change

Always run both of the following before considering a task complete:

1. `mvn test` — all 39 tests must pass
2. `cd frontend && npm run build` — frontend must compile with no errors

## Protocol rules

### Dealer hole card
The dealer's second card (index 1) must **never** be revealed to the frontend during player turns.

- `GenerateJson.generateUpdate(game, hideDealer)` — pass `hideDealer=true` during the player-turns phase; `false` only once `dealerTurn()` begins.
- When hidden, the hole card is sent as `"[hidden]"` and `handValue` is sent as `0`. The frontend renders `0` as `"?"`.
- This applies to **both** the board state (update messages) and any general message text sent to clients — do not expose the dealer's hand string before the dealer's turn.
