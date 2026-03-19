<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Divider from 'primevue/divider'
import Tag from 'primevue/tag'

import type { Product } from '../products.types'
import { useProducts } from '../useProducts.ts'

const route = useRoute()
const router = useRouter()
const { getProductById } = useProducts()

const product = ref<Product | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

async function fetchProduct(id: string) {
  loading.value = true
  error.value = null
  product.value = null
  try {
    product.value = await getProductById(id)
  } catch {
    error.value = 'Não foi possível carregar o produto. Tente novamente.'
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchProduct(String(route.params.id ?? '')))

watch(
  () => route.params.id,
  (id) => fetchProduct(String(id ?? '')),
)

function goToList() {
  router.push({ name: 'products-list' })
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
    <Button
      label="Voltar para listagem"
      icon="pi pi-arrow-left"
      severity="secondary"
      text
      @click="goToList"
    />

    <p v-if="loading" class="feedback">Carregando produto...</p>
    <p v-else-if="error" class="feedback error">{{ error }}</p>

    <Card v-else-if="product" class="details-card">
      <template #header>
        <img :src="product.image" :alt="product.name" class="product-image" />
      </template>

      <template #title>{{ product.name }}</template>

      <template #subtitle>
        <Tag :value="product.category" />
      </template>

      <template #content>
        <p class="description">{{ product.description }}</p>

        <Divider />

        <div class="meta-row">
          <strong>Preco</strong>
          <span>{{ formatCurrency(product.price) }}</span>
        </div>

        <div class="meta-row">
          <strong>Avaliacao</strong>
          <span>{{ product.rating.toFixed(1) }} / 5</span>
        </div>

        <div class="meta-row">
          <strong>Estoque</strong>
          <span>{{ product.stock }} unidades</span>
        </div>
      </template>
    </Card>

    <Card v-else class="details-card">
      <template #content>
        <p>O produto solicitado nao existe ou foi removido.</p>
      </template>
    </Card>
  </main>
</template>

<style scoped>
.page {
  margin: 0 auto;
  max-width: 920px;
  padding: 2rem 1.25rem 3rem;
}

.details-card {
  margin-top: 0.75rem;
}

.product-image {
  aspect-ratio: 16 / 8;
  display: block;
  object-fit: cover;
  width: 100%;
}

.description {
  color: #4d5a6a;
  line-height: 1.5;
  margin: 0;
}

.meta-row {
  align-items: center;
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.65rem;
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
