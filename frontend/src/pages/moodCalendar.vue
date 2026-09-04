<script setup>
import { ref } from 'vue'


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
    active:false,
    page:'/pages/moodData',
    iconSrc:'../static/data1.svg',
    activeIconSrc:'../static/data2.svg'
  },
  {
    id:4,
    name:'日历',
    icon:'calendar',
    active:true,
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

    
    </view>
  </view>
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


</style>