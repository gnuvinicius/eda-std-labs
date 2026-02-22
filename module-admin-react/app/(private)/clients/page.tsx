import Card from '../../../components/ui/card'
import { getClients } from '../../../lib/externalApiCustomers'

export default async function ClientsPage() {

  

  const customers: any = await getClients('a0554d28-93d3-4cdd-9ac8-3767bb34edf7');
  console.log(customers)

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Clientes</h1>
        <a href="/clients/new">Cadastrar cliente</a>
      </div>
      <div style={{ marginTop: '1rem', display: 'grid', gap: '0.75rem' }}>
        {customers?.length === 0 && <div>Nenhum cliente</div>}
        {customers?.map((c: any) => (
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
