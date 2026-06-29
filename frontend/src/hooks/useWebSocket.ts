import { useCallback, useEffect, useRef, useState } from 'react';

export interface GameEvent {
  type: string;
  text: string;
  id: number;
}

function classifyEvent(text: string): string | null {
  const t = text.toLowerCase();
  if (t.includes('blackjack'))    return 'blackjack';
  if (t.includes('bust!'))        return 'bust';
  if (t.includes('you win'))      return 'win';
  if (t.includes('push'))         return 'push';
  if (t.includes('you lost'))     return 'lose';
  if (t.includes('surrendered'))  return 'surrender';
  if (t.includes('dealer busts')) return 'dealer-bust';
  return null;
}
import type {
  DealerState,
  GamePhase,
  PlayerState,
  ServerMessage,
  UpdateMessage,
} from '../types';

export interface GameState {
  players: Record<string, PlayerState>;
  dealer: DealerState | null;
  currentPlayer: string;
}

interface UseWebSocketReturn {
  phase: GamePhase;
  myName: string;
  gameState: GameState;
  availableActions: string[];
  betMessage: string;
  messages: string[];
  isConnected: boolean;
  nameTaken: boolean;
  connectError: string;
  gameEvent: GameEvent | null;
  connect: (host: string, port: number, name: string) => void;
  retryName: (name: string) => void;
  sendTurnResponse: (action: string) => void;
  sendBet: (amount: number) => void;
}

const CONNECT_TIMEOUT_MS = 10_000;

export function useWebSocket(): UseWebSocketReturn {
  const wsRef = useRef<WebSocket | null>(null);
  const eventTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const connectTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const [phase, setPhase] = useState<GamePhase>('connecting');
  const [gameEvent, setGameEvent] = useState<GameEvent | null>(null);
  const [myName, setMyName] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const [nameTaken, setNameTaken] = useState(false);
  const [connectError, setConnectError] = useState('');
  const [availableActions, setAvailableActions] = useState<string[]>([]);
  const [betMessage, setBetMessage] = useState('');
  const [messages, setMessages] = useState<string[]>([]);
  const [gameState, setGameState] = useState<GameState>({
    players: {},
    dealer: null,
    currentPlayer: '',
  });

  const addMessage = useCallback((msg: string) => {
    setMessages((prev) => [...prev.slice(-99), msg]);
  }, []);

  const sendRaw = useCallback((payload: object) => {
    if (wsRef.current?.readyState === WebSocket.OPEN) {
      wsRef.current.send(JSON.stringify(payload));
    }
  }, []);

  const sendPlain = useCallback((text: string) => {
    if (wsRef.current?.readyState === WebSocket.OPEN) {
      wsRef.current.send(text);
    }
  }, []);

  const sendTurnResponse = useCallback(
    (action: string) => {
      sendRaw({ protocolType: 'turnResponse', playerName: myName, action });
      setAvailableActions([]);
    },
    [myName, sendRaw]
  );

  const sendBet = useCallback(
    (amount: number) => {
      sendRaw({ protocolType: 'betResponse', playerName: myName, bet: amount });
      setPhase('watching');
    },
    [myName, sendRaw]
  );

  const retryName = useCallback(
    (name: string) => {
      setNameTaken(false);
      sendPlain(name);
    },
    [sendPlain]
  );

  const connect = useCallback(
    (host: string, port: number, name: string) => {
      if (wsRef.current) wsRef.current.close();

      const ws = new WebSocket(`ws://${host}:${port}`);
      wsRef.current = ws;

      if (connectTimerRef.current) clearTimeout(connectTimerRef.current);
      connectTimerRef.current = setTimeout(() => {
        if (ws.readyState !== WebSocket.OPEN) {
          ws.close();
          setConnectError(`Connection timed out — no response from ws://${host}:${port}. Check the host, port, and that the server is running.`);
        }
      }, CONNECT_TIMEOUT_MS);

      ws.onopen = () => {
        if (connectTimerRef.current) clearTimeout(connectTimerRef.current);
        setConnectError('');
        setIsConnected(true);
        setPhase('naming');
      };

      ws.onmessage = (event) => {
        let msg: ServerMessage;
        try {
          msg = JSON.parse(event.data as string) as ServerMessage;
        } catch {
          return;
        }

        switch (msg.protocolType) {
          case 'general': {
            const text = msg.message;
            if (text.includes('Pick a name')) {
              ws.send(name);
            } else if (text.includes('Name already taken')) {
              setNameTaken(true);
            } else {
              addMessage(text);
              const evType = classifyEvent(text);
              if (evType) {
                if (eventTimerRef.current) clearTimeout(eventTimerRef.current);
                setGameEvent({ type: evType, text, id: Date.now() });
                eventTimerRef.current = setTimeout(() => setGameEvent(null), 3500);
              }
            }
            break;
          }

          case 'connectedUpdate':
            setMyName(msg.playerName);
            setPhase('waiting');
            addMessage(msg.message);
            break;

          case 'betRequest':
            setBetMessage(msg.message);
            setPhase('betting');
            break;

          case 'turnRequest':
            setAvailableActions(msg.actions);
            setPhase('playing');
            break;

          case 'update': {
            const u = msg as UpdateMessage;
            setGameState({
              players: u.players,
              dealer: u.dealer,
              currentPlayer: u.currentPlayer,
            });
            setPhase((prev) =>
              prev === 'playing' || prev === 'betting' ? prev : 'watching'
            );
            break;
          }
        }
      };

      ws.onclose = (event) => {
        if (connectTimerRef.current) clearTimeout(connectTimerRef.current);
        setIsConnected(false);
        setPhase('connecting');
        if (event.code !== 1000) {
          setConnectError(`Could not connect to ws://${host}:${port} — check the host, port, and that the server is running.`);
        }
      };

      ws.onerror = () => {
        if (connectTimerRef.current) clearTimeout(connectTimerRef.current);
        setConnectError(`Connection failed to ws://${host}:${port}. Make sure the server is running and the address is correct.`);
      };
    },
    [addMessage]
  );

  useEffect(() => () => {
    wsRef.current?.close();
    if (eventTimerRef.current) clearTimeout(eventTimerRef.current);
    if (connectTimerRef.current) clearTimeout(connectTimerRef.current);
  }, []);

  return {
    phase,
    myName,
    gameState,
    availableActions,
    betMessage,
    messages,
    isConnected,
    nameTaken,
    connectError,
    gameEvent,
    connect,
    retryName,
    sendTurnResponse,
    sendBet,
  };
}
