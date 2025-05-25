import request from '@/utils/request';

export interface FilesProcessDTO {
    id: string;
    filename: string;
    storagePath: string;
    uploaderId: string;
    createdAt: string;
    size: string;
    mimeType: string;
}

// 获取文件列表
export const getFilesList = () => {
    return request<FilesProcessDTO[]>({
        url: '/files/getFilesList',
        method: 'get',
    });
};

// 上传文件（file 是一个 FormData 中的文件对象）
export const uploadFile = (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    return request<FilesProcessDTO>({
        url: '/files/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

// 下载文件
export const downloadFile = (id: string) => {
    return request({
        url: `/files/download/${id}`,
        method: 'get',
        responseType: 'blob',
    }).then(res => res);
};

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