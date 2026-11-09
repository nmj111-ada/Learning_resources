<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi } from '@/views/api/clazz.js'
import { queryAllApi as queryAllEmpApi } from '@/views/api/emp.js'
import { ElMessage, ElMessageBox } from 'element-plus'

//学科列表与后端的数字枚举保持一致。
const subjects = ref([{ name: 'Java', value: 1 }, { name: '前端', value: 2 }, { name: '大数据', value: 3 }, { name: 'Python', value: 4 }, { name: 'Go', value: 5 }, { name: '嵌入式', value: 6 }])
const searchClazz = ref({ date: [], name: '' })
const tableData = ref([])
const emps = ref([])
const loading = ref(false)
const saving = ref(false)
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })
const dialogFormVisible = ref(false)
const formTitle = ref('')
const labelWidth = 90
const clazzFormRef = ref(null)
const clazz = ref({})

//钩子函数：查询班级列表，并加载班主任下拉框。
onMounted(async () => {
  queryPage()
  try {
    const result = await queryAllEmpApi()
    if (result.code === 1) emps.value = result.data
    else ElMessage.error(result.msg)
  } catch { /* 请求错误由axios拦截器统一提示。 */ }
})

//分页查询；清空日期选择器时date可能是null，不能直接取下标。
const queryPage = async () => {
  loading.value = true
  try {
    const date = searchClazz.value.date || []
    const result = await queryPageApi(date[0], date[1], searchClazz.value.name, pagination.value.currentPage, pagination.value.pageSize)
    if (result.code === 1) {
      tableData.value = result.data.rows
      pagination.value.total = result.data.total
      if (!tableData.value.length && pagination.value.currentPage > 1) {
        pagination.value.currentPage--
        await queryPage()
      }
    } else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
  finally { loading.value = false }
}

//重新搜索和切换每页条数时回到第一页，避免旧页码导致空列表。
const search = () => { pagination.value.currentPage = 1; queryPage() }
const handleSizeChange = (size) => { pagination.value.pageSize = size; search() }
const handleCurrentChange = (page) => { pagination.value.currentPage = page; queryPage() }
const clear = () => { searchClazz.value = { date: [], name: '' }; search() }

//新增班级：重置表单，等对话框渲染完成后清除旧校验信息。
const addClazz = async () => {
  clazz.value = { name: '', room: '', beginDate: '', endDate: '', subject: null, masterId: null }
  formTitle.value = '新增班级'
  dialogFormVisible.value = true
  await nextTick()
  clazzFormRef.value?.clearValidate()
}

//编辑班级：根据ID读取详情，再回显到对话框。
const updateClazz = async (id) => {
  try {
    const result = await queryInfoApi(id)
    if (result.code !== 1 || !result.data) return ElMessage.error(result.msg || '班级不存在')
    clazz.value = result.data
    formTitle.value = '编辑班级'
    dialogFormVisible.value = true
    await nextTick()
    clazzFormRef.value?.clearValidate()
  } catch { /* 网络异常已统一提示。 */ }
}

//表单校验，额外检查结课日期不能早于开课日期。
const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }, { min: 4, max: 30, message: '班级名称长度为4到30个字符', trigger: 'blur' }],
  room: [{ max: 20, message: '教室名称不超过20个字符', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请选择开课日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课日期', trigger: 'change' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }]
}
const save = async () => {
  if (saving.value || !await clazzFormRef.value.validate().catch(() => false)) return
  if (clazz.value.endDate < clazz.value.beginDate) return ElMessage.error('结课日期不能早于开课日期')
  saving.value = true
  try {
    const result = clazz.value.id ? await updateApi(clazz.value) : await addApi(clazz.value)
    if (result.code === 1) {
      ElMessage.success('保存班级成功')
      dialogFormVisible.value = false
      await queryPage()
    } else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
  finally { saving.value = false }
}

//删除班级：用户取消时直接返回，业务失败时显示后端提示。
const delById = async (id) => {
  try { await ElMessageBox.confirm('您确认删除该班级吗？', '删除班级', { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' }) }
  catch { return }
  try {
    const result = await deleteApi(id)
    if (result.code === 1) { ElMessage.success('删除成功'); await queryPage() }
    else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
}
</script>

<template>

    <!-- 顶部标题 -->
    <div>
      <div id="title">班级管理</div><br>
    </div>

    <!-- 条件搜索表单 -->
    <el-form :inline="true" :model="searchClazz" class="demo-form-inline">
      <el-form-item label="班级名称">
        <el-input v-model="searchClazz.name" placeholder="请输入班级名称"/>
      </el-form-item>

      <el-form-item label="结课时间">
        <el-date-picker
          v-model="searchClazz.date"
          type="daterange"
          range-separator=" 至 "
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="clear()">清空</el-button>
      </el-form-item>
    </el-form>

    <!-- 功能按钮 -->
    <el-button type="success" @click="addClazz">+ 新增班级</el-button>
    <br><br>

    <!-- 列表展示 -->
    <el-table v-loading="loading" :data="tableData" border style="width: 100%" fit>
      <el-table-column type="index" label="序号" width="55" align="center"/>
      <el-table-column prop="name" label="班级名称" align="center" width="200px" />
      <el-table-column prop="room" label="班级教室" align="center" width="100px"/>
      <el-table-column prop="masterName" label="班主任" align="center" width="100px"/>
      <el-table-column prop="beginDate" label="开课时间" align="center" width="150px"/>
      <el-table-column prop="endDate" label="结课时间" align="center" width="150px"/>
      <el-table-column prop="status" label="状态" align="center" width="130px" />
      <el-table-column prop="updateTime" label="最后修改时间" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="updateClazz(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="delById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>

    <!-- 分页组件Pagination -->
    <el-pagination
      v-model:current-page="pagination.currentPage"
      v-model:page-size="pagination.pageSize"
      :page-sizes="[5, 10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />




  <!-- 新增/修改员工对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="560px">
    <el-form :model="clazz" ref="clazzFormRef" :rules="rules">
      <el-form-item label="班级名称" :label-width="labelWidth" prop="name">
        <el-input v-model="clazz.name" placeholder="请输入班级名称" />
      </el-form-item>

      <el-form-item label="班级教室" :label-width="labelWidth" prop="room">
        <el-input v-model="clazz.room" placeholder="请输入班级教室"/>
      </el-form-item>

      <el-form-item label="开课时间" :label-width="labelWidth" prop="beginDate">
        <el-date-picker v-model="clazz.beginDate" type="date" placeholder="请选择开课时间" value-format="YYYY-MM-DD" style="width: 100%;"/>
      </el-form-item>

      <el-form-item label="结课时间" :label-width="labelWidth" prop="endDate">
        <el-date-picker v-model="clazz.endDate" type="date" placeholder="请选择结课时间" value-format="YYYY-MM-DD" style="width: 100%;"/>
      </el-form-item>

      <el-form-item label="班主任" :label-width="labelWidth">
        <el-select v-model="clazz.masterId" placeholder="请选择班主任" style="width: 100%;">
          <el-option v-for="emp in emps" :key="emp.id" :label="emp.name" :value="emp.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="学科" :label-width="labelWidth" prop="subject">
        <el-select v-model="clazz.subject" placeholder="请选择学科" style="width: 100%;">
          <el-option v-for="sub in subjects" :key="sub.value" :label="sub.name" :value="sub.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>

</template>


<style scoped>
#title {
  font-size: 20px;
  font-weight: 600;
}
</style>
