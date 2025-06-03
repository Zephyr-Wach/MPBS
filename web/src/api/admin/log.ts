import request from '@/utils/request';

/**
 * 获取日志列表
 * @param {Object} params 查询参数，包括：
 *  - userAccount: 用户账号（字符串，可选）
 *  - operationType: 操作类型（字符串，可选）
 *  - startTime: 开始时间（字符串，格式 yyyy-MM-dd HH:mm:ss，可选）
 *  - endTime: 结束时间（字符串，格式 yyyy-MM-dd HH:mm:ss，可选）
 *  - page: 页码，默认1
 *  - size: 每页条数，默认10
 */
export function getLogsList(params = {}) {
    return request({
        url: '/logs/list',
        method: 'get',
        params,
    });
}
