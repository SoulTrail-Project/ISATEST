<template>
  <div class="bar-wrap">
    <div ref="barRef" style="width:100%;height:200px;"></div>
  </div>
</template>

<script setup>
import {ref,onMounted,onUnmounted} from 'vue'
import * as echarts from 'echarts'

const barRef = ref(null)
let chartIns = null


const xData = ['','3','1','5','1','2','0','1','1','0','2','2','1','3']
const yData = [0,3,1,5,1,2,0,1,1,0,2,2,1,3]

function initChart(){
  chartIns = echarts.init(barRef.value)
  const option = {
    title:{
      text:'高频状态',
      left:10,
      top:3,
      textStyle:{
        fontSize:24,
        fontWeight:'bold'
      }
    },
    tooltip:{
      trigger:'axis'
    },
    grid:{
      left:'3%',
      right:'3%',
      bottom:'15%',
      top:'20%',
      containLabel:true
    },
    xAxis:{
      type:'category',
      data:xData,
      axisLine:{show:false},
      axisTick:{show:false},
      splitLine:{show:false},
    },
    yAxis:{
      type:'value',
      max:6,
      axisLine:{show:false},
      axisTick:{show:false},
      splitLine:{show:false},
    },
    series:[
      {
        type:'bar',
        data:yData,
        barWidth:'35%',
        itemStyle:{
          color:'#9b80e8',
          borderRadius:[8,8,8,8] 
        },
        label:{
          show:true,
          position:'bottom',
          fontSize:16
        }
      }
    ]
  }
  chartIns.setOption(option)
}

onMounted(()=>{
  initChart()
  window.addEventListener('resize',()=>chartIns?.resize())
})

onUnmounted(()=>{
  chartIns?.dispose()
})
</script>

<style scoped>
.bar-wrap{
  width:100%;
  height:280px;
}
</style>