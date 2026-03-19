import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/not-found',
      name: 'home',
      component: () => import('../pages/NotFound.vue')
    },
    {
      path: '/',
      name: 'products-list',
      component: () => import('../features/products/pages/ProductListPage.vue'),
    },
    {
      path: '/produtos/:id',
      name: 'product-details',
      component: () => import('../features/products/pages/ProductDetailsPage.vue'),
    },
  ],
})

export default router
