import request from '@/utils/request';

//getUserInfo
export const getUserInfo = () => {
    return request({
        url: '/user/getInfoByToken',
        method: 'get',
    });
};

// updatePassword
export const updatePassword = (obj: Object) => {
    return request({
        url: '/user/updatePassword',
        method: 'post',
        data: obj,
    });
};

//updateUserInfo
export const updateUserInfo = (userName: string, avatarUrl: string, email: string) => {
    return request({
        url: '/user/updateInfo',
        method: 'post',
        data: {
            userName,
            avatarUrl,
            email
        }
    });
}