import request from '@/views/utils/request.js'

//分页查询员工，使用 params 编码姓名和日期，空条件不发送。
export const queryPageApi = (name, gender, begin, end, page, pageSize) =>
  request.get('/emps', { params: { name: name || undefined, gender: gender || undefined, begin: begin || undefined, end: end || undefined, page, pageSize } })
//新增员工，同时保存工作经历。
export const addEmpApi = (data) => request.post('/emps', data)
//根据ID查询员工，编辑时回显。
export const queryInfoApi = (id) => request.get(`/emps/${id}`)
//修改员工及工作经历。
export const updateEmpApi = (data) => request.put('/emps', data)
//单个和批量删除共用接口，ID以逗号分隔。
export const deleteEmpApi = (ids) => request.delete('/emps', { params: { ids: Array.isArray(ids) ? ids.join(',') : ids } })
//查询班主任下拉列表。
export const queryAllApi = () => request.get('/emps/list')
//修改当前登录员工的密码，操作人由后端从Token获取。
export const updatePasswordApi = (data) => request.put('/emps/password', data)
