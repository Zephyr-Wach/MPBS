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
        url: '/ULTIMATE/blog/post',
        method: 'post',
        data: params,
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
}

interface ApiResponse<T> {
    code: number;
    message: string;
    data: T;
}

export function updateBlog(id: string, blog: Partial<BlogPost>): Promise<ApiResponse<null>> {
    return request({
        url: `/ULTIMATE/blog/update/${id}`,
        method: 'put',
        data: blog,
    });
}

export function deleteBlog(id: string): Promise<ApiResponse<null>> {
    return request({
        url: `/ULTIMATE/blog/delete/${id}`,
        method: 'delete',
    });
}