import { NextResponse } from 'next/server'
import fs from 'fs/promises'
import path from 'path'

const DATA_DIR = path.join(process.cwd(), 'data')
const DATA_FILE = path.join(DATA_DIR, 'clients.json')

async function ensureFile() {
  try {
    await fs.mkdir(DATA_DIR, { recursive: true })
    await fs.access(DATA_FILE)
  } catch (e) {
    await fs.writeFile(DATA_FILE, '[]', 'utf-8')
  }
}

function isAuthed(req: Request) {
  const cookie = req.headers.get('cookie') || ''
  return cookie.includes('admin_auth=true')
}

export async function GET(req: Request) {
  if (!isAuthed(req)) return NextResponse.json({ error: 'Unauthorized' }, { status: 401 })
  await ensureFile()
  const raw = await fs.readFile(DATA_FILE, 'utf-8')
  const data = JSON.parse(raw || '[]')
  return NextResponse.json(data)
}

export async function POST(req: Request) {
  if (!isAuthed(req)) return NextResponse.json({ error: 'Unauthorized' }, { status: 401 })
  const body = await req.json()
  const { name, email } = body || {}
  if (!name) return NextResponse.json({ error: 'Invalid' }, { status: 400 })
  await ensureFile()
  const raw = await fs.readFile(DATA_FILE, 'utf-8')
  const data = JSON.parse(raw || '[]')
  const id = Date.now().toString()
  const item = { id, name, email }
  data.push(item)
  await fs.writeFile(DATA_FILE, JSON.stringify(data, null, 2), 'utf-8')
  return NextResponse.json(item, { status: 201 })
}
