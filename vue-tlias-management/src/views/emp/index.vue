<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryPageApi, addEmpApi, queryInfoApi, updateEmpApi, deleteEmpApi } from '@/views/api/emp'
import { queryAllApi as queryAllDeptApi } from '@/views/api/dept'

// 下拉框数据与查询条件。
const jobs = ref(['班主任','讲师','学工主管','教研主管','咨询师','其他'].map((name,i) => ({ name, value:i+1 })))
const genders = ref([{name:'男',value:1},{name:'女',value:2}])
const deptList = ref([])
const searchEmp = ref({name:'',gender:'',date:[]})
const empList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const selectedIds = ref([])
const employeeFormRef = ref()
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const emptyEmployee = () => ({username:'',name:'',gender:'',phone:'',job:null,salary:null,deptId:null,entryDate:'',image:'',exprList:[]})
const employee = ref(emptyEmployee())
const rules = {
  username:[{required:true,message:'请输入用户名',trigger:'blur'},{min:2,max:20,message:'用户名长度为2-20个字',trigger:'blur'}],
  name:[{required:true,message:'请输入姓名',trigger:'blur'},{min:2,max:10,message:'姓名长度为2-10个字',trigger:'blur'}],
  gender:[{required:true,message:'请选择性别',trigger:'change'}],
  phone:[{required:true,message:'请输入手机号',trigger:'blur'},{pattern:/^1[3-9]\d{9}$/,message:'请输入有效手机号',trigger:'blur'}]
}
const getJobTitle = job => jobs.value.find(item => item.value === job)?.name || '其他'

// 查询员工列表，日期选择器清空时也可以正常查询。
const queryPage = async () => {
  loading.value = true
  try {
    const [begin,end] = searchEmp.value.date || []
    const result = await queryPageApi(searchEmp.value.name, searchEmp.value.gender, begin,end,currentPage.value,pageSize.value)
    if(result.code === 1) {
      empList.value = result.data.rows || []
      total.value = result.data.total
      if(!empList.value.length && currentPage.value > 1) { currentPage.value--; await queryPage() }
    } else ElMessage.error(result.msg)
  } catch { /* 请求错误统一由拦截器提示。 */ }
  finally { loading.value = false }
}
const handleSearch = () => { currentPage.value=1; queryPage() }
const handleReset = () => { searchEmp.value={name:'',gender:'',date:[]}; handleSearch() }
const handleSizeChange = size => { pageSize.value=size; handleSearch() }
const handleCurrentChange = page => { currentPage.value=page; queryPage() }
onMounted(async () => {
  queryPage()
  try { const result=await queryAllDeptApi(); if(result.code===1) deptList.value=result.data; else ElMessage.error(result.msg) } catch { /* 已统一提示。 */ }
})

// 新增、编辑共用表单，显示对话框后再清除旧的校验信息。
const addEmp = async () => {
  employee.value=emptyEmployee(); dialogTitle.value='新增员工'; dialogVisible.value=true
  await nextTick(); employeeFormRef.value?.clearValidate()
}
const handleEdit = async id => {
  try {
    const result=await queryInfoApi(id)
    if(result.code!==1) return ElMessage.error(result.msg)
    employee.value={...emptyEmployee(),...result.data,exprList:(result.data.exprList || []).map(expr=>({...expr,exprDate:[expr.begin,expr.end]}))}
    dialogTitle.value='修改员工'; dialogVisible.value=true
    await nextTick(); employeeFormRef.value?.clearValidate()
  } catch { /* 已统一提示。 */ }
}
// 工作经历只在提交时转换日期，避免深度监听反复修改表单。
const addExprItem = () => employee.value.exprList.push({exprDate:[],company:'',job:''})
const delExprItem = index => employee.value.exprList.splice(index,1)
const save = async () => {
  if(saving.value || !await employeeFormRef.value.validate().catch(()=>false)) return
  if(employee.value.salary !== null && employee.value.salary !== '' && (!Number.isFinite(Number(employee.value.salary)) || Number(employee.value.salary)<0)) return ElMessage.warning('薪资必须是非负数字')
  if(employee.value.exprList.some(expr=>expr.exprDate?.length!==2 || !expr.company.trim() || !expr.job.trim())) return ElMessage.warning('请补全每条工作经历的时间、公司和职位')
  saving.value=true
  try {
    const data={...employee.value,salary:employee.value.salary===''?null:employee.value.salary,exprList:employee.value.exprList.map(({exprDate,...expr})=>({...expr,begin:exprDate[0],end:exprDate[1]}))}
    const result=await (data.id?updateEmpApi(data):addEmpApi(data))
    if(result.code===1) { ElMessage.success('保存成功'); dialogVisible.value=false; await queryPage() } else ElMessage.error(result.msg)
  } catch { /* 已统一提示。 */ } finally { saving.value=false }
}
// 单条和批量删除共用确认逻辑。
const handleSelectionChange = rows => { selectedIds.value=rows.map(row=>row.id) }
const removeEmployees = async ids => {
  if(!ids.length) return ElMessage.warning('请先选择员工')
  try {
    await ElMessageBox.confirm('确定删除选中的员工吗？','提示',{type:'warning',confirmButtonText:'确定',cancelButtonText:'取消'})
    const result=await deleteEmpApi(ids)
    if(result.code===1) { ElMessage.success('删除成功'); await queryPage() } else ElMessage.error(result.msg)
  } catch { /* 取消确认或请求失败时不继续删除。 */ }
}
const handleDelete = id => removeEmployees([id])
const deleteByIds = () => removeEmployees(selectedIds.value)
</script>

