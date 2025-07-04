import request from '@/utils/request';

//getUserInfo
export const getUserInfo = () => {
    return request({
        url: '/NORMAL/user/getInfoByToken',
        method: 'get',
    });
};

// updatePassword
export const updatePassword = (obj: Object) => {
    return request({
        url: '/NORMAL/user/updatePassword',
        method: 'post',
        data: obj,
    });
};

//updateUserInfo
export const updateUserInfo = (userName: string, avatarUrl: string, email: string) => {
    return request({
        url: '/NORMAL/user/updateInfo',
        method: 'post',
        data: {
            userName,
            avatarUrl,
            email
        }
    });
}

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

    return request<MediaProcessDTO>({
        url: '/NORMAL/media/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};