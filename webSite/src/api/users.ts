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

export interface UserDetail{
    userId:string;
    userName:string;
    avatarUrl:string;
    email:string;
    userPermission:string;
    emailStatus:string;
}
export const getUserInfo = () => {
    return request.get<UserDetail>('/NORMAL/user/getInfoByToken')
}

interface UpdatePasswordDTO {
    oldPassword: string;
    newPassword: string;
}
export const updatePassword = (dto: UpdatePasswordDTO) => {
    return request.post<boolean>('/NORMAL/user/updatePassword', dto);
};

interface UpdateUserInfoDTO {
    userName: string;
    avatarUrl: string;
    email: string;
}

export const updateUserInfo = (dto: UpdateUserInfoDTO) => {
    return request.post<boolean>('/NORMAL/user/updateInfo', dto);
};

export interface MediaProcessDTO {
    id: string;
    filename: string;
    storagePath: string;
    uploaderId: string;
    createdAt: string;
    size: string;
    mimeType: string;
}
export const upCover = (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    return request.post<MediaProcessDTO>('/NORMAL/media/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });
};
