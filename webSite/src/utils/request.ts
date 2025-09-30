import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { useUserStore } from '@/store/userStore.ts'

interface ApiResponse<T = any> {
    code: number
    data: T
    message: string
}

interface CustomAxiosConfig extends AxiosRequestConfig {
    retry?: boolean
}

const instance: AxiosInstance = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
    },
})

instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const userStore = useUserStore()
        const token = userStore.token
        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error: AxiosError) => Promise.reject(error)
)

instance.interceptors.response.use(
    <T>(response: AxiosResponse<ApiResponse<T>>) => {
        const { code, message } = response.data
        if (code !== 200) {
            return Promise.reject(new Error(message || 'API Error'))
        }
        return response
    },
    (error: AxiosError) => {
        if (error.response) {
            const status = error.response.status
            const msg = (error.response?.data as ApiResponse)?.message || error.message
            switch (status) {
                case 401:
                    console.error('未授权，请登录')
                    break
                case 403:
                    console.error('Forbidden:', msg)
                    break
                case 500:
                    console.error('Server Error:', msg)
                    break
                default:
                    console.error('Response Error:', msg)
            }
        } else if (error.code === 'ECONNABORTED') {
            console.error('Request Timeout')
        } else {
            console.error('Network Error:', error.message)
        }
        return Promise.reject(error)
    }
)

const request = {
    get<T = any>(url: string, config?: CustomAxiosConfig): Promise<AxiosResponse<ApiResponse<T>>> {
        return instance.get<ApiResponse<T>>(url, config)
    },
    post<T = any>(url: string, data?: any, config?: CustomAxiosConfig): Promise<AxiosResponse<ApiResponse<T>>> {
        return instance.post<ApiResponse<T>>(url, data, config)
    },
    put<T = any>(url: string, data?: any, config?: CustomAxiosConfig): Promise<AxiosResponse<ApiResponse<T>>> {
        return instance.put<ApiResponse<T>>(url, data, config)
    },
    delete<T = any>(url: string, config?: CustomAxiosConfig): Promise<AxiosResponse<ApiResponse<T>>> {
        return instance.delete<ApiResponse<T>>(url, config)
    },
    custom<T = any>(config: CustomAxiosConfig): Promise<AxiosResponse<ApiResponse<T>>> {
        return instance.request<ApiResponse<T>>(config)
    },
}

export default request
