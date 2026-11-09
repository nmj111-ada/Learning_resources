<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi, handleViolationApi } from '@/views/api/stu.js'
import { queryAllApi as queryAllClazzApi } from '@/views/api/clazz.js'
import { ElMessage, ElMessageBox } from 'element-plus'

//下拉选项的value对应后端枚举，0表示不是院校学员。
const degrees = ref([{ name: '初中', value: 1 }, { name: '高中', value: 2 }, { name: '大专', value: 3 }, { name: '本科', value: 4 }, { name: '硕士', value: 5 }, { name: '博士', value: 6 }])
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
const whethers = ref([{ name: '是', value: 1 }, { name: '否', value: 0 }])
const searchStu = ref({ clazzId: '', degree: '', name: '' })
const tableData = ref([])
const clazzs = ref([])
const selectIds = ref([])
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })
const loading = ref(false)
const saving = ref(false)
const dialogFormVisible = ref(false)
const formTitle = ref('')
const labelWidth = 110
const stuFormRef = ref(null)
const stu = ref({})

//钩子函数：加载学员列表和班级选项。
onMounted(async () => {
  queryPage()
  try {
    const result = await queryAllClazzApi()
    if (result.code === 1) clazzs.value = result.data
    else ElMessage.error(result.msg)
  } catch { /* 请求错误由axios拦截器统一提示。 */ }
})

