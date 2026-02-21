export async function getClients() {
  const res = await fetch('/api/clients')
  if (!res.ok) throw new Error('Failed to fetch clients')
  return res.json()
}

export async function createClient(payload: { name: string; email?: string }) {
  const res = await fetch('/api/clients', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
  if (!res.ok) throw new Error('Failed to create client')
  return res.json()
}
