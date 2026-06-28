import { useState } from 'react';

interface Props {
  onConnect: (host: string, port: number, name: string) => void;
  nameTaken: boolean;
  onRetryName: (name: string) => void;
  connectError: string;
}

export function ConnectScreen({ onConnect, nameTaken, onRetryName, connectError }: Props) {
  const [host, setHost] = useState('10.0.2.2');
  const [port, setPort] = useState('2346');
  const [name, setName] = useState('');
  const [connecting, setConnecting] = useState(false);

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

  // Reset connecting state if an error comes back
  if (connectError && connecting) setConnecting(false);

  return (
    <div className="connect-screen">
      <h1>♠ Wavy Cards ♠</h1>
      <h2>Blackjack</h2>
      <form onSubmit={handleSubmit} className="connect-form">
        {!nameTaken && (
          <>
            <label>
              Server host
              <input
                value={host}
                onChange={(e) => setHost(e.target.value)}
                placeholder="10.0.2.2"
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
            placeholder="Enter name"
            autoFocus
            disabled={connecting}
            className={nameTaken ? 'error' : ''}
          />
        </label>

        {connectError && (
          <div className="connect-error">{connectError}</div>
        )}

        <button type="submit" disabled={connecting}>
          {connecting ? 'Connecting…' : nameTaken ? 'Try again' : 'Join game'}
        </button>
      </form>
    </div>
  );
}
