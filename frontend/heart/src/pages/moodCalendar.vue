<script setup>
import { ref } from 'vue'
import { onLoad } from 'vue'

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

const loadCalendarData = async (monthVal) => {
  const res = await getCalendarMoodApi(monthVal)
  if(res.data.code === 200) {
    moodList.value = res.data.data
    console.log("日历情绪数据：", moodList.value)
    if(moodList.value.length>0){
      console.log('日期', moodList.value[0].date)
      console.log('主要情绪', moodList.value[0].mainMood)
      console.log('平均分', moodList.value[0].avgScore)
    }
  }else{
    uni.showToast({title: res.data.message, icon:'none'})
  }
}

onLoad(()=>{
  loadCalendarData(8)
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
      <div class="jzy">
        <div class="rb-title">
          情绪日历
          <div class="title-2">
            用色彩看见一整月的情绪轨迹
          </div>
        </div>
        <div class="parts">
          <div class="p-1">
            <div class="p-1-title">
              <div>日</div>
              <div>一</div>
              <div>二</div>
              <div>三</div>
              <div>四</div>
              <div>五</div>
            </div>
            <div class="p-con">
              <div>1</div>
              <div>2</div>
              <div>3</div>
              <div>4</div>
              <div>5</div>
              <div>6</div>
              <div>7</div>
              <div>8</div>
              <div>9</div>
              <div>10</div>
              <div>11</div>
              <div>12</div>
              <div>13</div>
              <div>14</div>
              <div>15</div>
              <div>16</div>
              <div>17</div>
              <div>18</div>
              <div>19</div>
              <div>20</div>
              <div>21</div>
              <div>22</div>
              <div>23</div>
              <div>24</div>
              <div>25</div>
              <div>26</div>
              <div>27</div>
              <div>28</div>
              <div>29</div>
              <div>30</div>
            </div>
          </div>
      </div>
    </div>
    <div class="p-r">
            <div class="p-r-1">
              <div class="p-2-b"></div>
              <div class="p-2">
                <h2>当日心情预览</h2>
                <div class="date">2026年6月30日</div>
                <div class="tags">
                  <span>开心</span>
                  <span>开心</span>
                  <span>长途旅行</span>
                </div>
                <h3>日记预览</h3>
                <div class="p-line"></div>
                <p>去到了想去的地方，感受风和自由，感觉很放松……</p>
                <div class="btn">查看详情</div>
              </div>
            </div>
            <div class="p-3">
              <h2>情绪颜色对照表</h2>
              <div class="box">
                <div class="b-l">
                  <div class="b">
                    <p></p>
                    <span>开心</span>
                  </div>
                  <div class="b">
                    <p></p>
                    <span>悲伤</span>
                  </div>
                  <div class="b">
                    <p></p>
                    <span>生气</span>
                  </div>
                  <div class="b">
                    <p></p>
                    <span>温暖</span>
                  </div>
                </div>
            </div>
          </div>
        </div>
      
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
  height: 924px;
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
.rb-title {
    font-weight: 700;
    font-size: 36px;
    margin-top: 40px;
    margin-left: 36px;
    margin-bottom: 20px;
  }
  .title-2 {
    font-weight: 700;
    font-size: 22px;
  }
  .parts {
    display: flex;
    height: 717px;
  }
  .p-1 {
    width: 781px;
    height: 717px;
    background-color: white;
    border-radius: 30px;
    margin-left: 42px;
    box-shadow: 0px 4px 10px rgba(0,0,0,0.25);
  }
  .p-1-title {
    display: flex;
    justify-content: space-between;
    margin-left: 58px;
    margin-right: 58px;
    margin-top: 14px;
  }
.p-r-1 {
    position: absolute;
    top: 250px;
    width: 303px;
    height: 437px;
  }

  .p-2 {
    position: absolute;
    left: 835px;
    top: 15px;
    width: 303px;
    height: 437px;
    border-radius: 30px;
    background: #ffffff;
    box-shadow: 0px 4px 10px rgba(0,0,0,0.08);
    z-index: 2;
    padding: 30px;
    box-sizing: border-box;
  }
  .p-2-b {
    position: absolute;
    left: 840px;
    top: 10px;
    width: 303px;
    height: 437px;
    border-radius: 30px;
    background: #e9e9eb;
    box-shadow: 0px 4px 10px rgba(0,0,0,0.08);
    transform: translate(7px,7px);
    z-index: 1;
  }
  .p-2>h2 {
    position: absolute;
    left: 35px;
    top: 22px;
    margin: 0;
    font-weight: 700;
    font-size: 24px;
  }
  .p-2>.date {
    position: absolute;
    color: #3D3D3D;
    left: 168px;
    top: 67px;
    font-weight: 400;
    font-size: 18px;
  }
 .tags>:nth-child(1) {
    position: absolute;
    left: 35px;
    top: 81px;
    width: 76px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    background: rgba(221, 212, 234, 0.2);
    border-radius: 30px;
 }
 .tags>:nth-child(1):hover {
  background-color: rgb(221, 212, 234);
 }
 .tags>:nth-child(2):hover {
  background-color: rgb(221, 212, 234);
 }
 .tags>:nth-child(3):hover {
  background-color: rgb(221, 212, 234);
 }
.tags>:nth-child(2) {
  position: absolute;
  left: 35px;
  top: 126px;
  width: 76px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background: rgba(221, 212, 234, 0.2);
  border-radius: 30px;
}
.tags>:nth-child(3) {
  position: absolute;
  left: 147px;
  top: 126px;
  width: 122px;
  height: 37px;
  line-height: 36px;
  text-align: center;
  background: rgba(221, 212, 234, 0.2);
  border-radius: 30px;
}
.p-2>h3 {
  color: #3D3D3D;
  position: absolute;
  left: 35px;
  top: 190px;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
}
.p-2>p {
  position: absolute;
  left: 35px;
  top: 239px;
  right: 39px;
  color: #3D3D3D;
  font-size: 22px;
  font-weight: 400;
  margin: 0;
}
.p-line {
  position: absolute;
  left: 34px;
  top: 349.5px;
  width: 224.5px;
  height: 0px;
  border: 1px solid rgba(163, 136, 255, 0.41);
}
.btn {
  position: absolute;
  left: 159px;
  top: 365px;
  border-radius: 30px;
  color: #AD7CF3;
  width: 116px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  background-color: #F2F1FF;
  font-size: 20px;
  font-weight: 400;
}
.btn:hover {
  background-color: #AD7CF3;
  color: #F2F1FF;
}
.p-3 {
  position: absolute;
  left: 1100px;
  top: 700px;
  width: 308px;
  height: 253px;
  border-radius: 30px;
  background: #FFFFFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}
.p-3>h2 {
  position: absolute;
  left: 25px;
}
.box {
  position: absolute;
  left: 25px;
  top: 61px;
  display: flex;
}
.b-r {
  margin-left: 38px;
}
.b {
  display: flex;
  margin-bottom: 15px;
}
.b>span{
  width: 63px;
  height: 29px;
  line-height: 29px;
  font-size: 20px;
  font-weight: 400;
  margin-left: 15px;
}
.b>p {
  width: 15px;
  height: 15px;
  border-radius: 50px;
  margin: 0;
  margin-top: 7px;
}
.b-l>:nth-child(1)>p {
  background-color: #FFB86C;
}
.b-l>:nth-child(2)>p {
  background-color: #8FB8E5;
}
.b-l>:nth-child(3)>p {
  background-color: #F7A7A7;
}
.b-l>:nth-child(4)>p {
  background-color: #F8C8DC;
}
.b-r>:nth-child(1)>p {
  background-color: #98D8C8;
}
.b-r>:nth-child(2)>p {
  background-color: #C3B1E1;
}
.b-r>:nth-child(3)>p {
  background-color: #BFB0A3;
}
.b-r>:nth-child(4)>p {
  background-color: #C8C8C8;
}
.p-con {
    display: flex;
    flex-wrap: wrap;
    margin-left: 25px;
  }
  .p-con>div {
    width: 91px;
    height: 108px;
    background: linear-gradient(#C3B1E1, #dcd2ef);
    margin-right: 14px;
    margin-top: 19px;
    border-radius: 10px;
    line-height: 108px;
    text-align: center;
    color: #AC8B69;
  }
</style>