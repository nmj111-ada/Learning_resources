import request from '@/views/utils/request.js'

//根据姓名、学历、班级分页查询学员。
export const queryPageApi = (clazzId, degree, name, page, pageSize) =>
  request.get('/students', { params: { clazzId: clazzId || undefined, degree: degree || undefined, name: name || undefined, page, pageSize } })
//新增学员。
export const addApi = (data) => request.post('/students', data)
//根据ID回显学员。
export const queryInfoApi = (id) => request.get(`/students/${id}`)
//修改学员。
export const updateApi = (data) => request.put('/students', data)
//单个或批量删除学员。
export const deleteApi = (ids) => request.delete(`/students/${Array.isArray(ids) ? ids.join(',') : ids}`)
//违纪处理：增加违纪次数及扣分。
export const handleViolationApi = (id, score) => request.put(`/students/violation/${id}/${score}`)
