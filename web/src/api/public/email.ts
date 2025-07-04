import request from '@/utils/request';

export function sendCodeToEmail(email: string) {
    return request({
        url: '/public/email/sendCode',
        method: 'get',
        params: { email },
    });
}
