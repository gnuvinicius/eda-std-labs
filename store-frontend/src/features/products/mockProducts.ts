import type { Product } from './products.types.ts'

export const mockProducts: Product[] = [
  {
    id: 'notebook-pro-14',
    name: 'Notebook Pro 14',
    description: 'Notebook leve com tela 14 polegadas e bateria de longa duracao.',
    price: 7599.9,
    image:
      'https://images.unsplash.com/photo-1517336714739-489689fd1ca8?auto=format&fit=crop&w=900&q=80',
    category: 'Informatica',
    stock: 12,
    rating: 4.7,
  },
  {
    id: 'fone-studio-x',
    name: 'Fone Studio X',
    description: 'Fone over-ear com cancelamento de ruido para trabalho e lazer.',
    price: 1299,
    image:
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=80',
    category: 'Audio',
    stock: 35,
    rating: 4.5,
  },
  {
    id: 'smartwatch-active',
    name: 'Smartwatch Active',
    description: 'Relogio inteligente com monitoramento de saude e GPS integrado.',
    price: 1899.5,
    image:
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=80',
    category: 'Wearables',
    stock: 20,
    rating: 4.4,
  },
  {
    id: 'teclado-mecanico-rgb',
    name: 'Teclado Mecanico RGB',
    description: 'Teclado mecanico para alta performance com switches lineares.',
    price: 649.99,
    image:
      'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?auto=format&fit=crop&w=900&q=80',
    category: 'Perifericos',
    stock: 48,
    rating: 4.8,
  },
]
