const rateLimitMap = new Map<string, { count: number; lastReset: number }>();

export function checkRateLimit(ip: string): boolean {
  const WINDOW_MS = 60 * 1000; // 1 minute window
  const MAX_REQUESTS = 5; // Max 5 requests per minute per IP
  const now = Date.now();

  const record = rateLimitMap.get(ip) || { count: 0, lastReset: now };

  if (now - record.lastReset > WINDOW_MS) {
    // Reset the window
    record.count = 1;
    record.lastReset = now;
  } else {
    record.count++;
  }

  rateLimitMap.set(ip, record);
  return record.count <= MAX_REQUESTS;
}

