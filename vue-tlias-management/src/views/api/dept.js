import request from '@/views/utils/request.js'

//查询全部部门信息
export const queryAllApi = () => request.get('/depts');

//新增
export const addApi = (data) => request.post('/depts', data);

//根据id查询部门
export const queryByIdApi = (id) => request.get(`/depts/${id}`);

//修改
export const updateApi = (data) => request.put('/depts', data);

//删除
export const deleteApi = (id) => request.delete('/depts', { params: { id } });