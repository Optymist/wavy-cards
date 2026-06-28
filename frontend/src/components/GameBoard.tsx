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


function getActiveSplitIndex(splitHands: SplitHandState[]): number {
  return splitHands.findIndex(sh => sh.state === 'normal' || sh.state === '?');
}

function StateBadge({ state }: { state: string }) {
  if (!state || state === 'normal' || state === '?') return null;
  return <span className={`badge badge-${state}`}>{state.toUpperCase()}</span>;
}

function ValueChip({ value }: { value: number }) {
  return <span className="value-chip">{value === 0 ? '?' : value}</span>;
}

export function GameBoard({ players, dealer, currentPlayer, myName }: Props) {
  const myEntry = players[myName] ?? null;
  const otherEntries = Object.entries(players).filter(([n]) => n !== myName) as [string, PlayerState][];
  const isMeTurn = currentPlayer === myName;

  return (
    <div className="board">

      {/* ── Dealer — far end of table ── */}
      <div className="board-dealer">
        <span className="area-label">Dealer</span>
        {dealer && (
          <div className="dealer-content">
            <HandRow cards={dealer.hand} />
            <ValueChip value={dealer.handValue} />
          </div>
        )}
      </div>

      <div className="table-rail" />

      {/* ── Other players — middle, scrollable ── */}
      {otherEntries.length > 0 ? (
        <div className="board-others">
          {otherEntries.map(([name, p]) => {
            const isCurrent = name === currentPlayer;
            return (
              <div
                key={name}
                className={`other-player${isCurrent ? ' other-active' : ''}`}
              >
                <div className="other-header">
                  <span className="player-name">{name}</span>
                  {isCurrent && <span className="turn-dot" />}
                  <div className="player-money-row">
                    <span className="money">${Math.floor(p.money)}</span>
                    {p.bet > 0 && <span className="bet-chip">BET ${Math.floor(p.bet)}</span>}
                  </div>
                </div>

                <div className="other-hand">
                  {p.splitHands && p.splitHands.length > 0 ? (() => {
                    const activeSplitIdx = isCurrent ? getActiveSplitIndex(p.splitHands!) : -1;
                    return p.splitHands!.map((sh: SplitHandState, i: number) => {
                      const isActive = i === activeSplitIdx;
                      const isDone = sh.state !== 'normal' && sh.state !== '?';
                      return (
                        <div key={i} className={`player-hand-row${isActive ? ' split-hand--active' : isDone ? ' split-hand--done' : ''}`}>
                          <span className="split-label">Hand {i + 1}</span>
                          <HandRow cards={sh.hand} />
                          <div className="hand-meta">
                            <ValueChip value={sh.handValue} />
                            <StateBadge state={sh.state} />
                          </div>
                        </div>
                      );
                    });
                  })() : (
                    <div className="player-hand-row">
                      <HandRow cards={p.hand} />
                      <div className="hand-meta">
                        <ValueChip value={p.handValue} />
                        <StateBadge state={p.state} />
                      </div>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="board-spacer" />
      )}

      <div className="table-rail" />

      {/* ── My hand — near end of table, always featured ── */}
      {myEntry ? (
        <div className={`board-me${isMeTurn ? ' board-me--active' : ''}`}>
          <div className="me-header">
            <span className="me-name">You</span>
            {isMeTurn && <span className="turn-dot" />}
            {myEntry.bet > 0 && (
              <div className="player-money-row">
                <span className="bet-chip">BET ${Math.floor(myEntry.bet).toLocaleString()}</span>
              </div>
            )}
          </div>

          {myEntry.splitHands && myEntry.splitHands.length > 0 ? (() => {
            const activeSplitIdx = isMeTurn ? getActiveSplitIndex(myEntry.splitHands!) : -1;
            return myEntry.splitHands!.map((sh: SplitHandState, i: number) => {
              const isActive = i === activeSplitIdx;
              const isDone = sh.state !== 'normal' && sh.state !== '?';
              return (
                <div key={i} className={`me-split-hand${isActive ? ' me-split-hand--active' : isDone ? ' me-split-hand--done' : ''}`}>
                  <div className="split-hand-header">
                    <span className="split-label">Hand {i + 1}</span>
                    {isActive && <span className="split-playing-tag">Now Playing</span>}
                  </div>
                  <div className="me-hand">
                    <HandRow cards={sh.hand} />
                    <div className="me-hand-meta">
                      <ValueChip value={sh.handValue} />
                      <StateBadge state={sh.state} />
                    </div>
                  </div>
                </div>
              );
            });
          })() : (
            <div className="me-hand">
              <HandRow cards={myEntry.hand} />
              <div className="me-hand-meta">
                <ValueChip value={myEntry.handValue} />
                <StateBadge state={myEntry.state} />
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="board-me">
          <span className="me-name">You</span>
          <p className="me-waiting">Waiting for the next round…</p>
        </div>
      )}

    </div>
  );
}
