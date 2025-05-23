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