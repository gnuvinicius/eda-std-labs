import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'
import React from 'react'
import GarageLogo from '../../components/logo/GarageLogo'
import SidebarItem from '../../components/admin/SidebarItem'
import LogoutButton from '../../components/admin/LogoutButton'

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
      <aside style={{ width: 260, background: '#fff', color: '#111827', padding: '1rem 0', borderRight: '1px solid #eef2f6' }}>
        <div style={{ padding: '16px 20px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <GarageLogo size={48} />
        </div>

        <div style={{ padding: '0 12px 12px 12px' }}>
          <div style={{ margin: '8px 0' }}>
            <input placeholder="Procurar..." style={{ width: '100%', padding: '8px 10px', borderRadius: 8, border: '1px solid #e6e6e9' }} />
          </div>

          <nav aria-label="Admin menu">
            <ul style={{ listStyle: 'none', padding: 0, margin: '12px 0' }}>
              <SidebarItem href="/dashboard" icon={<svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M3 13h8V3H3v10zM3 21h8v-6H3v6zM13 21h8V11h-8v10zM13 3v6h8V3h-8z" fill="#0f172a"/></svg>}>Dashboard</SidebarItem>
              <SidebarItem href="/clients" icon={<svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12 12c2.7 0 8 1.35 8 4v2H4v-2c0-2.65 5.3-4 8-4zm0-2a4 4 0 100-8 4 4 0 000 8z" fill="#0f172a"/></svg>}>Clientes</SidebarItem>
              <SidebarItem href="/settings" icon={<svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M19.14 12.936a7.97 7.97 0 000-1.872l2.036-1.58a.5.5 0 00.12-.62l-1.928-3.34a.5.5 0 00-.6-.22l-2.4.96a8.054 8.054 0 00-1.62-.94l-.36-2.52A.5.5 0 0013.4 2h-3.8a.5.5 0 00-.5.42l-.36 2.52c-.57.22-1.1.5-1.62.94l-2.4-.96a.5.5 0 00-.6.22L2.7 8.86a.5.5 0 00.12.62l2.036 1.58a7.97 7.97 0 000 1.872L2.82 14.91a.5.5 0 00-.12.62l1.928 3.34c.14.24.43.34.68.24l2.4-.96c.5.44 1.05.82 1.62.94l.36 2.52c.06.28.3.5.58.5h3.8c.28 0 .52-.22.58-.5l.36-2.52c.57-.22 1.1-.5 1.62-.94l2.4.96c.25.1.54 0 .68-.24l1.928-3.34a.5.5 0 00-.12-.62l-2.04-1.97zM12 15.5A3.5 3.5 0 1112 8.5a3.5 3.5 0 010 7z" fill="#0f172a"/></svg>}>Configurações</SidebarItem>
            </ul>
          </nav>

          <div style={{ marginTop: 24, paddingTop: 12, borderTop: '1px solid #f1f5f9' }}>
            <div style={{ padding: '8px 10px', borderRadius: 8 }}>
              <LogoutButton />
            </div>
          </div>
        </div>
      </aside>

      <main style={{ flex: 1, padding: '1.5rem' }}>{children}</main>
    </div>
  )
}
