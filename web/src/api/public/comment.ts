import request from '@/utils/request';

/**
 * 获取某篇博客的评论树
 * @param postId 博客ID
 */
export function getCommentList(postId: String){
    return request({
        url: `/blog/getCommentsByPost/${postId}`,
        method: 'get',
    });
}
