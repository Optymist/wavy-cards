import { useState, useEffect } from 'react';

interface Props {
  onConnect: (host: string, port: number, name: string) => void;
  nameTaken: boolean;
  onRetryName: (name: string) => void;
  connectError: string;
}

export function ConnectScreen({ onConnect, nameTaken, onRetryName, connectError }: Props) {
  const [host, setHost] = useState('localhost');
  const [port, setPort] = useState('2346');
  const [name, setName] = useState('');
  const [connecting, setConnecting] = useState(false);

  useEffect(() => {
    if (nameTaken) setConnecting(false);
  }, [nameTaken]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    if (nameTaken) {
      onRetryName(name.trim());
    } else {
      setConnecting(true);
      onConnect(host.trim(), Number(port), name.trim());
    }
  };

  return (
    <div className="connect-screen">
      <div className="connect-suits">♠ ♥ ♦ ♣</div>
      <h1 className="connect-title">Wavy Cards</h1>
      <p className="connect-subtitle">Multiplayer Blackjack</p>
      <div className="connect-divider" />

      <form onSubmit={handleSubmit} className="connect-form">
        {!nameTaken && (
          <>
            <label>
              Server host
              <input
                value={host}
                onChange={(e) => setHost(e.target.value)}
                placeholder="localhost"
                disabled={connecting}
              />
            </label>
            <label>
              WebSocket port
              <input
                type="number"
                value={port}
                onChange={(e) => setPort(e.target.value)}
                placeholder="2346"
                disabled={connecting}
              />
            </label>
          </>
        )}

        <label>
          {nameTaken ? 'Name taken — choose another' : 'Your name'}
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter your name"
            autoFocus
            disabled={connecting}
            className={nameTaken ? 'error' : ''}
          />
        </label>

        {connectError && (
          <div className="connect-error">{connectError}</div>
        )}

        <button type="submit" disabled={connecting}>
          {connecting ? 'Connecting…' : nameTaken ? 'Try Again' : 'Join Game'}
        </button>
      </form>
    </div>
  );
}
