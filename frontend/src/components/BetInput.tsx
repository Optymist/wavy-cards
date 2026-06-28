import { useState } from 'react';

interface Props {
  message: string;
  onBet: (amount: number) => void;
}

export function BetInput({ message, onBet }: Props) {
  const [value, setValue] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const amount = parseInt(value, 10);
    if (amount > 0) {
      onBet(amount);
      setValue('');
    }
  };

  return (
    <div className="bet-bar">
      <div className="bet-bar-label">{message}</div>
      <form className="bet-form" onSubmit={handleSubmit}>
        <span className="bet-prefix">$</span>
        <input
          type="number"
          min={1}
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder="0"
          autoFocus
          className="bet-input"
        />
        <button type="submit" className="bet-submit">Place Bet</button>
      </form>
    </div>
  );
}
