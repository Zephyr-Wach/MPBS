import axios from 'axios';
import type { InternalAxiosRequestConfig, AxiosResponse } from 'axios';

const request = axios.create({
    baseURL: '/api',
    timeout: 30000,
});

// 标记当前是否正在刷新token，避免多次刷新请求
let isRefreshing = false;
// 刷新token请求的等待队列，刷新token成功后重新发起请求
let failedQueue: {
    resolve: (value?: unknown) => void;
    reject: (error: any) => void;
}[] = [];

const processQueue = (error: any, token: string | null = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

// 请求拦截器：请求头带上token
request.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('token');
        if (token && config.headers) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

// 响应拦截器：检测token失效，自动刷新
request.interceptors.response.use(
    (response: AxiosResponse) => response.data,
    async (error) => {
        const originalRequest = error.config;
        // 如果响应是401，且不是刷新token接口本身，尝试刷新token
        if (error.response?.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                // 如果正在刷新，把请求加入队列，等待刷新完
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers['Authorization'] = 'Bearer ' + token;
                    return request(originalRequest);
                }).catch(err => Promise.reject(err));
            }

            originalRequest._retry = true;
            isRefreshing = true;

            const refreshToken = localStorage.getItem('refreshToken');
            if (!refreshToken) {
                // 无refreshToken，直接登出或提示登录失效
                return Promise.reject(error);
            }

            try {
                const res = await axios.post('/api/auth/refresh', { refreshToken });

                const newToken = res.data.token;
                const newRefreshToken = res.data.refreshToken;

                localStorage.setItem('token', newToken);
                localStorage.setItem('refreshToken', newRefreshToken);

                originalRequest.headers['Authorization'] = 'Bearer ' + newToken;
                processQueue(null, newToken);

                return request(originalRequest);
            } catch (err) {
                processQueue(err, null);
                // 刷新失败，登出或提示重新登录
                localStorage.removeItem('token');
                localStorage.removeItem('refreshToken');
                return Promise.reject(err);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);

export default request;
