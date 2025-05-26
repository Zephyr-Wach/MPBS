import request from '@/utils/request';

interface checkParams {
    userName: string;
    userPwd: string;
}
export function checkEmail(params: checkParams){
    return request({
        url: '/email/checkEmail',
        method: 'post',
        data: params,
    });
}