import { useEffect, useRef, useState } from 'react';

interface Props {
  balance: number;
  bet: number;
}

export function BalanceBar({ balance, bet }: Props) {
  const [delta, setDelta] = useState<number | null>(null);
  const prevRef = useRef(balance);

  useEffect(() => {
    if (prevRef.current !== balance) {
      setDelta(balance - prevRef.current);
      prevRef.current = balance;
      const t = setTimeout(() => setDelta(null), 2000);
      return () => clearTimeout(t);
    }
  }, [balance]);

  return (
    <div className="balance-bar">
      <span className="balance-label">Balance</span>
      <span className="balance-amount">${Math.floor(balance).toLocaleString()}</span>
      {delta !== null && (
        <span
          key={balance}
          className={`balance-delta ${delta >= 0 ? 'balance-delta--pos' : 'balance-delta--neg'}`}
        >
          {delta >= 0 ? '+' : ''}${Math.floor(Math.abs(delta)).toLocaleString()}
        </span>
      )}
      {bet > 0 && (
        <span className="balance-bet-pill">BET ${Math.floor(bet).toLocaleString()}</span>
      )}
    </div>
  );
}
