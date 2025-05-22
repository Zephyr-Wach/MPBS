import axios from 'axios';
import type { InternalAxiosRequestConfig, AxiosResponse } from 'axios';

const request = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 5000,
});

// 请求拦截器
request.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.set('Authorization', `Bearer ${token}`);
        }
        return config;
    },
    error => Promise.reject(error)
);

// 响应拦截器
request.interceptors.response.use(
    (response: AxiosResponse) => response.data,
    error => {
        console.error('请求错误：', error);
        return Promise.reject(error);
    }
);

export default request;
