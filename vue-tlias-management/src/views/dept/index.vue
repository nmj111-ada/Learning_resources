<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { queryAllApi, addApi, queryByIdApi, updateApi, deleteApi } from '@/views/api/dept.js'
import { ElMessage, ElMessageBox } from 'element-plus'

//钩子函数
onMounted(() => {
  search()
})

//查询
const search = async() => {
  // const result = await.get();
  // if (result.date.code == 1) {
  //   deptList.value = result.date.data;
  // }

  try {
    const result = await queryAllApi()
    if (result.code === 1) {
      deptList.value = result.data
    }
  } catch (error) {
    console.error('查询部门失败：', error)
  }
}

const deptList = ref([])
const saving = ref(false)
const formTitle = ref('')

//dialog对话框
const dialogFormVisible = ref(false)
const dept = ref({
  name: ''
})

//新增部门
const addDept = () => {
  dept.value = { name: '' } // 重置部门信息
  dialogFormVisible.value = true
  formTitle.value = '新增部门'
  nextTick(() => deptFormRef.value?.clearValidate())
}

//编辑
const edit = async (id) => {
  try {
  const result = await queryByIdApi(id)
  //重置表单的校验规则-提示信息
  if(deptFormRef.value) {
    deptFormRef.value.clearValidate()
  }
  if(result.code == 1) {
    dept.value = result.data
    dialogFormVisible.value = true
    formTitle.value = '编辑部门'
  } else {
    ElMessage.error('查询部门失败：' + result.msg)
  }
  } catch { /* 请求错误已统一提示。 */ }
}

//保存部门信息
const save = async () => {
  // 表单校验
  if(saving.value) return
  const valid = await deptFormRef.value.validate().catch(() => false)
  if (!valid) return

  // 有 ID 修改，没有 ID 新增；禁止重复点击保存。
  saving.value = true
  try {
    const result = dept.value.id ? await updateApi(dept.value) : await addApi(dept.value)
    if (result.code === 1) {
      ElMessage.success('保存部门成功')
      dialogFormVisible.value = false
      await search()
    } else ElMessage.error(result.msg || '保存失败')
  } catch { /* 请求错误由拦截器统一提示。 */ }
  finally { saving.value = false }
}

//表单校验
const rules = ref({
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '部门名称长度在 2 到 10 个字符之间', trigger: 'blur' }
  ]
})
const deptFormRef = ref(null)

//删除
const deleteDept = async (id) => {
  //弹出确认框
  try {
    await ElMessageBox.confirm('您确认删除该部门吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return // 用户取消，不发送删除请求。
  }

  try {
    const result = await deleteApi(id)
    if (result.code === 1) {
      ElMessage.success('删除成功')
      await search()
    } else {
      ElMessage.error(result.msg || '删除失败')
    }
  } catch {
    ElMessage.error('删除请求失败，请检查网络或后端')
  }
}

</script>

<template>
  <h1>部门管理</h1>

  <div class = "container mb-4">
    <el-button type="primary" @click="addDept">+ 新增部门</el-button>
  </div>

  <div class = "container">
    <el-table :data="deptList" border style="width: 100%" >
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column property="name" label="部门名称" width="260" align="center" />
    <el-table-column property="updateTime" label="最后操作时间" width="300" align="center" />
    <el-table-column label="操作" align="center" >
      <template #default="scope">
    <div class="mb-4">
      <el-button type="warning"  @click="edit(scope.row.id)" text>编辑</el-button>
      <el-button type="danger"  @click="deleteDept(scope.row.id)" text>删除</el-button>
    </div>
  </template>
    </el-table-column>

  </el-table>
  </div>

  <!-- 对话框组件 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form ref="deptFormRef" :model="dept" :rules="rules" >
      <el-form-item label="部门名称" label-width= "80px" prop="name">
        <el-input v-model="dept.name" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
  /* 给按钮的容器加点下边距 */
  .container {
    margin-bottom: 20px;
  }

  /* 如果想让标题和按钮之间也有距离，可以调 h1 的 margin */
  h1 {
    margin-bottom: 20px;
  }
</style>
