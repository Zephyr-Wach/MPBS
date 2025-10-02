import request from "@/utils/request";

// list
export interface BlogListItem {
    id: string;
    title: string;
    contentMd: string;
    createdAt: string;
    coverUrl?: string;
}

interface BlogListResponse {
    total: number;
    pages: number;
    current: number;
    records: BlogListItem[];
}
export function getBlogList( params: { page?: number; size?: number } = { page: 1, size: 10 }) {
    return request.get<BlogListResponse>('/public/blog/getBlogList', { params })
}

export function searchBlog(keyword: string, page = 1, size = 1) {
    return request.get<BlogListResponse>('/public/blog/search', {params: {keyword, page, size}})
}

export interface BlogTitleItem {
    id: string;
    title: string;
    contentMd: string;
    createdAt: string;
}

interface BlogSearchTitleResponse {
    records: BlogTitleItem[];
    total: number;
}

export function searchBlogTitle(keyword: string) {
    return request.get<BlogSearchTitleResponse>('public/blog/searchTitle', {
        params: { keyword }
    })
}

// detail
interface BlogDetail {
    id: string;
    title: string;
    contentMd: string;
    coverUrl?: string;
    createdAt: string;
}

export function getBlogDetail(id: string) {
    return request.get<BlogDetail>(`/public/blog/detail/${id}`)
}
