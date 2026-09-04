<script setup>
import { ref } from 'vue'
import Login from '../components/login.vue'
import { addDiaryApi } from '@/api/login.js'


// 点击登录按钮：打开弹窗
const showLogin = ref(false)

const loginBtn = () => {
  showLogin.value = true
}
// 心情列表
const moodList = ref([
 { id: 1, name: '开心', typeCode:'happy', color: 'linear-gradient(180deg, #FFD28F 0%, #FFE8C8 100%)', textColor: '#FFB86C' },
 { id: 2, name: '平静', typeCode:'calm', color: 'linear-gradient(180deg, #A8E6CF 0%, #D4F5E9 100%)', textColor: '#98D8C8' },
 { id: 3, name: '悲伤', typeCode:'sad', color: 'linear-gradient(180deg, #A0C4E8 0%, #D4E4F7 100%)', textColor: '#8FB8E5' },
 { id: 4, name: '焦虑', typeCode:'anxious', color: 'linear-gradient(180deg, #C9B8E8 0%, #E8DEF5 100%)', textColor: '#C3B1E1' },
 { id: 5, name: '生气', typeCode:'angry', color: 'linear-gradient(180deg, #F5B8B8 0%, #FADDDD 100%)', textColor: '#F7A7A7' },
 { id: 6, name: '疲惫', typeCode:'tired', color: 'linear-gradient(180deg, #D4C8B8 0%, #E8DFD4 100%)', textColor: '#BFB0A3' },
])


// 标签列表
const tagList = ref([
  { id: 1, name: '开心', color: 'linear-gradient(180deg, #FFD28F 0%, #FFE8C8 100%)', textColor: '#FFB86C' },
  { id: 2, name: '平静', color: 'linear-gradient(180deg, #A8E6CF 0%, #D4F5E9 100%)', textColor: '#98D8C8' },
  { id: 3, name: '悲伤', color: 'linear-gradient(180deg, #A0C4E8 0%, #D4E4F7 100%)', textColor: '#8FB8E5' },
  { id: 4, name: '焦虑', color: 'linear-gradient(180deg, #C9B8E8 0%, #E8DEF5 100%)', textColor: '#C3B1E1' },
  { id: 5, name: '生气', color: 'linear-gradient(180deg, #F5B8B8 0%, #FADDDD 100%)', textColor: '#F7A7A7' },
  { id: 6, name: '疲惫', color: 'linear-gradient(180deg, #D4C8B8 0%, #E8DFD4 100%)', textColor: '#BFB0A3' },
  { id: 7, name: '温暖', color: 'linear-gradient(180deg, #F8C8D8 0%, #FCE4EC 100%)', textColor: '#F8C8DC' },
  { id: 8, name: '迷茫', color: 'linear-gradient(180deg, #C8C8C8 0%, #D4D4D4 100%)', textColor: '#F8C8DC' }
])


// 心情选择器横向滚动（每次滚一格 = 卡片宽度 + 间距）
const scrollMood = (direction) => {
  const selector = document.querySelector('.mood-selector')
  if (!selector) return
  const scrollAmount = 98 + 11  // 卡片宽度98 + gap 11 = 109px
  selector.scrollBy({
    left: direction === 'left' ? -scrollAmount : scrollAmount,
    behavior: 'smooth'
  })
}


// 当前选中的心情
const selectedMood = ref(null)
//首页or去年今日
const choose = ref(false)
// 点击返回切换主页
const turnBack = () => {
  choose.value = !choose.value
}
// 点击进入详情
const entoll = () => {
  choose.value = !choose.value
}

