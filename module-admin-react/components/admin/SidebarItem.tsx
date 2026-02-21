"use client"
import React, { useState } from 'react'
import Link from 'next/link'

export default function SidebarItem({ href, icon, children }: { href: string; icon?: React.ReactNode; children: React.ReactNode }) {
  const [hover, setHover] = useState(false)
  return (
    <li style={{ marginBottom: 8 }}>
      <Link
        href={href}
        onMouseEnter={() => setHover(true)}
        onMouseLeave={() => setHover(false)}
        style={{
          textDecoration: 'none',
          display: 'flex',
          alignItems: 'center',
          gap: 12,
          padding: '8px 10px',
          borderRadius: 8,
          color: '#111827',
          background: hover ? '#eef2ff' : 'transparent',
          transform: hover ? 'translateX(4px)' : 'none',
          transition: 'background .15s ease, transform .12s ease'
        }}
      >
        {icon}
        <span style={{ fontWeight: 500 }}>{children}</span>
      </Link>
    </li>
  )
}
