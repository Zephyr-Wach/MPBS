import request from '@/utils/request';
import {getUserInfo} from "@/api/user/user";

export const addComment = (obj: Object) => {
    return request({
        url: '/blog/addComment',
        method: 'post',
        data: obj,
    });
};
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
    return request({
        url: `/blog/comment/delete/${id}`,
        method: 'delete',
    });
};