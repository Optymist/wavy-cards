import { useEffect, useRef } from 'react';

interface Props {
  messages: string[];
}

function getLogClass(text: string): string {
  const t = text.toLowerCase();
  if (t.includes('blackjack'))                               return 'log-blackjack';
  if (t.includes('you win') || t.includes('you won') || t.includes('dealer busts') || t.includes('blackjack!')) return 'log-win';
  if (t.includes('bust!') || t.includes('you lost'))        return 'log-loss';
  if (t.includes('push'))                                    return 'log-push';
  if (t.includes('surrendered'))                             return 'log-surrender';
  if (
    t.includes("dealer") ||
    t.includes('round') ||
    t.includes('stopping') ||
    t.includes('starting') ||
    t.includes("'s turn")
  )                                                          return 'log-dealer';
  return '';
}

export function MessageLog({ messages }: Props) {
  const endRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  if (messages.length === 0) return null;

  return (
    <div className="message-log">
      {messages.map((m, i) => (
        <div key={i} className={`log-entry ${getLogClass(m)}`}>
          {m}
        </div>
      ))}
      <div ref={endRef} />
    </div>
  );
}
