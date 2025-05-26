import request from '@/utils/request';

export function sendCodeToEmail(email: string) {
    return request({
        url: '/email/sendCode',
        method: 'get',
        params: { email },
    });
}