// 菜单数组，新增 page 属性填写页面路径
const menuList = ref([
  {
    id:1,
    name:'首页',
    icon:'home',
    active:true,
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
    active:false,
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


// 心情记录内容
const moodContent = ref('')

// 隐私保护开关
const privacyProtected = ref(false)

// 选择心情
const selectMood = (mood) => {
  selectedMood.value = mood
}

// 切换标签
const toggleTag = (tag) => {
  tag.active = !tag.active
}

//提交日记
const submitDiary = async ()=>{
  console.log(uni.getStorageSync('token').token)
  const params = {
    content:moodContent.value,
    moodType:selectedMood.value.typeCode,
    tags:tagList.value.filter(t => t.active).map(t => t.name)
  }
  try{
    console.log('上传的日内容',params);
    const res = await addDiaryApi(params)
    console.log('新增日记返回结果：',res)
    if(res.data.code === 200){
      uni.showToast({title:'保存成功'})
    }else{
      uni.showToast({title:res.data.message,icon:'none'})
    }
  }catch(err){
    uni.showToast({title:'保存失败',icon:'none'})
  }
}


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
    <view v-if="choose === true" class="right">
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
            <view class="user-detail" @click="loginBtn">
              <text class="user-name">youyou</text>
              <text class="user-email">3346677@xx.com</text>
            </view>
            <text class="dropdown-arrow">▼</text>
          </view>
        </view>
      </view>

      <!-- 主内容区 -->
      <view class="main-content">
        <!-- 左侧主卡片 -->
        <view class="content-left">
          <view class="page-header">
            <text class="page-title">今日心情</text>
            <text class="page-subtitle">选择此刻的状态</text>
          </view>

          <view class="mood-card">
            <view class="card-header">
              <view class="card-title-wrap">
                <view class="title-dot"></view>
                <text class="card-title">选择此刻的状态</text>
              </view>
              <text class="card-tip">今天的你，值得被温柔记录</text>
            </view>

          <!-- 心情选择 -->
          <view class="mood-selector-wrap">
            <view class="mood-selector">
              <view
                v-for="mood in moodList"
                :key="mood.id"
                class="mood-item"
                :class="{ selected: selectedMood?.id === mood.id }"
                :style="{ background: mood.color }"
                @click="selectMood(mood)"
              >
                <text class="mood-name" :style="{ color: mood.textColor }">{{ mood.name }}</text>
              </view>
            </view>
          </view>
            <!-- 右箭头，在容器外面 -->
            <view @click="scrollMood('right')" class="mood-scroll-btn">
              <image
                src="../static/rightArrow.svg"
                mode="scaleToFill"
              />
            </view>

            <!-- 心情输入框 -->
            <view class="mood-input-wrap">
              <textarea
                class="mood-input"
                v-model="moodContent"
                placeholder="想对自己说点什么？记录今天的心情、小事、感受……"
                placeholder-class="input-placeholder"
              />
            </view>

            <!-- 标签区域 -->
            <view class="tag-section">
              <view class="tag-header">
                <text class="tag-title">标记今天的状态</text>
                <view class="add-tag-btn">
                  <text class="add-icon">+</text>
                  <text class="add-text">新增标签</text>
                </view>
              </view>
              <view class="tag-list">
                <view
                  v-for="tag in tagList"
                  :key="tag.id"
                  class="tag-item"
                  :class="{ active: tag.active }"
                  @click="toggleTag(tag)"
                >
                  <text>{{ tag.name }}</text>
                </view>
              </view>
            </view>

            <!-- 隐私保护 -->
            <view class="privacy-section">
              <view class="privacy-check" @click="privacyProtected = !privacyProtected">
                <view class="checkbox" :class="{ checked: privacyProtected }">
                  <text v-if="privacyProtected">✓</text>
                </view>
                <text class="privacy-text">内容隐私保护</text>
              </view>
            </view>

            <!-- 保存按钮 -->
            <view class="save-btn" :class="{ enabled: selectedMood }" @click="submitDiary">
              <text class="save-text">保存今日记录</text>
            </view>
          </view>
        </view>

        <!-- 右侧边栏 -->
        <view class="content-right">
          <!-- 去年今天 -->
          <view class="sidebar-card last-year-card" @click="entoll">
            <view class="sidebar-card-header">
              <view class="title-dot purple"></view>
              <text class="sidebar-title">去年今天</text>
            </view>
            <view class="last-year-tags">
              <view class="year-tag light">治愈</view>
              <view class="year-tag light">开心</view>
              <view class="year-tag dark">朋友聚会</view>
            </view>
            <text class="last-year-content">今天和朋友见面，聊了很多开心的事，感觉整个人都放松下来了……</text>
            <view class="card-stack-layer layer1"></view>
            <view class="card-stack-layer layer2"></view>
          </view>

          <!-- 本周小回顾 -->
          <view class="sidebar-card week-review-card">
            <view class="sidebar-card-header">
              <view class="title-dot purple"></view>
              <text class="sidebar-title">本周小回顾</text>
            </view>
            <view class="review-item">
              <text class="review-label">本周记录：</text>
              <text class="review-value">4天</text>
            </view>
            <view class="review-item">
              <text class="review-label">出现最多情绪：</text>
              <text class="review-value">平静</text>
            </view>
            <view class="review-item">
              <text class="review-label">高频标签：</text>
              <text class="review-value">学习、压力</text>
            </view>
          </view>

          <!-- 底部标语 -->
          <view class="quote-card">
            <text class="quote-text">情绪没有好坏，每一种都值得被看见</text>
            <view class="quote-deco">🍃</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 去年今日 -->
     <view v-else class="right2">
      <!-- 标题 -->
      <view class="title2">
        <image
          src="../static/arrow.svg"
          mode="scaleToFill"
          @click="turnBack"
        />
        <view class="subtitle">去年今日·时光回顾</view>
      </view>
      <view class="sub">翻看前一年同一天的心情记录</view>

      <view style="display: flex;">
        <!-- 多层卡片 -->
        <view class="level1">
          <view class="level2"></view>
          <view class="level3"></view>
          <view class="level4">
            <!-- title -->
            <view class="one">
              <view class="dot"></view>
              <view class="name">去年今日记录总览</view>
            </view>
            <!-- tips -->
            <view class="tips">
              <view>治愈</view>
              <view>外出旅行</view>
              <view>2025年7月28日   8:30 a.m</view>
            </view>
            <!-- 内容 -->
            <view class="content">
              终于要开启期待已久的海边旅行啦，此刻满心都是抑制不住的激动与欢喜。心里早已迫不及待奔赴远方，去吹咸湿的海风、看辽阔无垠的大海，静静感受海浪翻涌、落日归海的浪漫，光是想象都满心雀跃！
            </view>
          </view>
        </view>

        <view>
          <!-- 小记 -->
          <view class="otherCard"></view>

          <!-- 继续探索 -->
          <view class="keepGoNo"></view>
        </view>

      </view>

     </view>
  </view>

  <Login v-model:show="showLogin"></Login>
</template>

<style lang="scss" scoped>
.background {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #E8E2F5;
}

/* 左侧导航栏 */
.left {
  width: 256px;
  height: 100%;
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

.right2 {
  padding: 20px 29px 40px 40px;
  .title2 {
    display: flex;
    image {
      width: 11.5px;
      height: 26px;
      margin-right: 12px;
      margin-top: 17px;
    }
    .subtitle {
      font-size: 36px;
      font-weight: bold;
      color: #3D3D3D;
    }

  }
  .sub {
    font-size: 22px;
    color: #3D3D3D;
    margin-left: 28px;
  }
  .level1 {
    position: relative;
    width: 300px;
    height: 615px;
    border: 2px solid #FFFFFF;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    background-color: #F8F4FF;
    border-radius: 30px;
    margin-top: 24px;
    margin-left: 60px;
  }
  .level2 {
    position: absolute;
    top: 25px;
    left: -25px;
    width: 350px;
    height: 550px;
    background: linear-gradient(180deg, #7fdcc2 0%, #5ecbad 50%, #95bce6 52%, #b5d7f9 100%);
    border-radius: 30px;
    border: 2px solid #FFFFFF;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  }
  .level3 {
    position: absolute;
    top: 220px;
    left: -50px;
    width: 400px;
    transform: rotate(180deg);
    height: 308px;
    border-radius: 30px;
    background: linear-gradient(180deg, #e2c0ed 0%, #9f88ff 50%, #a388ff69 52%, #a388ff00 100%);
    box-sizing: border-box;
    border: 2px solid #FFFFFF;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  }
  .level4 {
    position: absolute;
    top: 70px;
    left: -70px;
    width: 365px;
    height: 340px;
    background-color: #FFFFFF;
    border-radius: 30px;
    padding-top: 40px;
    padding: 35px 40px 30px 30px;
    .one {
      display: flex;
      margin-bottom: 15px;
      .dot {
        width: 19px;
        height: 19px;
        border-radius: 50%;
        background: #A388FF;
        margin-right: 18px;
        margin-top: 10px;
      }
      .name {
        font-size: 24px;
        font-weight: 700px;

      }
    }
    .tips {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
    .tips>view {
      width: auto;
      font-size: 20px;
      font-weight: 400;
      color: #3D3D3D;
      background-color: #F2F0FC;
      border-radius: 30px;
      padding: 10px 20px;
      display: block;
    }
    .content {
      margin-top: 15px;
      font-size: 20px;
      font-weight: 400;
      color: #3D3D3D;
    }
  }
  .otherCard {
    width: 565px;
    height: 294px;
    background-color: #FFFFFF;
    margin-left: 107px;
    margin-top: 20px;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    border-radius: 30px;
  }
  .keepGoNo {
    width: 565px;
    height: 294px;
    margin-left: 107px;
    margin-top: 30px;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    background-color: #E7E3F9;
    border-radius: 30px;
  }
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
/* 主内容区 */
.main-content {
  // height: 700px;
  flex: 1;
  display: flex;
  padding: 20px 29px 40px 40px;
  gap: 20px;
  overflow-y: auto;
}
// .content-left {
//   flex: 1;
//   display: flex;
//   flex-direction: column;
// }
.content-right {
  margin-top: 78px;
  width: 256px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 38px;
}

/* 页面标题 */
.page-header {
  margin-bottom: 12px;
  margin-left: 2px;

  .page-title {
    font-size: 28px;
    font-weight: 700;
    color: #333;
    display: block;
  }

  .page-subtitle {
    font-size: 16px;
    color: #666;
  }
}

/* 心情卡片 */
.mood-card {
  width: 737px;
  height: 547px;
  background: #FFFFFF;
  border-radius: 30px;
  padding: 15px 15px 33px 23px;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;

  .card-title-wrap {
    display: flex;
    align-items: center;
    gap: 15px;
    // margin-bottom: 23px;

    .title-dot {
      width: 14px;
      height: 14px;
      background: #9B7ED9;
      border-radius: 50%;
    }

    .card-title {
      font-size: 24px;
      font-weight: 700;
      color: #3D3D3D;
    }
  }

  .card-tip {
    font-size: 14px;
    color: #999;
  }
}

/* 心情选择器 */
/* 外层包裹：定位参照 */
.mood-selector-wrap {
  width: 100%;
  position: relative;
  // margin-bottom: 20px;
  display: flex;
}
.mood-selector {
  width: 100%;
  height: 155px;                  // 【关键改动】145px → 180px，比卡片高35px，留出位移+指示器+阴影空间
  display: flex;
  gap: 11px;
  // margin-bottom: 20px;            // 【改动】32px → 20px，因为容器变高了
  position: relative;
  overflow-x: auto;
  overflow-y: hidden;
  padding-right: 50px;
  -webkit-overflow-scrolling: touch;
  
  &::-webkit-scrollbar {
    display: none;
  }
  scrollbar-width: none;
  .mood-item {
    width: 98px;
    height: 134px;                // 卡片高度保持145px不变
    flex-shrink: 0;
    border-radius: 24px;
    box-sizing: border-box;
    display: flex;
    align-items: flex-end;
    justify-content: center;
    padding-bottom: 15px;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    .mood-name {
      font-size: 22px;
      font-weight: 600;
    }
  }
}
.mood-scroll-btn {
  position: relative;
  top: -160px;
  left: 687px;
  width: 65px;
  height: 140px;
  display: block;
  background-color: #FFFFFF;
  z-index: 9;
  image {
    width: 29px;
    height: 29px;
    z-index: 999;
    margin-top: 55px;
    margin-left: 15px;
  }
}
/* 心情输入框 */
.mood-input-wrap {
  margin-bottom: 15px;
  margin-top: -140px;
  .mood-input {
    width: 732px;
    height: 130px;
    padding: 20px;
    background: #FFFFFF;
    border: 2px solid #A388FF;
    border-radius: 30px;
    font-size: 18px;
    color: rgba(61, 61, 61, 0.7);
    resize: none;
    outline: none;
    box-sizing: border-box;
    transition: border-color 0.3s ease;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    &:focus {
      border-color: #9B7ED9;
    }
  }

  .input-placeholder {
    color: rgba(61, 61, 61, 0.7);
  }
}

/* 标签区域 */
.tag-section {
  margin-bottom: 10px;
  .tag-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;

    .tag-title {
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }

    .add-tag-btn {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 4px 10px;
      border: 1.5px solid #9B7ED9;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(155, 126, 217, 0.1);
      }

      .add-icon {
        font-size: 14px;
        color: #9B7ED9;
        font-weight: bold;
      }

      .add-text {
        font-size: 13px;
        color: #9B7ED9;
      }
    }
  }

  .tag-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;

    .tag-item {
      padding: 6px 24px;
      background: #F5F3FA;
      border-radius: 20px;
      font-size: 15px;
      color: #666;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        background: #EDEAF5;
      }

      &.active {
        background: rgba(155, 126, 217, 0.2);
        color: #7C5CBF;
        font-weight: 500;
      }
    }
  }
}

/* 隐私保护 */
.privacy-section {
  margin-bottom: 20px;

  .privacy-check {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;

    .checkbox {
      width: 18px;
      height: 18px;
      border: 2px solid #CCC;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      font-size: 12px;
      color: #fff;

      &.checked {
        background: #9B7ED9;
        border-color: #9B7ED9;
      }
    }

    .privacy-text {
      font-size: 13px;
      color: #999;
    }
  }
}

/* 保存按钮 */
.save-btn {
  width: 100%;
  height: 52px;
  background: #E0D8F0;
  border-radius: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: not-allowed;
  transition: all 0.3s ease;

  &.enabled {
    background: linear-gradient(135deg, #9B7ED9 0%, #7C5CBF 100%);
    cursor: pointer;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 16px rgba(155, 126, 217, 0.4);
    }
  }

  .save-text {
    font-size: 17px;
    font-weight: 600;
    color: #fff;
    letter-spacing: 2px;
  }
}

/* 侧边栏卡片通用 */
.sidebar-card {
  background: #fff;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 6px 16px rgba(155, 126, 217, 0.2);
  position: relative;
}

.sidebar-card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;

  .title-dot {
    width: 10px;
    height: 10px;
    border-radius: 50%;

    &.purple {
      background: #9B7ED9;
    }
  }

  .sidebar-title {
    font-size: 17px;
    font-weight: 600;
    color: #333;
  }
}

/* 去年今天卡片 */
.last-year-card {
  position: relative;
  height: 289px;

  .last-year-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 14px;

    .year-tag {
      padding: 4px 12px;
      border-radius: 14px;
      font-size: 13px;

      &.light {
        background: rgba(155, 126, 217, 0.15);
        color: #7C5CBF;
      }

      &.dark {
        background: #9B7ED9;
        color: #fff;
      }
    }
  }

  .last-year-content {
    font-size: 14px;
    color: #555;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .card-stack-layer {
    position: absolute;
    background: #fff;
    border-radius: 20px;
    box-shadow: 0 4px 12px rgba(155, 126, 217, 0.15);
    z-index: -1;

    &.layer1 {
      width: 96%;
      height: 100%;
      top: 6px;
      left: 2%;
    }

    &.layer2 {
      width: 92%;
      height: 100%;
      top: 12px;
      left: 4%;
    }
  }
}

/* 本周小回顾 */
.week-review-card {
  height: 228px;
  .review-item {
    margin-bottom: 12px;
    font-size: 14px;
  

    &:last-child {
      margin-bottom: 0;
    }

    .review-label {
      color: #666;
    }

    .review-value {
      color: #333;
      font-weight: 500;
    }
  }
}

/* 底部标语卡片 */
.quote-card {
  // background-image: url('../static/back1.svg');
  background: linear-gradient(135deg, #A78BFA 0%, #8B5CF6 100%);
  border-radius: 20px;
  padding: 24px;
  height: 105px;
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