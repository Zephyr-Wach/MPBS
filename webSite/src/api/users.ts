import request from '@/utils/request'

// Login
interface UserInfo {
    refreshToken: string;
    token: string
    userId: string
    userName: string
}
export function loginByEmail(user: { email: string; userPwd?: string }) {
    return request.post<UserInfo>('/public/usr/emailLogin', user)
}