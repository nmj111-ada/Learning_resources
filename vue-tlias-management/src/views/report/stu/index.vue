<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { queryStudentCountDataApi, queryStudentDegreeDataApi } from '@/views/api/report'
const firstChart = ref()
const secondChart = ref()
const loading = ref(false)
let charts = []
let disposed = false
// 窗口变化时调整图表尺寸，离开页面时销毁实例。
const resize = () => charts.forEach(chart => chart.resize())
onBeforeUnmount(() => { disposed=true; window.removeEventListener('resize',resize); charts.forEach(chart=>chart.dispose()) })
// 页面加载后请求真实统计数据。
onMounted(async () => {
  loading.value=true
  try {
    const [first,second] = await Promise.all([queryStudentCountDataApi(),queryStudentDegreeDataApi()])
    if(disposed) return
    if(first.code!==1 || second.code!==1) return ElMessage.error(first.msg || second.msg || '统计查询失败')
    charts=[echarts.init(firstChart.value),echarts.init(secondChart.value)]
    const data=first.data || []
    const names=Array.isArray(data)?data.map(item=>item.name):(data.jobList || data.clazzList || [])
    const values=Array.isArray(data)?data.map(item=>item.value):(data.dataList || [])
    charts[0].setOption({title:{text:'班级人数统计'},tooltip:{trigger:'axis'},grid:{containLabel:true,left:30,right:20},xAxis:{type:'category',data:names,axisLabel:{interval:0,rotate:20}},yAxis:{type:'value',minInterval:1},series:[{type:'bar',data:values,barMaxWidth:55}]})
    charts[1].setOption({title:{text:'学员学历分布'},tooltip:{trigger:'item'},legend:{bottom:0},series:[{type:'pie',radius:['35%','65%'],data:second.data || [],label:{formatter:'{b}: {c} ({d}%)'}}]})
    window.addEventListener('resize',resize)
  } catch { /* 请求错误由拦截器提示。 */ } finally { loading.value=false }
})
</script>
<template>
  <h1>学员信息统计</h1>
  <div v-loading="loading" class="charts">
    <div ref="firstChart" class="chart"></div>
    <div ref="secondChart" class="chart"></div>
  </div>
</template>
<style scoped>
.charts { display:flex; flex-wrap:wrap; gap:24px; margin-top:24px; }
.chart { flex:1; min-width:350px; height:440px; }
</style>
