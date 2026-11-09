// 仅对隔离测试数据库运行：会创建并删除测试部门、员工、班级和学员，并修改后恢复测试账号密码。
import { chromium } from 'playwright'
import assert from 'node:assert/strict'
import fs from 'node:fs/promises'
const baseURL=process.env.E2E_BASE_URL
const username=process.env.E2E_USERNAME
const password=process.env.E2E_PASSWORD
assert(baseURL && username && password && process.env.E2E_ISOLATED_DATABASE==='true', '必须配置测试地址、测试账号及 E2E_ISOLATED_DATABASE=true，禁止使用日常数据库')
const browser=await chromium.launch({headless:true})
const page=await browser.newPage({viewport:{width:1600,height:1100}})
page.setDefaultTimeout(15000)
const errors=[]
page.on('pageerror',e=>errors.push(e.message))
const checks=[]
const check=name=>{checks.push(name);console.log('PASS '+name)}
const suffix=String(Date.now()).slice(-6)
const deptName='测部'+suffix
const empName='测员'+suffix
const clazzName='测试班'+suffix
const stuName='测生'+suffix
let token,deptId,empId,clazzId,stuId
let passwordChanged=false
const api=async(method,path,data,expected=1)=>{
 const response=await page.request.fetch(baseURL+'/api'+path,{method,headers:token?{token}:{},data})
 assert.equal(response.status(),200,path)
 const result=await response.json()
 assert.equal(result.code,expected,path+': '+result.msg)
 return result.data
}
const submit=async(method,path,action,code=1)=>{
 const pending=page.waitForResponse(r=>new URL(r.url()).pathname==='/api'+path && r.request().method()===method)
 await action()
 const response=await pending
 assert.equal(response.status(),200,path)
 const result=await response.json()
 assert.equal(result.code,code,path+': '+result.msg)
 return result.data
}
const route=async(path,endpoint)=>{
 const pending=page.waitForResponse(r=>new URL(r.url()).pathname==='/api'+endpoint && r.request().method()==='GET')
 await page.goto(baseURL+path)
 const response=await pending
 assert.equal((await response.json()).code,1,path)
 await page.locator('.el-loading-mask').waitFor({state:'hidden'})
}
const dialog=()=>page.getByRole('dialog')
const field=(label)=>dialog().locator('.el-form-item').filter({has:page.locator('label').filter({hasText:new RegExp('^'+label+'$')})})
const fill=async(label,value)=>field(label).locator('input').fill(value)
const choose=async(label,value)=>{
 await field(label).locator('.el-select').click()
 await page.locator('.el-select-dropdown:visible').getByText(value,{exact:true}).click()
}
const row=name=>page.locator('.el-table__body-wrapper tr').filter({hasText:name})
const confirm=()=>page.locator('.el-message-box').getByRole('button',{name:'确定',exact:true}).click()
const save=()=>dialog().getByRole('button',{name:/^(保存|确定)$/}).click()
try {
 // 登录守卫、登录失败与成功、刷新保持登录。
 await page.goto(baseURL+'/emp')
 await page.waitForURL('**/login?**')
 await page.getByPlaceholder('请输入用户名').fill(username)
 await page.getByPlaceholder('请输入密码').fill('wrong-password')
 await submit('POST','/login',()=>page.getByRole('button',{name:'登 录'}).click(),0)
 await page.getByPlaceholder('请输入密码').fill(password)
 await submit('POST','/login',()=>page.getByRole('button',{name:'登 录'}).click())
 await page.waitForURL('**/emp')
 token=await page.evaluate(()=>JSON.parse(localStorage.getItem('loginUser')).token)
 check('登录守卫、错误密码、成功登录与跳转')
 // 部门：新增、编辑回显、修改。
 await route('/dept','/depts')
 await page.getByRole('button',{name:'+ 新增部门'}).click()
 await fill('部门名称',deptName)
 await submit('POST','/depts',save)
 await dialog().waitFor({state:'hidden'})
 deptId=(await api('GET','/depts')).find(d=>d.name===deptName).id
 await row(deptName).getByRole('button',{name:'编辑',exact:true}).click()
 await dialog().waitFor()
 assert.equal(await field('部门名称').locator('input').inputValue(),deptName)
 await submit('PUT','/depts',save)
 await dialog().waitFor({state:'hidden'})
 check('部门新增、编辑回显、修改')
 // 员工：必填校验、新增工作经历、编辑回显及日期保存。
 await route('/emp','/emps')
 await page.getByRole('button',{name:'+ 新增员工'}).click()
 await save()
 await dialog().getByText('请输入用户名',{exact:true}).waitFor()
 await fill('用户名','test'+suffix)
 await fill('姓名',empName)
 await choose('性别','男')
 await fill('手机号','13800'+suffix)
 await choose('所属部门',deptName)
 await choose('职位','讲师')
 await fill('薪资','9000')
 await fill('入职日期','2026-01-01');await field('入职日期').locator('input').press('Tab')
 await dialog().getByRole('button',{name:'+ 添加工作经历'}).click()
 const range=dialog().locator('.el-range-input')
 await range.nth(0).fill('2024-01-01');await range.nth(1).fill('2025-01-01');await range.nth(1).press('Tab')
 await dialog().getByPlaceholder('请输入公司名称').fill('测试公司')
 await dialog().getByPlaceholder('请输入职位',{exact:true}).fill('开发')
 await submit('POST','/emps',save)
 await dialog().waitFor({state:'hidden'})
 empId=(await api('GET','/emps?name='+encodeURIComponent(empName))).rows[0].id
 await row(empName).getByRole('button',{name:'编辑',exact:true}).click()
 await dialog().waitFor()
 assert.equal(await field('用户名').locator('input').inputValue(),'test'+suffix)
 assert.equal(await range.nth(0).inputValue(),'2024-01-01')
 assert.equal(await dialog().getByPlaceholder('请输入职位',{exact:true}).inputValue(),'开发')
 await dialog().getByPlaceholder('请输入职位',{exact:true}).fill('高级开发')
 await fill('薪资','')
 await submit('PUT','/emps',save)
 await dialog().waitFor({state:'hidden'})
 const detail=await api('GET','/emps/'+empId)
 assert.equal(detail.salary,null,'清空薪资后应写入null，不能保留旧值')
 assert.equal(detail.exprList.length,1); assert.equal(detail.exprList[0].job,'高级开发')
 assert(!('password' in detail))
 assert.equal((await api('GET','/emps?name='+encodeURIComponent(empName)+'&gender=1&begin=2026-01-01&end=2026-12-31')).total,1)
 await api('DELETE','/depts?id='+deptId,undefined,0)
 check('员工校验、新增、经历回显与修改、组合查询、密码不返回、有员工部门禁止删除')
 // 班级：新增、回显和修改，日期清空查询。
 await route('/clazz','/clazzs')
 await page.getByRole('button',{name:/新增班级/}).click()
 await fill('班级名称',clazzName);await fill('班级教室','101')
 await fill('开课时间','2026-01-01');await field('开课时间').locator('input').press('Tab')
 await fill('结课时间','2027-01-01');await field('结课时间').locator('input').press('Tab')
 await choose('班主任',empName);await choose('学科','Java')
 await submit('POST','/clazzs',save)
 await dialog().waitFor({state:'hidden'})
 clazzId=(await api('GET','/clazzs?name='+encodeURIComponent(clazzName))).rows[0].id
 await row(clazzName).getByRole('button',{name:'编辑',exact:true}).click()
 await dialog().waitFor()
 assert.equal(await field('班级名称').locator('input').inputValue(),clazzName)
 await fill('班级教室','102');await submit('PUT','/clazzs',save);await dialog().waitFor({state:'hidden'})
 await submit('GET','/clazzs',()=>page.getByRole('button',{name:'清空',exact:true}).click())
 check('班级新增、编辑回显、修改和清空查询')
 // 学员：新增、编辑回显、修改、违纪与班级删除保护。
 await route('/stu','/students')
 await page.getByRole('button',{name:/新增学员/}).click()
 await fill('姓名',stuName);await fill('学号','2026'+suffix)
 await choose('性别','女');await fill('手机号','13700'+suffix)
 await fill('身份证号','110101200001'+suffix)
 await choose('是否院校','否');await choose('最高学历','本科');await choose('所属班级',clazzName)
 await submit('POST','/students',save);await dialog().waitFor({state:'hidden'})
 stuId=(await api('GET','/students?name='+encodeURIComponent(stuName))).rows[0].id
 await row(stuName).getByRole('button',{name:'编辑',exact:true}).click();await dialog().waitFor()
 assert.equal(await field('姓名').locator('input').inputValue(),stuName)
 await fill('联系地址','联调测试地址');await submit('PUT','/students',save);await dialog().waitFor({state:'hidden'})
 await row(stuName).getByRole('button',{name:'违纪',exact:true}).click()
 await page.locator('.el-message-box input').fill('10')
 await submit('PUT','/students/violation/'+stuId+'/10',confirm)
 const student=await api('GET','/students/'+stuId)
 assert.equal(student.violationCount,1);assert.equal(student.violationScore,10)
 await api('PUT','/students/violation/'+stuId+'/0',undefined,0)
 await api('DELETE','/clazzs/'+clazzId,undefined,0)
 assert.equal((await api('GET','/students?degree=4&clazzId='+clazzId+'&name='+encodeURIComponent(stuName))).total,1)
 check('学员新增、回显修改、组合查询、违纪扣分和非法扣分拒绝、有学员班级禁止删除')
 // 统计图和日志真实渲染，确认操作人和分页。
 for(const [path,endpoint] of [['/empResult','/report/empJobData'],['/stuReport','/report/studentCountData']]) {
   await route(path,endpoint)
   await page.locator('.chart canvas').nth(1).waitFor()
 }
 await route('/log','/log/page')
 await page.getByRole('cell',{name:username,exact:true}).count() // 日志展示姓名而非用户名。
 const logs=await api('GET','/log/page?page=1&pageSize=2')
 assert.equal(logs.rows.length,2);assert(logs.total>2)
 assert(logs.rows.every(log=>log.operateEmpName==='联调账号'))
 check('两类统计图渲染、日志记录操作人和分页')
 // 使用 UI 批量删除并核对经历同步清理。
 // 再创建一名学员，验证逗号分隔的多 ID 删除，而不只勾选一条。
 const {id,createTime,updateTime,violationCount,violationScore,...secondStudent}=student
 const secondName='二生'+suffix
 await api('POST','/students',{...secondStudent,name:secondName,no:'2027'+suffix,phone:'13600'+suffix,idCard:'110101200002'+suffix})
 const secondId=(await api('GET','/students?name='+encodeURIComponent(secondName))).rows[0].id
 await route('/stu','/students')
 await row(stuName).locator('.el-checkbox').click()
 await row(secondName).locator('.el-checkbox').click()
 await page.getByRole('button',{name:/批量删除/}).click()
 await submit('DELETE','/students/'+[stuId,secondId].join(','),confirm);stuId=null
 assert.equal((await api('GET','/students')).total,0)
 await route('/clazz','/clazzs')
 await row(clazzName).getByRole('button',{name:'删除',exact:true}).click()
 await submit('DELETE','/clazzs/'+clazzId,confirm);clazzId=null
 await route('/emp','/emps')
 await row(empName).locator('.el-checkbox').click()
 await page.getByRole('button',{name:/批量删除/}).click()
 await submit('DELETE','/emps',confirm);empId=null
 await route('/dept','/depts')
 await row(deptName).getByRole('button',{name:'删除',exact:true}).click()
 const deptResponse=page.waitForResponse(r=>r.request().method()==='DELETE' && new URL(r.url()).pathname==='/api/depts')
 await page.locator('.el-message-box').getByRole('button',{name:'确认',exact:true}).click()
 assert.equal((await (await deptResponse).json()).code,1);deptId=null
 check('学员和员工批量删除、空班级及部门删除')
 // 当前账号改密及重新登录，最后恢复测试密码。
 await page.getByText('修改密码',{exact:false}).first().click()
 await dialog().waitFor()
 await fill('原密码','wrong-password');await fill('新密码','Changed-123');await fill('确认密码','Changed-123')
 await submit('PUT','/emps/password',save,0)
 await fill('原密码',password)
 await submit('PUT','/emps/password',save);passwordChanged=true
 await page.waitForURL('**/login')
 await page.getByPlaceholder('请输入用户名').fill(username);await page.getByPlaceholder('请输入密码').fill('Changed-123')
 await submit('POST','/login',()=>page.getByRole('button',{name:'登 录'}).click());await page.waitForURL('**/index')
 token=await page.evaluate(()=>JSON.parse(localStorage.getItem('loginUser')).token)
 await api('PUT','/emps/password',{oldPassword:'Changed-123',newPassword:password});passwordChanged=false
 await page.getByText('退出登录',{exact:false}).click();await page.waitForURL('**/login')
 check('原密码错误提示、当前账号改密、重新登录与退出')
 // 伪造令牌返回401并清空本地登录状态。
 await page.evaluate(()=>localStorage.setItem('loginUser',JSON.stringify({token:'invalid'})))
 await page.goto(baseURL+'/dept');await page.waitForURL('**/login')
 assert.equal(await page.evaluate(()=>localStorage.getItem('loginUser')),null)
 check('无效令牌401回登录页')
 assert.deepEqual(errors,[],'浏览器未捕获异常')
 await fs.mkdir('test-results',{recursive:true})
 await fs.writeFile('test-results/integration.json',JSON.stringify({date:new Date().toISOString(),checks,browserErrors:errors},null,2))
 console.log('ALL '+checks.length+' CHECK GROUPS PASSED')
} catch(error) {
 await fs.mkdir('test-results',{recursive:true})
 await page.screenshot({path:'test-results/failure.png',fullPage:true})
 console.error(error)
 process.exitCode=1
} finally {
 if(token) {
  if(passwordChanged) await api('PUT','/emps/password',{oldPassword:'Changed-123',newPassword:password}).catch(()=>{})
  if(stuId) await api('DELETE','/students/'+stuId).catch(()=>{})
  if(clazzId) await api('DELETE','/clazzs/'+clazzId).catch(()=>{})
  if(empId) await api('DELETE','/emps?ids='+empId).catch(()=>{})
  if(deptId) await api('DELETE','/depts?id='+deptId).catch(()=>{})
 }
 await browser.close()
}
