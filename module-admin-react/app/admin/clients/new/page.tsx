"use client"
import React, { useState } from 'react'
import { useRouter } from 'next/navigation'
import { createClient } from '../../../../lib/api/clients'
import Button from '../../../../../components/ui/button'
import Input from '../../../../../components/ui/input'
import Card from '../../../../../components/ui/card'

export default function NewClientPage() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [error, setError] = useState<string | null>(null)
  const router = useRouter()

  async function submit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    try {
      await createClient({ name, email })
      router.push('/admin/clients')
    } catch (err: any) {
      setError(err?.message || 'Erro')
    }
  }

  return (
    <div>
      <h1>Novo Cliente</h1>
      {error && <div style={{ color: 'red' }}>{error}</div>}
      <Card className="mt-4" >
        <form onSubmit={submit} style={{ maxWidth: 480 }}>
          <div style={{ marginBottom: '.75rem' }}>
            <label className="block text-sm font-medium mb-1">Nome</label>
            <Input value={name} onChange={e => setName(e.target.value)} />
          </div>
          <div style={{ marginBottom: '.75rem' }}>
            <label className="block text-sm font-medium mb-1">Email</label>
            <Input value={email} onChange={e => setEmail(e.target.value)} />
          </div>
          <div>
            <Button type="submit">Criar</Button>
          </div>
        </form>
      </Card>
    </div>
  )
}
