import { useEffect, useState } from 'react';
import {
  cardToSpriteName,
  getAtlasDimensions,
  getSprite,
  loadSpriteMap,
} from '../utils/cardSprites';

let mapLoaded = false;
let mapLoadPromise: Promise<void> | null = null;

function ensureMapLoaded(): Promise<void> {
  if (mapLoaded) return Promise.resolve();
  if (!mapLoadPromise) {
    mapLoadPromise = loadSpriteMap().then(() => { mapLoaded = true; });
  }
  return mapLoadPromise;
}

interface Props {
  card: string; // e.g. "A♥", "10♣", "[hidden]"
  scale?: number;
}

const NATIVE_W = 140;
const NATIVE_H = 190;

export function CardSprite({ card, scale = 0.5 }: Props) {
  const [ready, setReady] = useState(mapLoaded);

  useEffect(() => {
    if (!ready) {
      ensureMapLoaded().then(() => setReady(true));
    }
  }, [ready]);

  if (!ready) {
    return (
      <div
        className="card-sprite card-loading"
        style={{ width: NATIVE_W * scale, height: NATIVE_H * scale }}
      />
    );
  }

  const spriteName = cardToSpriteName(card);
  const rect = getSprite(spriteName);
  const atlas = getAtlasDimensions();

  if (!rect) {
    return (
      <div
        className="card-sprite card-unknown"
        style={{ width: NATIVE_W * scale, height: NATIVE_H * scale }}
      >
        ?
      </div>
    );
  }

  const displayW = rect.w * scale;
  const displayH = rect.h * scale;

  return (
    <div
      className="card-sprite"
      style={{
        width: displayW,
        height: displayH,
        backgroundImage: 'url(/playingCards.png)',
        backgroundPosition: `-${rect.x * scale}px -${rect.y * scale}px`,
        backgroundSize: `${atlas.w * scale}px ${atlas.h * scale}px`,
        backgroundRepeat: 'no-repeat',
        flexShrink: 0,
        borderRadius: 4 * scale,
      }}
      title={card}
    />
  );
}
