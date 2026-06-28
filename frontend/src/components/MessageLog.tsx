import { useEffect, useRef } from 'react';

interface Props {
  messages: string[];
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
        <div key={i} className="log-entry">
          {m}
        </div>
      ))}
      <div ref={endRef} />
    </div>
  );
}
