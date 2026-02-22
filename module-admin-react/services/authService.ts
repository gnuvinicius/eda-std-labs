export class AuthService {
  static async login(username: string, password: string) {
    const res = await fetch('/api/auth', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    })
    if (!res.ok) {
      let msg = 'Unauthorized'
      try {
        const body = await res.json()
        msg = body?.error || msg
      } catch {}
      throw new Error(msg)
    }
    return res.json()
  }
}
