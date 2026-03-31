import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import type { Product } from '../features/products/products.types'

export class HttpClientService {
  private readonly http: AxiosInstance

  constructor() {
    this.http = axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://192.168.122.223:8080',
      timeout: 10_000,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    this.registerRequestInterceptor()
    this.registerResponseInterceptor()
  }

  private registerRequestInterceptor(): void {
    this.http.interceptors.request.use(
      (config) => {
        // Adicionar token de autenticação se necessário
        // const token = localStorage.getItem('token')
        // if (token) config.headers.Authorization = `Bearer ${token}`
        return config
      },
      (error) => Promise.reject(error),
    )
  }

  private registerResponseInterceptor(): void {
    this.http.interceptors.response.use(
      (response) => response,
      (error) => {
        const status = error.response?.status
        const message = error.response?.data?.message ?? error.message

        console.error(`[HttpClient] ${status ?? 'Network Error'}: ${message}`)
        return Promise.reject(error)
      },
    )
  }

  async listProducts(): Promise<Product[]> {
    const response: AxiosResponse<Product[]> = await this.http.get('/api/v1/products')
    return response.data
  }

  async getProductById(id: string): Promise<Product> {
    const response: AxiosResponse<Product> = await this.http.get(`/api/v1/products/${id}`)
    return response.data
  }
}

export const httpClientService = new HttpClientService()

