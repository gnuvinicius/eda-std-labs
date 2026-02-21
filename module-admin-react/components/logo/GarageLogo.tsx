import React from 'react'

export default function GarageLogo({ size = 72 }: { size?: number }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
      <svg
        width={size}
        height={size}
        viewBox="0 0 100 100"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        role="img"
        aria-label="garage474 logo"
      >
        <defs>
          <linearGradient id="g1" x1="0" x2="1">
            <stop offset="0%" stopColor="#0ea5e9" />
            <stop offset="100%" stopColor="#0369a1" />
          </linearGradient>
        </defs>
        <rect x="5" y="5" width="90" height="90" rx="14" fill="url(#g1)" />
        <g transform="translate(22,22) scale(0.56)" fill="#fff">
          <path d="M44.4 18.6c-2.7-4.8-7.8-7.6-13.4-7.6-8.8 0-15.4 6.9-15.4 15.6 0 8.6 6.6 15.5 15.4 15.5 5.6 0 10.7-2.8 13.4-7.6l-5.2-3.1c-1.5 2.9-4.4 4.9-8.2 4.9-5.3 0-8.7-3.9-8.7-9s3.4-9 8.7-9c3.8 0 6.6 2 8.2 4.9l5.2-3.1z" />
          <path d="M58.6 48H41.4v6.6H58.6V48zM61.8 34.4h-3.2V24.8H41.4v6.6h8.8v2.9H41.4v6.6h20.4V34.4z" />
        </g>
      </svg>

      <div style={{ textAlign: 'center', lineHeight: 1 }}>
        <div style={{ fontSize: 20, fontWeight: 700, color: '#0f172a' }}>garage474</div>
        <div style={{ fontSize: 12, color: '#6b7280', marginTop: 2 }}>eda-std-labs</div>
      </div>
    </div>
  )
}
