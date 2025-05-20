import request from '@/utils/request';

export const getSideBarList = () => {
    return request({
        url: '/public/getSideBarList',
        method: 'get',
    });
};