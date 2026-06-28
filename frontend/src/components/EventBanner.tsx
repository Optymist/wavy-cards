import type { GameEvent } from '../hooks/useWebSocket';

interface Props {
  event: GameEvent;
}

const EVENT_CONFIG: Record<string, { headline: string; sub: string }> = {
  blackjack:    { headline: 'Blackjack!',     sub: 'Natural 21 — Pays 3:2'          },
  win:          { headline: 'You Win',         sub: 'Congratulations'                },
  'dealer-bust':{ headline: 'Dealer Busts',   sub: 'All standing players win'       },
  bust:         { headline: 'Bust',            sub: 'Over 21 — Bet forfeited'        },
  lose:         { headline: 'Unlucky',         sub: 'Better luck next hand'          },
  push:         { headline: 'Push',            sub: 'Bet returned'                   },
  surrender:    { headline: 'Surrendered',     sub: 'Half your bet returned'         },
};

export function EventBanner({ event }: Props) {
  const cfg = EVENT_CONFIG[event.type];
  if (!cfg) return null;

  return (
    <div className={`event-banner event-${event.type}`}>
      <span className="event-headline">{cfg.headline}</span>
      {cfg.sub && <span className="event-sub">{cfg.sub}</span>}
    </div>
  );
}
