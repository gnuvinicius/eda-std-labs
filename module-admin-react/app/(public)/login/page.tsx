"use client"
import React, { useState } from 'react'
import { useRouter } from 'next/navigation'
import { AuthService } from '../../../services/authService'
import Button from '../../../components/ui/button'
import Input from '../../../components/ui/input'
import GarageLogo from '../../../components/logo/GarageLogo'
import Card from '../../../components/ui/card'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const router = useRouter()

  async function submit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    try {
      await AuthService.login(username, password)
      router.push('/dashboard')
    } catch (err: any) {
      setError(err?.message || 'Network error')
    }
  }

  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', padding: 24, background: 'linear-gradient(180deg, #f8fafc 0%, #ffffff 100%)' }}>
      <Card>
        <div style={{ width: 420, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <GarageLogo size={80} />
          <div style={{ width: '100%', marginTop: 16 }}>
            <h2 style={{ margin: 0, fontSize: 18, color: '#0f172a', textAlign: 'center' }}>Acesso Administrativo</h2>
            {error && <div style={{ color: 'red', marginTop: 8, textAlign: 'center' }}>{error}</div>}

            <form onSubmit={submit} style={{ marginTop: 12, display: 'grid', gap: 12 }}>
              <div>
                <label className="block text-sm font-medium mb-1">Usuário</label>
                <Input value={username} onChange={e => setUsername(e.target.value)} />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Senha</label>
                <Input type="password" value={password} onChange={e => setPassword(e.target.value)} />
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', marginTop: 6 }}>
                <Button type="submit">Entrar</Button>
              </div>
            </form>
          </div>
        </div>
      </Card>
    </div>
  )
}