<template>
  <h1>员工管理</h1> <br>
  <el-form :inline="true" :model="searchEmp">
    <el-form-item label="姓名">
      <el-input v-model="searchEmp.name" placeholder="请输入员工姓名"></el-input>
    </el-form-item>

    <el-form-item label="性别">
      <el-select v-model="searchEmp.gender" placeholder="请选择">
        <el-option label="男" value="1"></el-option>
        <el-option label="女" value="2"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="入职日期">
      <el-date-picker
        v-model="searchEmp.date"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
      ></el-date-picker>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">清空</el-button>
    </el-form-item>
  </el-form>


  <el-button type="primary" @click="addEmp"> + 新增员工</el-button>
  <el-button type="danger" @click="deleteByIds"> - 批量删除</el-button>
  <br><br>

  <!-- 表格 -->
  <el-table v-loading="loading" :data="empList" border style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" align="center"></el-table-column>
    <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
    <el-table-column label="性别" width="80" align="center">
      <template #default="scope" >
        {{ scope.row.gender == 1 ? '男' : '女' }}
      </template>
    </el-table-column>
    <el-table-column label="头像" width="170" align="center">
      <template #default="scope" >
        <img v-if="scope.row.image" :src="scope.row.image" alt="Avatar" class="avatar" />
      </template>
    </el-table-column>
    <el-table-column prop="deptName" label="部门名称" width="170" align="center"></el-table-column>
    <el-table-column label="职位" width="120" align="center">
      <template #default="scope">
        {{ getJobTitle(scope.row.job) }}
      </template>
    </el-table-column>
    <el-table-column prop="entryDate" label="入职日期" width="180" align="center"></el-table-column>
    <el-table-column prop="updateTime" label="最后操作时间" width="210" align="center"></el-table-column>
    <el-table-column label="操作" fixed="right" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <br>

  <!-- 分页 -->
  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="currentPage"
    :page-sizes="[10, 20, 30, 40]"
    :page-size="pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total"
  >
  </el-pagination>

  <!-- 新增/修改员工的对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
      <el-form ref="employeeFormRef" :model="employee" :rules="rules" label-width="80px">
        <!-- 基本信息 -->
        <!-- 第一行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="employee.username" placeholder="请输入员工用户名，2-20个字"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="employee.name" placeholder="请输入员工姓名，2-10个字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第二行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="employee.gender" placeholder="请选择性别" style="width: 100%;">
                <el-option v-for="gender in genders" :key="gender.name" :label="gender.name" :value="gender.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="employee.phone" placeholder="请输入员工手机号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第三行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="employee.job" placeholder="请选择职位" style="width: 100%;">
                <el-option v-for="job in jobs" :key="job.name" :label="job.name" :value="job.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input v-model="employee.salary" placeholder="请输入员工薪资"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第四行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select v-model="employee.deptId" placeholder="请选择部门" style="width: 100%;">
                <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="employee.entryDate" type="date" style="width: 100%;" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第五行 -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="头像">
              <!-- 暂未配置 OSS，头像上传先保留占位。 -->
              <img v-if="employee.image" :src="employee.image" class="avatar" />
              <span v-else>暂未配置图片上传</span>
            </el-form-item>
          </el-col>
        </el-row>


        <!-- 工作经历 -->
        <!-- 第六行 -->
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addExprItem">+ 添加工作经历</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第七行 ...  工作经历 -->
        <el-row :gutter="3" v-for="(expr, index) in employee.exprList" :key="index">
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker type="daterange" v-model="expr.exprDate" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input placeholder="请输入公司名称" v-model="expr.company"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="delExprItem(index)">- 删除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>

</template>

<style scoped>
.avatar {
  height: 40px;
}
.avatar-uploader .avatar {
  width: 78px;
  height: 78px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  /* 添加灰色的虚线边框 */
  border: 1px dashed var(--el-border-color);
}
</style>
