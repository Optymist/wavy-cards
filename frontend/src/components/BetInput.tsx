import { useState } from 'react';

interface Props {
  message: string;
  onBet: (amount: number) => void;
  maxBet?: number;
  betsPlaced?: number;
  betsTotal?: number;
}

const CHIPS = [5, 10, 25, 50, 100, 250, 500];

export function BetInput({ message, onBet, maxBet, betsPlaced, betsTotal }: Props) {
  const [custom, setCustom] = useState('');

  const handleCustomSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const amount = parseInt(custom, 10);
    if (amount > 0) {
      onBet(amount);
      setCustom('');
    }
  };

  const affordable = maxBet != null ? CHIPS.filter(c => c <= maxBet) : CHIPS;
  const showAllIn = maxBet != null && !CHIPS.includes(Math.floor(maxBet)) && Math.floor(maxBet) > 0;

  return (
    <div className="bet-bar">
      <div className="bet-bar-header">
        <span className="bet-bar-label">{message}</span>
        {betsTotal != null && betsTotal > 1 && (
          <span className="bet-players-ready">
            {betsPlaced}/{betsTotal} ready
          </span>
        )}
      </div>

      <div className="bet-quick-picks">
        {affordable.map(amount => (
          <button
            key={amount}
            type="button"
            className="bet-chip-btn"
            onClick={() => onBet(amount)}
          >
            ${amount}
          </button>
        ))}
        {showAllIn && (
          <button
            type="button"
            className="bet-chip-btn bet-chip-allin"
            onClick={() => onBet(Math.floor(maxBet!))}
          >
            All&nbsp;In
          </button>
        )}
      </div>

      <form className="bet-form" onSubmit={handleCustomSubmit}>
        <span className="bet-prefix">$</span>
        <input
          type="number"
          min={1}
          max={maxBet}
          value={custom}
          onChange={(e) => setCustom(e.target.value)}
          placeholder="Custom"
          className="bet-input"
        />
        <button type="submit" className="bet-submit">Bet</button>
      </form>
    </div>
  );
}
