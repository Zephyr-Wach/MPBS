import request from '@/utils/request';

// login
interface LoginParams {
    userName: string;
    userPwd: string;
}
interface LoginResponse {
    code: number;
    message: string;
    data: {
        userId: string;
        token: string;
        username: string;
    };
}
export function login(params: LoginParams): Promise<LoginResponse> {
    return request({
        url: '/usr/login',
        method: 'post',
        data: params,
    });
}
export async function loginAndSaveToken(userName: string, userPwd: string) {
    try {
        const res = await login({ userName, userPwd });
        if (res.code === 0 && res.data.token) {
            // 保存 token 到 localStorage
            localStorage.setItem('token', res.data.token);
            console.log('登录成功，token已保存'+res.data.token);
            // 可以返回用户信息等
            return res.data;
        } else {
            throw new Error(res.message || '登录失败');
        }
    } catch (error) {
        console.error('登录出错:', error);
        throw error;
    }
}

// register
interface RegisterParams {
    userName: string;
    userPwd: string;
}
interface RegisterResponse {
    code: number;
    message: string;
    data: {
        userId: string;
        token: string;
        username: string;
    };
}
export function register(params: RegisterParams): Promise<RegisterResponse> {
    return request({
        url: '/usr/register',
        method: 'post',
        data: params,
    });
}
export async function registerAndSaveToken(userName: string, userPwd: string) {
    try {
        const res = await register({ userName, userPwd });
        if (res.code === 0 && res.data.token) {
            // 保存 token 到 localStorage
            localStorage.setItem('token', res.data.token);
            console.log('注册成功，token已保存');
            // 可以返回用户信息等
            return res.data;
        } else {
            throw new Error(res.message || '注册失败');
        }
    } catch (error) {
        console.error('注册出错:', error);
        throw error;
    }
}

// checkUserName
export const checkUserName = (userName: object) => {
    return request({
        url: '/usr/login',
        method: 'get',
        params: userName,
    });
};