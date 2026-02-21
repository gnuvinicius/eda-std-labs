import React from 'react'
import Link from 'next/link'
import GarageLogo from '../components/logo/GarageLogo'

export default function Home() {
  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', fontFamily: 'Inter, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 2rem', borderBottom: '1px solid #e6e6e6', background: '#ffffff' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <GarageLogo size={44} />
        </div>
        <nav>
          <Link href="/login" style={{ color: '#0f172a', textDecoration: 'none', fontWeight: 600 }}>Entrar</Link>
        </nav>
      </header>

      <main style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '3rem 1rem', background: 'linear-gradient(180deg,#ffffff 0%,#f8fafc 100%)' }}>
        <div style={{ maxWidth: 1100, width: '100%', display: 'flex', gap: 40, alignItems: 'center' }}>
          <section style={{ flex: 1 }}>
            <h1 style={{ fontSize: 40, margin: '0 0 12px', color: '#0f172a' }}>Delivery SaaS para seu negócio</h1>
            <p style={{ fontSize: 18, color: '#374151', margin: '0 0 20px' }}>
              Plataforma completa de delivery com painel administrativo, integração com PagSeguro para pagamentos, rastreio de pedidos e gestão de clientes.
            </p>

            <ul style={{ margin: '0 0 20px 0', paddingLeft: 18, color: '#374151' }}>
              <li>Integração pronta com PagSeguro (cobrança segura)</li>
              <li>Painel administrativo com cadastro de clientes e gerenciamento</li>
              <li>Relatórios e métricas essenciais</li>
            </ul>

            <div style={{ display: 'flex', gap: 12 }}>
              <Link href="/login" style={{ background: '#0ea5e9', color: 'white', padding: '10px 16px', borderRadius: 8, textDecoration: 'none', fontWeight: 600 }}>Teste grátis</Link>
              <a href="#pricing" style={{ padding: '10px 16px', borderRadius: 8, border: '1px solid #e6e6e6', textDecoration: 'none', color: '#0f172a' }}>Ver planos</a>
            </div>
          </section>

          <aside style={{ width: 420, background: 'white', border: '1px solid #e6e6e6', borderRadius: 12, padding: 20, boxShadow: '0 6px 18px rgba(15,23,42,0.06)' }}>
            <h3 style={{ marginTop: 0, color: '#0f172a' }}>Pagamentos com PagSeguro</h3>
            <p style={{ color: '#6b7280', marginBottom: 12 }}>Aceite cartões, boletos e pagamentos instantâneos usando integração segura e configurável com PagSeguro.</p>

            <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
              <div style={{ width: 56, height: 40, background: '#f1f5f9', borderRadius: 8, display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 700, color: '#0f172a' }}>PS</div>
              <div>
                <div style={{ fontWeight: 600 }}>Checkout seguro</div>
                <div style={{ color: '#6b7280' }}>Tokenização e suporte a webhooks</div>
              </div>
            </div>
          </aside>
        </div>
      </main>

      <footer style={{ borderTop: '1px solid #e6e6e6', padding: '1.5rem 2rem', background: '#ffffff' }}>
        <div style={{ maxWidth: 1100, margin: '0 auto', display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 24 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <GarageLogo size={40} />
            <div>
              <div style={{ fontWeight: 700 }}>garage474</div>
              <div style={{ color: '#6b7280', fontSize: 13 }}>eda-std-labs</div>
            </div>
          </div>

          <div style={{ display: 'flex', gap: 24, alignItems: 'center' }}>
            <div>
              <div style={{ fontWeight: 600 }}>Contato</div>
              <div style={{ color: '#6b7280', marginTop: 6 }}>
                WhatsApp: +55 11 9 9999-9999
                <br />
                Instagram: @garage474
                <br />
                LinkedIn: /company/garage474
              </div>
            </div>

            <div style={{ color: '#9ca3af', fontSize: 13 }}>© {new Date().getFullYear()} garage474 — Todos os direitos reservados</div>
          </div>
        </div>
      </footer>
    </div>
  )
}
