import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'
import React from 'react'

export const metadata = {
  title: 'Admin'
}

export default async function AdminLayout({ children }: { children: React.ReactNode }) {
  const cookieStore = await cookies()
  const auth = cookieStore.get('admin_auth')
  if (!auth) {
    redirect('/login')
  }

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <aside style={{ width: 240, background: '#f8fafc', color: '#111827', padding: '1rem', borderRight: '1px solid #e5e7eb' }}>
        <h2 style={{ margin: '0 0 1rem 0' }}>Admin</h2>
        <nav>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
            <li style={{ marginBottom: '.5rem' }}>
              <a href="/admin/dashboard" style={{ color: '#111827', textDecoration: 'none' }}>Dashboard</a>
            </li>
            <li style={{ marginBottom: '.5rem' }}>
              <a href="/admin/clients" style={{ color: '#111827', textDecoration: 'none' }}>Clientes</a>
            </li>
          </ul>
        </nav>
      </aside>
      <main style={{ flex: 1, padding: '1.5rem' }}>{children}</main>
    </div>
  )
}
