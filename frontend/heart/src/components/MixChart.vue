<template>
  <div class="zjx">
    <!-- 独立饼图容器 -->
    <div ref="pieRef" style="width:100%;height:232px;margin-bottom:24px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

// 三个ref，两个图表容器
const pieRef = ref(null)

// 保存图表实例
let pieChart = null

// 【1】单独饼图
function initPie() {
  pieChart = echarts.init(pieRef.value)
  const pieOption = {
    title: { text: '情绪占比', left: 'center',top:12, },
    tooltip: { trigger: 'item' },
      legend: { orient:'vertical', right:8, top:'center', itemWidth:14,itemHeight:14 },
      color: [
        '#FFB86C',
        '#98D8C8',
        '#8FB8E5',
        '#C3B1E1',
    ],
    series: [
      {
        type: 'pie',
        radius: ['0%', '65%'],
        center: ['50%', '60%'],
        label: {
             show:true,
            distance:14, 
            fontSize:12
        },
      labelLine:{
        length:6,    // 第一段引线缩短
        length2:10   // 第二段引线缩短
        },
        data: [
          { name: '开心', value: 40 },
          { name: '平静', value: 30 },
          { name: '悲伤', value: 18 },
          { name: '焦虑', value: 12 }
        ]
      }
    ]
  }
  pieChart.setOption(pieOption)
}



onMounted(()=>{
  initPie()
  initMixChart()
  window.addEventListener('resize', ()=>{
    pieChart?.resize()
  })
})

// 销毁实例，防止内存泄漏
onUnmounted(()=>{
  pieChart?.dispose()
})
</script>

<style scoped>
.zjx {
    width: 100%;
}
</style>