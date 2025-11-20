export const apiFetch = async (path: string, opts: RequestInit = {}) => {
  const base = process.env.NEXT_PUBLIC_API_BASE_URL;
  const res = await fetch(`${base}${path}`, {
    credentials: 'include', // essencial para cookies HttpOnly
    headers: { 'Content-Type': 'application/json' },
    ...opts
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}; 