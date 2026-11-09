import request from '@/views/utils/request.js'

//分页查询操作日志。
export const queryPageApi = (page, pageSize) => request.get('/log/page', { params: { page, pageSize } })
