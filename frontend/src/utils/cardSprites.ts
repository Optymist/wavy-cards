export interface SpriteRect {
  x: number;
  y: number;
  w: number;
  h: number;
}

// Parsed from playingCards.xml at runtime
let spriteMap: Map<string, SpriteRect> | null = null;
let atlasWidth = 0;
let atlasHeight = 0;

export async function loadSpriteMap(): Promise<void> {
  const response = await fetch('/playingCards.xml');
  const text = await response.text();
  const parser = new DOMParser();
  const doc = parser.parseFromString(text, 'application/xml');

  spriteMap = new Map();
  let maxW = 0;
  let maxH = 0;

  doc.querySelectorAll('SubTexture').forEach((el) => {
    const name = el.getAttribute('name') ?? '';
    const x = Number(el.getAttribute('x'));
    const y = Number(el.getAttribute('y'));
    const w = Number(el.getAttribute('width'));
    const h = Number(el.getAttribute('height'));
    spriteMap!.set(name, { x, y, w, h });
    maxW = Math.max(maxW, x + w);
    maxH = Math.max(maxH, y + h);
  });

  // <TextureAtlas> has no width/height attrs — derive from sprite extents
  const atlas = doc.querySelector('TextureAtlas');
  atlasWidth  = Number(atlas?.getAttribute('width')  || 0) || maxW;
  atlasHeight = Number(atlas?.getAttribute('height') || 0) || maxH;
}

const SUIT_MAP: Record<string, string> = {
  '♥': 'HEARTS',
  '♦': 'DIAMONDS',
  '♣': 'CLUBS',
  '♠': 'SPADES',
};

/** Convert a server card string like "A♥" or "10♣" to an atlas sprite name. */
export function cardToSpriteName(card: string): string {
  if (card === '[hidden]') return 'JOKER';
  const suitChar = card.slice(-1);
  const rank = card.slice(0, -1);
  const suit = SUIT_MAP[suitChar];
  if (!suit) return 'JOKER';
  return suit + rank; // e.g. "HEARTSA", "CLUBS10"
}

export function getSprite(spriteName: string): SpriteRect | null {
  return spriteMap?.get(spriteName) ?? null;
}

export function getAtlasDimensions(): { w: number; h: number } {
  return { w: atlasWidth, h: atlasHeight };
}
