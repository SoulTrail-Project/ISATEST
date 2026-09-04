<script setup>
import { ref } from 'vue'
import MixChart from "@/components/MixChart.vue";
import  MixLine  from "@/components/MixLine.vue";
import MixBoon from '../components/MixBoon.vue';
import { getMoodTrendApi, getEmotionDistributionApi } from '@/api/calendar'


// 导航菜单
const menuList = ref([
  {
    id:1,
    name:'首页',
    icon:'home',
    active:false,
    page:'/pages/todayMood',
    iconSrc:'../static/home1.svg',
    activeIconSrc:'../static/home2.svg'
  },
  {
    id:2,
    name:'日记',
    icon:'diary',
    active:false,
    page:'/pages/myDiary',
    iconSrc:'../static/diary1.svg',
    activeIconSrc:'../static/diary2.svg'
  },
  {
    id:3,
    name:'数据图表',
    icon:'chart',
    active:true,
    page:'/pages/moodData',
    iconSrc:'../static/data1.svg',
    activeIconSrc:'../static/data2.svg'
  },
  {
    id:4,
    name:'日历',
    icon:'calendar',
    active:false,
    page:'/pages/moodCalendar',
    iconSrc:'../static/calendar1.svg',
    activeIconSrc:'../static/calendar2.svg'
  },
  {
    id:5,
    name:'我的',
    icon:'user',
    active:false,
    page:'/pages/personal',
    iconSrc:'../static/person1.svg',
    activeIconSrc:'../static/person2.svg'
  }
])

// 点击菜单
const selectMenu = (menu)=>{
  // 全部取消激活
  menuList.value.forEach(item=>{
    item.active = false
  })
  menu.active = true

  uni.navigateTo({
    url: menu.page
  })
}



// 切换标签
const toggleTag = (tag) => {
  tag.active = !tag.active
}

// 调用饼图接口
const loadPie = async () => {
  const [err, res] = await getEmotionDistributionApi()
  if(!err && res.data.code === 200){
    pieData.value = res.data.data
  }
}

// 调用折线图接口
const loadTrend = async () => {
  const [err, res] = await getMoodTrendApi(7)
  if(!err && res.data.code === 200){
    trendData.value = res.data.data
  }
}

onMounted(()=>{
  loadPie()
  loadTrend()
})

</script>

<template>
  <view class="background">
    <!-- 左边导航栏 -->
    <view class="left">
      <view class="line1">
        <view class="circle"></view>
        <view class="logo">
          <text class="logo-text">心灵轨迹</text>
        </view>
      </view>

      <!-- 菜单列表 -->
      <view class="menu-list">
        <view
          v-for="menu in menuList"
          :key="menu.id"
          class="menu-item"
          :class="{ active: menu.active }"
          @click="selectMenu(menu)"
        >
          <view class="menu-icon">
            <image
              :src="menu.active ? menu.activeIconSrc : menu.iconSrc"
              mode="scaleToFill"
            />
          </view>
          <text class="menu-text">{{ menu.name }}</text>
          <view v-if="menu.active" class="menu-indicator"></view>
        </view>
      </view>
    </view>

    <!-- 右边内容 -->
    <view class="right">
      <!-- 顶部导航栏 -->
      <view class="top-bar">
        <view class="search-box">
          <input class="search-input" placeholder="搜索内容" placeholder-class="search-placeholder" />
          <text class="search-icon">🔍</text>
        </view>
        <view class="top-actions">
          <view class="action-icon">
            <image
              src="../static/flower.svg"
              mode="scaleToFill"
            />
          </view>
          <view class="action-icon">
            <image
              src="../static/notice.svg"
              mode="scaleToFill"
            />
            <view class="badge-dot"></view>
          </view>
          <view class="user-info">
            <view class="user-avatar">
              <image
                src="../static/avatar.svg"
                mode="scaleToFill"
              />
            </view>
            <view class="user-detail">
              <text class="user-name">youyou</text>
              <text class="user-email">3346677@xx.com</text>
            </view>
            <text class="dropdown-arrow">▼</text>
          </view>
        </view>
      </view>
      <div class="crx">数据图表
        <div class="crx-li">近七天情绪趋势与心情分布</div>
      </div>
      <div class="nxl">
        <div class="nxl-one">
          <div class="cr">
            <MixLine/>
          </div></div>
        <div class="nxl-two">
          <div class="cr-a">
            <MixChart/>
          </div>
        </div>
        <div class="nxl-three">
          <div class="cr-b">
            <MixBoon/>
          </div>
        </div>
      </div>
      <div class="wf">
        <div class="card">
            <div class="card-head">
              <span class="dot"></span>
              <span class="card-title">本周总结</span>
            </div>
            <ul class="tyf">
              <li>本周整体情绪：偏安静</li>
              <li>最高情绪日：周三（开心）</li>
              <li>最低情绪日：周一（悲伤）</li>
              <li>最近情绪比较稳定，继续保持～</li>
            </ul>
          </div>
      </div>
       <view class="quote-card">
            <text class="quote-text">情绪没有好坏，每一种都值得被看见</text>
            <view class="quote-deco">🍃</view>
        </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.background {
  width: 100%;
  height: 100%;
  display: flex;
  background: #E8E2F5;
}

