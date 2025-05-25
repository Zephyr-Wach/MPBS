import request from '@/utils/request';

//getUserInfoList
export const getUserInfoList = () => {
    return request({
        url: '/admin/getUserList',
        method: 'get',
    });
};
interface UserInfoDTO {
    userName?: string;
    userPwd?: string;
    email?: string;
    avatarUrl?: string;
    userPermission?: string;
}

export const updateUserInfo = (userId: string, info: UserInfoDTO) => {
    return request({
        url: `/admin/updateInfo?userId=${encodeURIComponent(userId)}`,
        method: 'post',
        data: info,
        headers: {
            'Content-Type': 'application/json',
        },
    });
};
