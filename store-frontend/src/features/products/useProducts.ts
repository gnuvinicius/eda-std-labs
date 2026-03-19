import { httpClientService } from '@/services/httpClient.service'

export function useProducts() {
  function listProducts() {
    return httpClientService.listProducts()
  }

  function getProductById(id: string) {
    return httpClientService.getProductById(id)
  }

  return {
    listProducts,
    getProductById,
  }
}
