import React from 'react'
import Card from '../../../components/ui/card'

async function fetchClients() {
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_PATH || ''}/api/clients`, { cache: 'no-store' })
  if (!res.ok) return []
  return res.json()
}

export default async function ClientsPage() {
  const clients = await fetchClients()

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Clientes</h1>
        <a href="/admin/clients/new">Cadastrar cliente</a>
      </div>
      <div style={{ marginTop: '1rem', display: 'grid', gap: '0.75rem' }}>
        {clients.length === 0 && <div>Nenhum cliente</div>}
        {clients.map((c: any) => (
          <Card key={c.id}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <div className="font-medium">{c.name}</div>
                {c.email && <div className="text-sm text-gray-500">{c.email}</div>}
              </div>
            </div>
          </Card>
        ))}
      </div>
    </div>
  )
}
