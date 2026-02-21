"use client"
import React, { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function LogoutButton() {
  const router = useRouter()
  const [loading, setLoading] = useState(false)

  async function handleLogout() {
    setLoading(true)
    try {
      await fetch('/api/auth/logout', { method: 'POST' })
    } catch (e) {
      // ignore
    }
    setLoading(false)
    router.push('/login')
  }

  return (
    <button onClick={handleLogout} disabled={loading} style={{ background: 'transparent', border: 'none', color: '#111827', cursor: 'pointer' }}>
      {loading ? 'Saindo...' : 'Sair'}
    </button>
  )
}
