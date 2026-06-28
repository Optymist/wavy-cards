import { ActionButtons } from './components/ActionButtons';
import { BetInput } from './components/BetInput';
import { ConnectScreen } from './components/ConnectScreen';
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
    connect,
    retryName,
    sendTurnResponse,
    sendBet,
  } = useWebSocket();

  const showConnect = phase === 'connecting' || phase === 'naming' || nameTaken;

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
          {phase === 'waiting' && (
            <div className="waiting-banner">Waiting for round to start…</div>
          )}

          {(gameState.dealer || Object.keys(gameState.players).length > 0) && (
            <GameBoard
              players={gameState.players}
              dealer={gameState.dealer}
              currentPlayer={gameState.currentPlayer}
              myName={myName}
            />
          )}

          <MessageLog messages={messages} />

          {phase === 'playing' && availableActions.length > 0 && (
            <ActionButtons actions={availableActions} onAction={sendTurnResponse} />
          )}

          {phase === 'betting' && (
            <BetInput message={betMessage} onBet={sendBet} />
          )}
        </div>
      )}
    </div>
  );
}
