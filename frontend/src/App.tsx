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
    connect,
    retryName,
    sendTurnResponse,
    sendBet,
  } = useWebSocket();

  const showConnect = phase === 'connecting' || phase === 'naming' || nameTaken;
  const myPlayer = gameState.players[myName];

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
          </header>

          {phase === 'waiting' && (
            <div className="waiting-banner">Waiting for round to start…</div>
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
            <BetInput message={betMessage} onBet={sendBet} maxBet={myPlayer?.money} />
          )}
        </div>
      )}
    </div>
  );
}
