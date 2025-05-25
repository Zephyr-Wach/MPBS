import request from '@/utils/request';

export const addComment = (obj: Object) => {
    return request({
        url: '/blog/getCommentsByPost',
        method: 'post',
        data: obj,
    });
};

export const deleteComment  = (id: String) => {
    return request({
        url: `/blog/comment/delete/${id}`,
        method: 'post',
    });
};