import { useCallback, useEffect, useRef, useState } from 'react';
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
  connect: (host: string, port: number, name: string) => void;
  retryName: (name: string) => void;
  sendTurnResponse: (action: string) => void;
  sendBet: (amount: number) => void;
}

export function useWebSocket(): UseWebSocketReturn {
  const wsRef = useRef<WebSocket | null>(null);

  const [phase, setPhase] = useState<GamePhase>('connecting');
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

      ws.onopen = () => {
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
              // Server is prompting for a name — send the chosen name
              ws.send(name);
            } else if (text.includes('Name already taken')) {
              setNameTaken(true);
            } else {
              addMessage(text);
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
        setIsConnected(false);
        setPhase('connecting');
        if (event.code !== 1000) {
          setConnectError(`Could not connect to ws://${host}:${port} — check the host, port, and that the server is running.`);
        }
      };

      ws.onerror = () => {
        setConnectError(`Connection failed to ws://${host}:${port}. Make sure the server is running and the address is correct.`);
      };
    },
    [addMessage]
  );

  useEffect(() => () => { wsRef.current?.close(); }, []);

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
    connect,
    retryName,
    sendTurnResponse,
    sendBet,
  };
}
