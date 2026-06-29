import { ActionButtons } from './components/ActionButtons';
import { BalanceBar } from './components/BalanceBar';
import { BetInput } from './components/BetInput';
import { ConnectScreen } from './components/ConnectScreen';
import { EventBanner } from './components/EventBanner';
import { GameBoard } from './components/GameBoard';
import { MessageLog } from './components/MessageLog';
import { useWebSocket } from './hooks/useWebSocket';
import './App.css';

export default function App() {
  const {
    phase,
    myName,
    gameState,
    availableActions,
    betMessage,
    messages,
    nameTaken,
    connectError,
    gameEvent,
    waitingForBets,
    connect,
    retryName,
    sendTurnResponse,
    sendBet,
    disconnect,
  } = useWebSocket();

  const showConnect = phase === 'connecting' || phase === 'naming' || nameTaken;
  const myPlayer = gameState.players[myName];

  const allPlayers = Object.values(gameState.players);
  const activePlayers = allPlayers.filter(p => !p.sittingOut);
  const betsPlaced = activePlayers.filter(p => p.bet > 0).length;
  const betsTotal = activePlayers.length;
  const betsRemaining = betsTotal - betsPlaced;

  return (
    <div className="app">
      {showConnect ? (
        <ConnectScreen
          onConnect={connect}
          nameTaken={nameTaken}
          onRetryName={retryName}
          connectError={connectError}
        />
      ) : (
        <div className="game-screen">
          <header className="table-header">
            <span className="table-title">Wavy Cards</span>
            <span className="table-dot" />
            <span className="table-subtitle">Blackjack</span>
            <button className="leave-btn" onClick={disconnect} aria-label="Leave game">
              Leave
            </button>
          </header>

          {phase === 'waiting' && !waitingForBets && (
            <div className="waiting-banner">Waiting for round to start…</div>
          )}

          {waitingForBets && phase !== 'betting' && (
            <div className="waiting-banner waiting-banner--bets">
              {betsRemaining > 0
                ? `Waiting for ${betsRemaining} player${betsRemaining !== 1 ? 's' : ''} to bet…  (${betsPlaced}/${betsTotal} ready)`
                : 'All bets placed — starting round…'}
            </div>
          )}

          {gameEvent && <EventBanner key={gameEvent.id} event={gameEvent} />}

          {(gameState.dealer || Object.keys(gameState.players).length > 0) && (
            <GameBoard
              players={gameState.players}
              dealer={gameState.dealer}
              currentPlayer={gameState.currentPlayer}
              myName={myName}
            />
          )}

          <MessageLog messages={messages} />

          {myPlayer && (
            <BalanceBar balance={myPlayer.money} bet={myPlayer.bet} />
          )}

          {phase === 'playing' && availableActions.length > 0 && (
            <ActionButtons actions={availableActions} onAction={sendTurnResponse} />
          )}

          {phase === 'betting' && (
            <BetInput
              message={betMessage}
              onBet={sendBet}
              maxBet={myPlayer?.money}
              betsPlaced={betsPlaced}
              betsTotal={betsTotal}
            />
          )}
        </div>
      )}
    </div>
  );
}
