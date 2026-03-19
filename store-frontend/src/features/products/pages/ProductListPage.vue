<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import Button from 'primevue/button'
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'

import type { Product } from '../products.types'
import { useProducts } from '../useProducts.ts'

const router = useRouter()
const { listProducts } = useProducts()

const search = ref('')
const products = ref<Product[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

onMounted(async () => {
  loading.value = true
  error.value = null
  try {
    products.value = await listProducts()
  } catch {
    error.value = 'Não foi possível carregar os produtos. Tente novamente.'
  } finally {
    loading.value = false
  }
})

const filteredProducts = computed(() => {
  const normalizedSearch = search.value.trim().toLowerCase()

  if (!normalizedSearch) {
    return products.value
  }

  return products.value.filter((product) => {
    return (
      product.name.toLowerCase().includes(normalizedSearch) ||
      product.category.toLowerCase().includes(normalizedSearch)
    )
  })
})

function goToDetails(productId: string) {
  router.push({ name: 'product-details', params: { id: productId } })
}

function formatCurrency(value: number) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value)
}
</script>

<template>
  <main class="page">
    <header class="header">
      <h1>Produtos</h1>
      <p>Explore os produtos disponiveis na loja.</p>
      <InputText v-model="search" placeholder="Buscar por nome ou categoria" class="search" />
    </header>

    <p v-if="loading" class="feedback">Carregando produtos...</p>
    <p v-else-if="error" class="feedback error">{{ error }}</p>

    <section v-else class="product-grid">
      <Card v-for="product in filteredProducts" :key="product.id" class="product-card">
        <template #header>
          <img :src="product.image" :alt="product.name" class="product-image" />
        </template>

        <template #title>{{ product.name }}</template>

        <template #subtitle>
          <Tag :value="product.category" severity="info" />
        </template>

        <template #content>
          <p class="description">{{ product.description }}</p>
          <p class="price">{{ formatCurrency(product.price) }}</p>
          <p class="stock">Estoque: {{ product.stock }}</p>
        </template>

        <template #footer>
          <Button label="Ver detalhes" icon="pi pi-arrow-right" @click="goToDetails(product.id)" />
        </template>
      </Card>
    </section>
  </main>
</template>

<style scoped>
.page {
  margin: 0 auto;
  max-width: 1120px;
  padding: 2rem 1.25rem 3rem;
}

.header {
  margin-bottom: 1.5rem;
}

.header h1 {
  margin-bottom: 0.5rem;
}

.header p {
  color: #5f6b7a;
  margin-bottom: 1rem;
}

.search {
  max-width: 420px;
  width: 100%;
}

.product-grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
}

.product-card {
  height: 100%;
}

.product-image {
  aspect-ratio: 16 / 10;
  display: block;
  object-fit: cover;
  width: 100%;
}

.description {
  color: #4d5a6a;
  margin: 0 0 0.75rem;
}

.price {
  font-size: 1.1rem;
  font-weight: 700;
  margin: 0 0 0.35rem;
}

.stock {
  color: #5f6b7a;
  margin: 0;
}

.feedback {
  margin-top: 2rem;
  text-align: center;
  color: #5f6b7a;
}

.feedback.error {
  color: #dc2626;
}
</style>
