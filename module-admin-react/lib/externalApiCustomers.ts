import 'server-only'
import soap from 'soap'

const base = process.env.MSREGISTER_PUBLIC_BASE_URL || `http://localhost:8085/msregister`

export async function getClients(tenantId: string): Promise<any> {
  const client = await soap.createClientAsync(`${base}/CustomerService?wsdl`);
  const [result] = await client.getAllCustomersAsync({tenantId})
  return result.return?.customers || []
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
