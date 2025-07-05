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
        url: '/public/blog/getBlogList',
        method: 'get',
        params: { page, size },
    });
}

interface BlogDetail {
    id: string;
    title: string;
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
        url: `/public/blog/detail/${id}`,
        method: 'get',
    });
}
interface BlogSearchResponse {
    code: number;
    message: string;
    data: {
        total: number;
        pages: number;
        current: number;
        list: BlogListItem[];
    };
}

export function searchBlog(keyword: string, page = 1, size = 1): Promise<BlogSearchResponse> {
    return request({
        url: '/public/blog/search',
        method: 'get',
        params: { keyword, page, size },
    });
}

export interface BlogTitleItem {
    id: string;
    title: string;
    contentMd: string;
    createdAt: string;
}

interface BlogSearchTitleResponse {
    code: number;
    message: string;
    data: {
        records: BlogTitleItem[];
        total: number;
    };
}

export function searchBlogTitle(keyword: string): Promise<BlogSearchTitleResponse> {
    return request({
        url: '/public/blog/searchTitle',
        method: 'get',
        params: { keyword },
    });
}
