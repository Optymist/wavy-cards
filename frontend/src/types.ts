export type GamePhase =
  | 'connecting'
  | 'naming'
  | 'waiting'
  | 'betting'
  | 'playing'
  | 'watching';

// Server → Client
export interface GeneralMessage {
  protocolType: 'general';
  message: string;
}

export interface ConnectedUpdate {
  protocolType: 'connectedUpdate';
  message: string;
  playerName: string;
  money: number;
}

export interface TurnRequest {
  protocolType: 'turnRequest';
  currentPlayer: string;
  actions: string[];
}

export interface SplitHandState {
  hand: string[];
  handValue: number;
  state: string;
}

export interface PlayerState {
  name: string;
  state: string;
  hand: string[];
  handValue: number;
  money: number;
  bet: number;
  splitHands: SplitHandState[];
  sittingOut?: boolean;
}

export interface DealerState {
  state: string;
  hand: string[];
  handValue: number;
}

export interface UpdateMessage {
  protocolType: 'update';
  currentPlayer: string;
  players: Record<string, PlayerState>;
  dealer: DealerState;
}

export interface BetRequest {
  protocolType: 'betRequest';
  playerName: string;
  message: string;
}

export type ServerMessage =
  | GeneralMessage
  | ConnectedUpdate
  | TurnRequest
  | UpdateMessage
  | BetRequest;

// Client → Server
export interface TurnResponse {
  protocolType: 'turnResponse';
  playerName: string;
  action: string;
}

export interface BetResponse {
  protocolType: 'betResponse';
  playerName: string;
  bet: number;
}
