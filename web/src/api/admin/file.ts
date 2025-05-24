import request from '@/utils/request';

export function getFileShareLink(fileId: string) {
    return request({
        url: `/files/${fileId}/share`,
        method: 'post',
    });
}

export function delFile(fileId: string) {
    return request({
        url: `/files/${fileId}/del`,
        method: 'post',
    });
}