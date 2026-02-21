import { NextResponse } from 'next/server'

export async function POST(req: Request) {
  try {
    const body = await req.json()
    const { username, password } = body || {}
    const envUser = process.env.ADMIN_USER || 'admin'
    const envPass = process.env.ADMIN_PASS || 'password'

    if (username === envUser && password === envPass) {
      const res = NextResponse.json({ ok: true })
      res.cookies.set('admin_auth', 'true', {
        httpOnly: true,
        path: '/',
        sameSite: 'lax',
        maxAge: 60 * 60 * 24 // 1 day
      })
      return res
    }

    return NextResponse.json({ error: 'Unauthorized' }, { status: 401 })
  } catch (err) {
    return NextResponse.json({ error: 'Bad Request' }, { status: 400 })
  }
}
