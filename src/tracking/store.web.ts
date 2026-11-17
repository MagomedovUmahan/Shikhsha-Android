import type { Fix, Walk, WalkView } from './model';

// The browser preview has no GPS recorder or native database. Keep SQLite/WASM
// out of its bundle and never populate it with simulated activity.
const unavailable = () => new Error('Маршруты доступны в сборке Шихши на телефоне.');
export async function activeWalk(): Promise<Walk | null> { return null; }
export async function listWalks(_limit = 20, _offset = 0): Promise<Walk[]> { return []; }
export async function getWalk(_id: string, _display = true): Promise<WalkView | null> { throw unavailable(); }
export async function createWalk(_walk: Walk): Promise<void> { throw unavailable(); }
export async function editWalk(_id: string, _change: (walk: Walk) => void): Promise<Walk | null> { throw unavailable(); }
export async function appendFixes(_fixes: Fix[]): Promise<void> { throw unavailable(); }
export async function trackingWarning(_message: string): Promise<void> { throw unavailable(); }
export async function deleteWalk(_id: string): Promise<void> { throw unavailable(); }
