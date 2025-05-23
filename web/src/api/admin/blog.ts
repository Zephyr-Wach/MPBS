import request from '@/utils/request';

// post blog
interface BlogParams {
    title: string;
    contentMd: string;
    contentHtml?: string;
    coverUrl?: string;
    status: string;
}

interface BlogResponse {
    code: number;
    message: string;
    data: {

    };
}
export function postBlog(params: BlogParams): Promise<BlogResponse> {
    return request({
        url: '/blog/post',
        method: 'post',
        data: params,
    });
}

interface BlogListItem {
    id: string;
    title: string;
    coverUrl?: string;
    createAt: string;
    summary?: string;
}

interface BlogListResponse {
    code: number;
    message: string;
    data: {
        total: number;
        pages: number;
        current: number;
        list: BlogListItem[];
    };
}

export function getBlogList(page = 1, size = 10): Promise<BlogListResponse> {
    return request({
        url: '/blog/getBlogList',
        method: 'get',
        params: { page, size },
    });
}

interface BlogDetail {
    id: string;
    title: string;
    contentHtml: string;
    contentMd: string;
    coverUrl?: string;
    createdAt: string;
}

interface BlogDetailResponse {
    code: number;
    message: string;
    data: BlogDetail;
}

export function getBlogDetail(id: string): Promise<BlogDetailResponse> {
    return request({
        url: `/blog/detail/${id}`,
        method: 'get',
    });
}

// 删除媒体文件
interface MediaDeleteResponse {
    code: number;
    message: string;
    data?: any;
}
export function deleteMedia(mediaId: string): Promise<MediaDeleteResponse> {
    return request({
        url: '/media/delete',
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
        url: '/media/list',
        method: 'get',
        params: { page, size },
    });
}

export interface BlogPost {
    id: string;
    title: string;
    contentMd: string;
    contentHtml?: string;
    coverUrl?: string;
    authorId?: string;
    status: string;
    createdAt?: string;
    updatedAt?: string;
}

interface ApiResponse<T> {
    code: number;
    message: string;
    data: T;
}

export function updateBlog(id: string, blog: Partial<BlogPost>): Promise<ApiResponse<null>> {
    return request({
        url: `/blog/update/${id}`,
        method: 'put',
        data: blog,
    });
}

export function deleteBlog(id: string): Promise<ApiResponse<null>> {
    return request({
        url: `/blog/delete/${id}`,
        method: 'delete',
    });
}