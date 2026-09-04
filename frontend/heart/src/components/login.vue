<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { loginAPI } from '@/api/login.js'
import { useUserStore } from '../stores/login'

//接收父组件弹窗状态
const props = defineProps({
  show: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['update:show','close'])

const handleLoginSuccess = () => {
  emit('update:show', false)
}

const agreed = ref(false) //协议勾选状态
const canLogin = computed(()=>{
  //账号、密码不为空，并且勾选协议
  return username.value && password.value && agreed.value
})


const activeTab = ref('sms')
const useStore = useUserStore()
const username = ref('')
const password = ref('')

const handleLogin = async () => {
  if (!username.value || !password.value) {
    uni.showToast({
      title: '请输入完整账号密码',
      icon: 'none'
    });
    return;
  }
  try {
    const response = await loginAPI(username.value, password.value);
    console.log('API返回:', response);
    console.log('token:', response.data.data)
    if(response.data.code === 200) {
      console.log('登录成功');
      const token = response.data.data.token;
      uni.setStorageSync('token', token);
      // 读取校验
      const testSave = uni.getStorageSync('token')
      console.log("存入后读取校验：",testSave,typeof testSave)
      handleLoginSuccess()
    }else{
      uni.showToast({title:'账号或密码错误',icon:'none'})
    }
  } catch (error) {
    console.error('登录错误：', error);
    uni.showToast({title:'登录请求失败',icon:'none'})
  }
}

const handleClose = () => {
  emit('close')
}
</script>


<template>
  <view v-if="show" class="login-modal">
    <view class="modal-mask" @click="handleClose"></view>
    <view class="modal-content">
      <!-- 顶部 tab -->
      <view class="login-tabs">
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'sms' }"
          @click="activeTab = 'sms'"
        >
          <text>短信登录</text>
        </view>
        <view class="tab-divider"></view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'password' }"
          @click="activeTab = 'password'"
        >
          <text>用户注册</text>
        </view>
      </view>

      <!-- 短信登录 -->
      <view v-if="activeTab === 'sms'" class="login-form">
        <!-- 手机号输入 -->
        <view class="input-row phone-row">
          <view class="area-code">
            <text class="code-text">+86</text>
            <text class="code-arrow">▼</text>
          </view>
          <view class="input-divider"></view>
          <input 
            class="input-field" 
            type="number" 
            v-model="phone" 
            placeholder="请输入手机号" 
            placeholder-class="input-placeholder"
            maxlength="11"
          />
        </view>

        <!-- 验证码输入 -->
        <view class="input-row code-row">
          <input 
            class="input-field code-input" 
            type="number" 
            v-model="code" 
            placeholder="请输入验证码" 
            placeholder-class="input-placeholder"
            maxlength="6"
          />
          <view 
            class="get-code-btn" 
            :class="{ disabled: counting }"
            @click="handleGetCode"
          >
            <text>{{ counting ? `${countdown}s后重发` : '获取验证码' }}</text>
          </view>
        </view>
      </view>

      <!-- 密码登录 -->
      <view v-else class="login-form">
        <view class="input-row">
          <input 
            class="input-field" 
            v-model="username" 
            placeholder="请输入账号/手机号" 
            placeholder-class="input-placeholder"
          />
        </view>
        <view class="input-row">
          <input 
            class="input-field" 
            :password="!showPwd" 
            v-model="password" 
            placeholder="请输入密码" 
            placeholder-class="input-placeholder"
          />
          <view class="eye-btn" @click="showPwd = !showPwd">
            <text>{{ showPwd ? '👁' : '👁‍🗨' }}</text>
          </view>
        </view>
      </view>

      <!-- 登录按钮 -->
      <view 
        class="login-btn" 
        :class="{ disabled: !canLogin }"
        @click="handleLogin"
      >
        <text class="login-text">登录</text>
      </view>

      <!-- 协议 -->
      <view class="agreement-row" @click="agreed = !agreed">
        <view class="agreement-checkbox" :class="{ checked: agreed }">
          <text v-if="agreed">✓</text>
        </view>
        <text class="agreement-text">
          您已阅读并同意
          <text class="agreement-link">《心灵轨迹社区用户服务协议》</text>
          <text class="agreement-link">《隐私政策》</text>
          <text class="agreement-link">《软件许可协议》</text>
        </text>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.login-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}

.modal-content {
  position: relative;
  width: 600px;
  background: #FFFFFF;
  border-radius: 28px;
  padding: 40px 50px 30px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  z-index: 1;
}

/* 顶部 tab */
.login-tabs {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 36px;

  .tab-item {
    flex: 1;
    text-align: center;
    font-size: 26px;
    color: #BBB;
    cursor: pointer;
    transition: color 0.3s ease;
    font-weight: 500;

    &.active {
      color: #333;
      font-weight: 700;
    }
  }

  .tab-divider {
    width: 1px;
    height: 28px;
    background: #E0E0E0;
  }
}

/* 表单 */
.login-form {
  margin-bottom: 30px;
}

.input-row {
  display: flex;
  align-items: center;
  height: 56px;
  background: #F6F5FA;
  border-radius: 28px;
  padding: 0 24px;
  margin-bottom: 24px;

  &:last-child {
    margin-bottom: 0;
  }

  .input-field {
    flex: 1;
    height: 100%;
    background: transparent;
    border: none;
    outline: none;
    font-size: 17px;
    color: #333;
  }

  .input-placeholder {
    color: #BBB;
  }
}

/* 手机号行 */
.phone-row {
  .area-code {
    display: flex;
    align-items: center;
    gap: 8px;
    padding-right: 16px;

    .code-text {
      font-size: 18px;
      color: #333;
      font-weight: 500;
    }

    .code-arrow {
      font-size: 10px;
      color: #999;
    }
  }

  .input-divider {
    width: 1px;
    height: 24px;
    background: #DDD;
    margin-right: 16px;
  }
}

/* 验证码行 */
.code-row {
  .code-input {
    flex: 1;
  }

  .get-code-btn {
    flex-shrink: 0;
    font-size: 17px;
    color: #333;
    font-weight: 500;
    cursor: pointer;
    padding-left: 16px;

    &.disabled {
      color: #999;
      cursor: not-allowed;
    }
  }
}

/* 眼睛按钮 */
.eye-btn {
  font-size: 18px;
  cursor: pointer;
  padding-left: 12px;
  opacity: 0.6;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 58px;
  background: linear-gradient(135deg, #8B6BFF 0%, #7B5AFF 100%);
  border-radius: 29px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;

  &.disabled {
    background: #D0C8F0;
    cursor: not-allowed;
  }

  &:not(.disabled):hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(139, 107, 255, 0.4);
  }

  .login-text {
    font-size: 20px;
    font-weight: 600;
    color: #fff;
    letter-spacing: 4px;
  }
}

/* 协议 */
.agreement-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  cursor: pointer;

  .agreement-checkbox {
    width: 16px;
    height: 16px;
    border: 1.5px solid #CCC;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    margin-top: 2px;
    font-size: 10px;
    color: #fff;
    transition: all 0.2s ease;

    &.checked {
      background: #8B6BFF;
      border-color: #8B6BFF;
    }
  }

  .agreement-text {
    font-size: 13px;
    color: #999;
    line-height: 1.6;
  }

  .agreement-link {
    color: #8B6BFF;
  }
}
</style>