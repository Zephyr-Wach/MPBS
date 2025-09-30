import request from '@/utils/request'

// Login
interface UserInfo {
    refreshToken: string;
    token: string
    userId: string
    userName: string
}

export function login( user:{userName:string;userPwd:string}){
    return request.post<UserInfo>('/public/usr/login',user)
}

export function register( user:{userName:string; userPwd:string;email:string}){
    return request.post('/public/usr/register',user)
}