//分页查询，删除最后一条记录后自动返回上一页。
const queryPage = async () => {
  loading.value = true
  try {
    const result = await queryPageApi(searchStu.value.clazzId, searchStu.value.degree, searchStu.value.name, pagination.value.currentPage, pagination.value.pageSize)
    if (result.code === 1) {
      tableData.value = result.data.rows
      pagination.value.total = result.data.total
      selectIds.value = []
      if (!tableData.value.length && pagination.value.currentPage > 1) {
        pagination.value.currentPage--
        await queryPage()
      }
    } else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
  finally { loading.value = false }
}

//搜索、清空条件及切换分页。
const search = () => { pagination.value.currentPage = 1; queryPage() }
const clear = () => { searchStu.value = { clazzId: '', degree: '', name: '' }; search() }
const handleSizeChange = (size) => { pagination.value.pageSize = size; search() }
const handleCurrentChange = (page) => { pagination.value.currentPage = page; queryPage() }
const handleSelectionChange = (rows) => { selectIds.value = rows.map(item => item.id) }

//新增学员：初始化字段，防止使用上次编辑的ID。
const addStu = async () => {
  stu.value = { name: '', no: '', gender: null, phone: '', idCard: '', isCollege: 0, address: '', degree: null, graduationDate: null, clazzId: null }
  formTitle.value = '新增学员'
  dialogFormVisible.value = true
  await nextTick()
  stuFormRef.value?.clearValidate()
}

//编辑学员：先查询详情，再打开弹窗回显。
const updateStu = async (id) => {
  try {
    const result = await queryInfoApi(id)
    if (result.code !== 1 || !result.data) return ElMessage.error(result.msg || '学员不存在')
    stu.value = result.data
    formTitle.value = '编辑学员'
    dialogFormVisible.value = true
    await nextTick()
    stuFormRef.value?.clearValidate()
  } catch { /* 网络异常已统一提示。 */ }
}

//表单校验：正则不使用g标志，避免重复校验时状态发生变化。
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }, { min: 2, max: 10, message: '姓名长度为2到10个字符', trigger: 'blur' }],
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }, { pattern: /^\d{10}$/, message: '学号为10位数字', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }, { pattern: /^\d{17}[\dXx]$/, message: '身份证号为18位，末位允许X', trigger: 'blur' }],
  isCollege: [{ required: true, message: '请选择是否院校学员', trigger: 'change' }],
  clazzId: [{ required: true, message: '请选择所属班级', trigger: 'change' }],
  degree: [{ required: true, message: '请选择最高学历', trigger: 'change' }]
}
const save = async () => {
  if (saving.value || !await stuFormRef.value.validate().catch(() => false)) return
  saving.value = true
  try {
    const result = stu.value.id ? await updateApi(stu.value) : await addApi(stu.value)
    if (result.code === 1) {
      ElMessage.success('保存学员成功')
      dialogFormVisible.value = false
      await queryPage()
    } else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
  finally { saving.value = false }
}

//单个和批量删除共用确认逻辑，未勾选数据时不发送请求。
const removeStudents = async (ids) => {
  if (!ids.length) return ElMessage.warning('请先选择要删除的学员')
  try { await ElMessageBox.confirm(`确定删除这${ids.length}名学员吗？`, '删除学员', { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' }) }
  catch { return }
  try {
    const result = await deleteApi(ids)
    if (result.code === 1) { ElMessage.success('删除成功'); await queryPage() }
    else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
}
const delById = (id) => removeStudents([id])
const delByIds = () => removeStudents(selectIds.value)

//违纪处理：只允许1到100的整数扣分，取消输入不发送请求。
const openViolation = async (id) => {
  let score
  try {
    const input = await ElMessageBox.prompt('请输入违纪扣分（1到100的整数）', '违纪处理', {
      confirmButtonText: '确定', cancelButtonText: '取消', inputPattern: /^(?:[1-9]\d?|100)$/, inputErrorMessage: '请输入1到100的整数'
    })
    score = Number(input.value)
  } catch { return }
  try {
    const result = await handleViolationApi(id, score)
    if (result.code === 1) { ElMessage.success('违纪处理成功'); await queryPage() }
    else ElMessage.error(result.msg)
  } catch { /* 网络异常已统一提示。 */ }
}
</script>

<template>
    <!-- 顶部标题 -->
    <div>
      <div id="title">学员管理</div><br>
    </div>

    <!-- 条件搜索表单 -->
    <el-form :inline="true" :model="searchStu" class="demo-form-inline">
      <el-form-item label="姓名">
        <el-input v-model="searchStu.name" placeholder="请输入学生姓名"/>
      </el-form-item>

      <el-form-item label="最高学历">
        <el-select v-model="searchStu.degree" placeholder="请选择">
          <el-option v-for="degree in degrees" :key="degree.value" :label="degree.name" :value="degree.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="所属班级">
        <el-select v-model="searchStu.clazzId" placeholder="请选择">
          <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="clear()">清空</el-button>
      </el-form-item>
    </el-form>

    <!-- 功能按钮 -->
    <el-button type="success" @click="addStu">+ 新增学员</el-button>
    <el-button type="danger" @click="delByIds()">- 批量删除</el-button>
    <br><br>


    <!-- 列表展示 -->
    <el-table v-loading="loading" :data="tableData" border style="width: 100%" fit @selection-change="handleSelectionChange">
      <el-table-column type="selection"  align="center" width="35" />
      <el-table-column prop="name" label="姓名" align="center" width="100px" />
      <el-table-column prop="no" label="学号" align="center" width="130px" />
      <el-table-column prop="clazzName" label="班级" align="center" width="160px"/>
      <el-table-column prop="gender" label="性别" align="center" width="70px" >
        <template #default="scope">
          {{ scope.row.gender == 1 ? '男': '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" align="center" width="130px"/>
      <el-table-column prop="degree" label="最高学历" align="center" width="90px">
        <template #default="scope">
          <span v-if="scope.row.degree == 1">初中</span>
          <span v-else-if="scope.row.degree == 2">高中</span>
          <span v-else-if="scope.row.degree == 3">大专</span>
          <span v-else-if="scope.row.degree == 4">本科</span>
          <span v-else-if="scope.row.degree == 5">硕士</span>
          <span v-else-if="scope.row.degree == 6">博士</span>
          <span v-else>其他</span>
        </template>
      </el-table-column>
      <el-table-column prop="violationCount" label="违纪次数" align="center" width="85px" />
      <el-table-column prop="violationScore" label="违纪扣分" align="center" width="85px" />
      <el-table-column prop="updateTime" label="最后修改时间" align="center" width="170px"/>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="updateStu(scope.row.id)">编辑</el-button>
          <el-button type="warning" size="small" @click="openViolation(scope.row.id)">违纪</el-button>
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




  <!-- 新增/修改学员对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="50%">
    <el-form :model="stu" ref="stuFormRef" :rules="rules">
      <!-- 第一行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" :label-width="labelWidth" prop="name">
            <el-input v-model="stu.name" placeholder="请输入学员姓名"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" :label-width="labelWidth" prop="no">
            <el-input v-model="stu.no" placeholder="请输入学员学号"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第二行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" :label-width="labelWidth"  prop="gender">
            <el-select v-model="stu.gender" placeholder="请选择" style="width: 100%;">
              <el-option v-for="gender in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" :label-width="labelWidth"  prop="phone">
            <el-input v-model="stu.phone" placeholder="请输入手机号"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第三行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="身份证号" :label-width="labelWidth"  prop="idCard">
            <el-input v-model="stu.idCard" placeholder="请输入身份证号"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否院校" :label-width="labelWidth" prop="isCollege">
            <el-select v-model="stu.isCollege" placeholder="请选择" style="width: 100%;">
              <el-option v-for="w in whethers" :key="w.value" :label="w.name" :value="w.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第四行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="联系地址" :label-width="labelWidth">
            <el-input v-model="stu.address" placeholder="请输入联系地址"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历" :label-width="labelWidth" prop="degree">
            <el-select v-model="stu.degree" placeholder="请选择" style="width: 100%;">
              <el-option v-for="degree in degrees" :key="degree.value" :label="degree.name" :value="degree.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>


      <!-- 第五行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="毕业时间" :label-width="labelWidth">
            <el-date-picker v-model="stu.graduationDate" type="date" placeholder="请选择毕业时间" value-format="YYYY-MM-DD" style="width: 100%;"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属班级" :label-width="labelWidth" prop="clazzId">
            <el-select v-model="stu.clazzId" placeholder="请选择" style="width: 100%;">
              <el-option v-for="clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>


  <!-- 违纪处理 -->

</template>


<style scoped>
#title {
  font-size: 20px;
  font-weight: 600;
}
</style>
