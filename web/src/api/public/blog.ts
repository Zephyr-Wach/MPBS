import request from "@/utils/request";

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
