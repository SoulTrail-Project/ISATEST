<template>
    <div class="mzc">
        <div ref="mixRef" style="width:100%;height:240px;"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const mixRef = ref(null)

// 保存图表实例
let mixChart = null

function initMixChart() {
  mixChart = echarts.init(mixRef.value)
  const mixOption = {
    title: { text: '情绪频率', left: 'left', textStyle:{fontSize:22} },
    tooltip: { trigger: 'axis' },
    legend: {
      data: ['情绪频次','趋势'],
      bottom:12
    },
    xAxis: {
      type: 'category',
      data: ['开心','平静','悲伤','焦虑','生气','疲惫','温暖','迷茫']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name:'情绪频次',
        type:'bar', // 柱状图
        data:[4,7,2,3,1,2,2,3],
        itemStyle:{
          color:'#9b80e8' 
        }
      },
      {
        name:'趋势',
        type:'line',//折线图
        smooth:true, 
        areaStyle:{ //区域面积效果
          color:{
            type:'linear',
            x: 0,y:0,x2:0,y2:1,
            colorStops:[
              {offset:0,color:'rgba(155,128,232,0.5)'},
              {offset:1,color:'rgba(155,128,232,0.05)'}
            ]
          }
        },
        lineStyle:{
          color:'#9b80e8'
        },
        data:[5,7,4,5,4,4,5,7]
      }
    ]
  }
  mixChart.setOption(mixOption)
}

onMounted(()=>{
  initMixChart()
  window.addEventListener('resize', ()=>{
    mixChart?.resize()
  })
})

// 销毁实例，防止内存泄漏
onUnmounted(()=>{
  mixChart?.dispose()
})
</script>
<style scoped>
.mzc {
    width: 100%;
}
</style>
