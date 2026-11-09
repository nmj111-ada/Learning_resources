import request from '@/views/utils/request.js'

//根据班级名称和结课日期分页查询。
export const queryPageApi = (begin, end, name, page, pageSize) =>
  request.get('/clazzs', { params: { begin: begin || undefined, end: end || undefined, name: name || undefined, page, pageSize } })
//新增班级。
export const addApi = (data) => request.post('/clazzs', data)
//根据ID回显班级。
export const queryInfoApi = (id) => request.get(`/clazzs/${id}`)
//修改班级。
export const updateApi = (data) => request.put('/clazzs', data)
//删除班级，班级下有学员时后端拒绝删除。
export const deleteApi = (id) => request.delete(`/clazzs/${id}`)
//查询所有班级供下拉框使用。
export const queryAllApi = () => request.get('/clazzs/list')
