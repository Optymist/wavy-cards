interface Props {
  card: string;
}

const RED_SUITS = new Set(['♥', '♦']);

export function Card({ card }: Props) {
  if (card === '[hidden]') {
    return <div className="card card-back" aria-label="Hidden card" />;
  }

  const suit = card.slice(-1);
  const rank = card.slice(0, -1);
  const isRed = RED_SUITS.has(suit);

  return (
    <div className={`card ${isRed ? 'card-red' : 'card-black'}`} aria-label={card}>
      <div className="card-corner card-tl">
        <span className="card-rank">{rank}</span>
        <span className="card-suit-sm">{suit}</span>
      </div>
      <span className="card-center-suit">{suit}</span>
      <div className="card-corner card-br">
        <span className="card-rank">{rank}</span>
        <span className="card-suit-sm">{suit}</span>
      </div>
    </div>
  );
}