/* 左侧导航栏 */
.left {
  width: 256px;
  height: 920px;
  background-color: #F9F8FB;
  box-shadow: 0px 4px 10px 0px #B8A0D8;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;

  .line1 {
    padding-top: 24px;
    padding-left: 24px;
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 40px;

    .circle {
      width: 56px;
      height: 56px;
      border-radius: 50%;
      background: linear-gradient(135deg, #D8D8D8 0%, #E8E8E8 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;

      .avatar-icon {
        font-size: 28px;
      }
    }

    .logo {
      position: relative;

      .logo-text {
        font-size: 22px;
        font-weight: 600;
        color: #333;
        font-family: 'PingFang SC', sans-serif;
      }

      .logo-deco {
        position: absolute;
        top: -4px;
        right: -20px;
        width: 30px;
        height: 20px;
        background: linear-gradient(90deg, transparent 0%, #9B7ED9 50%, transparent 100%);
        opacity: 0.6;
        border-radius: 50%;
      }
    }
  }

  .menu-list {
    flex: 1;
    padding: 0 16px;

    .menu-item {
      display: flex;
      align-items: center;
      padding: 14px 16px;
      margin-bottom: 8px;
      border-radius: 12px;
      cursor: pointer;
      position: relative;
      transition: all 0.3s ease;
      image {
        width: 30px;
        height: 25px;
      }
      &:hover {
        background: rgba(155, 126, 217, 0.08);
      }
      &.active {
        background: rgba(155, 126, 217, 0.15);
        .menu-text {
          color: #7C5CBF;
          font-weight: 600;
        }
        .menu-indicator {
          opacity: 1;
        }
      }
      .menu-icon {
        font-size: 20px;
        margin-right: 14px;
        width: 24px;
        text-align: center;
      }

      .menu-text {
        font-size: 16px;
        color: #555;
        flex: 1;
        margin-left: 12px;
        margin-top: -6px;
      }

      .menu-indicator {
        position: absolute;
        left: -16px;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 28px;
        background: #7C5CBF;
        border-radius: 0 4px 4px 0;
        opacity: 0;
        transition: opacity 0.3s ease;
      }
    }
  }
}

/* 右侧内容区 */
.right {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶部导航栏 */
.top-bar {
  height: 70px;
  background: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  box-shadow: 0 2px 8px rgba(155, 126, 217, 0.15);
  flex-shrink: 0;

  .search-box {
    width: 360px;
    height: 40px;
    background: #F6F5FA;
    border-radius: 20px;
    display: flex;
    align-items: center;
    padding: 0 16px;

    .search-icon {
      font-size: 16px;
      margin-right: 10px;
      opacity: 0.6;
    }

    .search-input {
      flex: 1;
      height: 100%;
      background: transparent;
      border: none;
      outline: none;
      font-size: 14px;
      color: #333;
    }

    .search-placeholder {
      color: #999;
    }
  }

  .top-actions {
    display: flex;
    align-items: center;
    gap: 24px;

    .action-icon {
      font-size: 22px;
      cursor: pointer;
      position: relative;
      image {
        width: 38px;
        height: 38px;
      }

      .badge-dot {
        position: absolute;
        top: 2px;
        right: 0;
        width: 8px;
        height: 8px;
        background: #FF6B6B;
        border-radius: 50%;
        border: 2px solid #fff;
      }
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      padding: 6px 12px;
      border-radius: 12px;
      transition: background 0.3s ease;

      &:hover {
        background: #F5F3FA;
      }

      .user-avatar {
        border-radius: 50%;
        background: linear-gradient(135deg, #7C5CBF 0%, #9B7ED9 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        image {
          width: 55px;
          height: 55px;
          margin-top: -5px;
        }
      }

      .user-detail {
        display: flex;
        flex-direction: column;

        .user-name {
          font-size: 22px;
          font-weight: 500;
          color: #3D3D3D;
          margin-top: -16px;
        }

        .user-email {
          font-size: 18px;
          color: #3D3D3D;
        }
      }

      .dropdown-arrow {
        font-size: 10px;
        color: #999;
        margin-left: 4px;
      }
    }
  }
}
.crx {
  font-weight: 700;
    font-size: 32px;
    margin-top: 15px;
    margin-left: 36px;
    margin-bottom: 20px;
}
.crx-li {
   font-weight: 700;
    font-size: 20px;
}
.nxl-one {
  position: absolute;
  left:290px;
  top: 170px;
  width: 542px;
  height: 232px;
  padding: 10px 25px 20px;
  border-radius: 24.07px;
  background-color: #F6F5FA;
}
.nxl-two {
  position: absolute;
  left: 952px;
  top: 168px;
   width: 352px;
  height: 232px;
  padding: 10px 25px 20px;
  border-radius: 24.07px;
   background-color: #F6F5FA;
   overflow:hidden;
}
.nxl-three {
  position: absolute;
  left: 284px;
  top:465px;
  width: 1050px;
  height: 160px;
  padding: 10px 15px 20px;
  border-radius: 24.07px;
   background-color: #F6F5FA;
   overflow:hidden;
}
.card-head {
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  left: 20px;
  top: 10px;
}
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #7b5ce0;
}
.card-title {
  font-weight: 700;
  color: #333;
  font-size: 20px;
}
.card {
  position: absolute;
  left: 280px;
  top:680px;
  width: 540px;
  height: 232px;
  border-radius: 24.07px;
  background: #FFFFFF;
}
.tyf {
  line-height: 2.0;
  padding: 30px 35px 20px;
}
.quote-card {
  background: linear-gradient(135deg, #A78BFA 0%, #8B5CF6 100%);
  border-radius: 20px;
  padding: 24px;
  height: 105px;
  width: 278px;
  position: relative;
  overflow: hidden;
  margin-top: auto;

  .quote-text {
    font-size: 15px;
    color: #fff;
    line-height: 1.6;
    font-weight: 500;
    position: relative;
    z-index: 1;
  }

  .quote-deco {
    position: absolute;
    right: 12px;
    bottom: 8px;
    font-size: 48px;
    opacity: 0.3;
  }
}
</style>
