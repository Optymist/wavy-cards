interface Props {
  actions: string[];
  onAction: (action: string) => void;
}

const ACTION_META: Record<string, { label: string; sub: string }> = {
  hit:       { label: 'Hit',       sub: 'Draw a card' },
  stand:     { label: 'Stand',     sub: 'Hold your hand' },
  double:    { label: 'Double',    sub: 'Double bet + 1 card' },
  split:     { label: 'Split',     sub: 'Split the pair' },
  surrender: { label: 'Surrender', sub: 'Take half back' },
};

export function ActionButtons({ actions, onAction }: Props) {
  return (
    <div className="action-bar">
      <div className="action-bar-label">Your turn — choose an action</div>
      <div className="action-grid">
        {actions.map((action) => {
          const meta = ACTION_META[action] ?? { label: action, sub: '' };
          return (
            <button
              key={action}
              className={`action-btn btn-${action}`}
              onClick={() => onAction(action)}
            >
              <span className="btn-label">{meta.label}</span>
              {meta.sub && <span className="btn-sub">{meta.sub}</span>}
            </button>
          );
        })}
      </div>
    </div>
  );
}
