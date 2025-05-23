import request from '@/utils/request';

export interface MediaProcessDTO {
    id: string;
    filename: string;
    storagePath: string;
    uploaderId: string;
    createdAt: string;
    size: string;
    mimeType: string;
}
export const uploadFile = (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    return request<MediaProcessDTO>({
        url: '/media/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const generateUrl = (mediaId: string) => {
    return request<string>({
        url: `/media/generateUrl/${mediaId}`,
        method: 'get',
    });
};
