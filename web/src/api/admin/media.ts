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
        url: '/SENIOR/media/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const generateUrl = (mediaId: string) => {
    return request<string>({
        url: `/SENIOR/media/generateUrl/${mediaId}`,
        method: 'get',
    });
};

// 删除媒体文件
interface MediaDeleteResponse {
    code: number;
    message: string;
    data?: any;
}
export function deleteMedia(mediaId: string): Promise<MediaDeleteResponse> {
    return request({
        url: '/SENIOR/media/delete',
        method: 'delete',
        params: { mediaId },  // delete接口用params传mediaId
    });
}

// 媒体分页列表返回的单条数据结构
interface MediaItem {
    id: string;
    filename: string;
    storagePath: string;
    uploaderId: string;
    createdAt: string;
    size: string;
    mimeType: string;
    isPublic?: string;
}

// 分页返回数据结构
interface MediaListData {
    total: number;
    pages: number;
    current: number;
    size: number;
    records: MediaItem[];
}

interface MediaListResponse {
    code: number;
    message: string;
    data: MediaListData;
}

// 获取媒体文件列表（分页）
export function getMediaList(page = 1, size = 10): Promise<MediaListResponse> {
    return request({
        url: '/SENIOR/media/list',
        method: 'get',
        params: { page, size },
    });
}
