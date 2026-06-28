import type { DealerState, PlayerState, SplitHandState } from '../types';
import { Card } from './Card';

interface Props {
  players: Record<string, PlayerState>;
  dealer: DealerState | null;
  currentPlayer: string;
  myName: string;
}

function HandRow({ cards }: { cards: string[] }) {
  return (
    <div className="hand-row">
      {cards.map((c, i) => <Card key={i} card={c} />)}
    </div>
  );
}

function StateBadge({ state }: { state: string }) {
  if (!state || state === 'normal' || state === '?') return null;
  return <span className={`badge badge-${state}`}>{state.toUpperCase()}</span>;
}

function ValueChip({ value }: { value: number }) {
  return <span className="value-chip">{value === 0 ? '?' : value}</span>;
}

export function GameBoard({ players, dealer, currentPlayer, myName }: Props) {
  const playerList = Object.entries(players) as [string, PlayerState][];

  return (
    <div className="board">

      {/* ── Dealer ── */}
      <div className="board-dealer">
        <span className="area-label">Dealer</span>
        {dealer && (
          <div className="dealer-content">
            <HandRow cards={dealer.hand} />
            <ValueChip value={dealer.handValue} />
          </div>
        )}
      </div>

      {/* ── Players ── */}
      <div className="board-players">
        {playerList.map(([name, p]) => {
          const isMe = name === myName;
          const isCurrent = name === currentPlayer;
          return (
            <div
              key={name}
              className={`player-card ${isMe ? 'is-me' : ''} ${isCurrent ? 'is-active' : ''}`}
            >
              <div className="player-meta">
                <span className="player-name">
                  {name}
                  {isMe && <span className="you-tag">YOU</span>}
                </span>
                <div className="player-money-row">
                  <span className="money">${Math.floor(p.money)}</span>
                  {p.bet > 0 && <span className="bet-chip">BET ${Math.floor(p.bet)}</span>}
                  {isCurrent && <span className="turn-dot" />}
                </div>
              </div>
              {p.splitHands && p.splitHands.length > 0 ? (
                p.splitHands.map((sh: SplitHandState, i: number) => (
                  <div key={i} className="player-hand-row">
                    <span className="split-label">Hand {i + 1}</span>
                    <HandRow cards={sh.hand} />
                    <div className="hand-meta">
                      <ValueChip value={sh.handValue} />
                      <StateBadge state={sh.state} />
                    </div>
                  </div>
                ))
              ) : (
                <div className="player-hand-row">
                  <HandRow cards={p.hand} />
                  <div className="hand-meta">
                    <ValueChip value={p.handValue} />
                    <StateBadge state={p.state} />
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>

    </div>
  );
}
