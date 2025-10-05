import request from '@/utils/request';

// post blog
interface BlogParams {
    title: string;
    contentMd: string;
    coverUrl?: string;
    status: string;
}
export function postBlog(params: BlogParams) {
    return request.post('/ULTIMATE/blog/post',params);
}

export interface BlogPost {
    id: string;
    title: string;
    contentMd: string;
    coverUrl?: string;
    authorId?: string;
    status: string;
}
export function updateBlog(id: string, blog: Partial<BlogPost>) {
    return request.put(`/ULTIMATE/blog/update/${id}`,blog);
}
export function deleteBlog(id: string) {
    return request.delete(`/ULTIMATE/blog/delete/${id}`)
}