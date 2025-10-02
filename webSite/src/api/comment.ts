import request from '@/utils/request';
import {getUserInfo} from "@/api/users.ts";

export const addComment = (obj: object) => {
    return request.post('/public/blog/addComment', obj)
}

export const addCommentWithEmailCheck = async (obj: Object) => {
    try {
        const userInfoResponse = await getUserInfo();
        const userData = userInfoResponse.data;

        if (userData.email && userData.emailStatus === 'confirmed') {
            // 邮箱已验证，继续发表评论
            return await addComment(obj);
        } else {
            // 邮箱未验证，抛出错误
            return Promise.reject(new Error('请完成邮箱验证再评论'));
        }
    } catch (error) {
        // 如果获取用户信息失败，也可以抛出错误
        return Promise.reject(error);
    }
};

export const deleteComment  = (id: String) => {
    return request.delete(`/public/blog/comment/delete/${id}`);
};

/**
 * 获取某篇博客的评论树
 * @param postId 博客ID
 */
export function getCommentList(postId: String){
    return request.get(`/public/blog/getCommentsByPost/${postId}`)
}
