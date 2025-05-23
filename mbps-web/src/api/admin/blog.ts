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
