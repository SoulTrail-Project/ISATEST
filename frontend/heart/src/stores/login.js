import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    loginName: '',        // 这是啥？？
    loginPassword: '',    // 登录密码（注意：实际项目不建议存明文密码）
    loginTime: '',        // 注册时间
    // 可以再来一个头像
  }),
  
  actions: {
    // 设置登录信息
    setLoginInfo(loginData) {
      this.loginName = loginData.loginName || ''
      this.loginPassword = loginData.loginPassword || ''
      this.loginTime = loginData.loginTime || new Date().toLocaleString()
    },
    
    // 新增：清除登录信息（退出登录时用）
    clearLoginInfo() {
      this.loginName = ''
      this.loginPassword = ''
      this.loginTime = ''
    },
  },
  
  // 关键：开启持久化
  persist: {
    enabled: true,
    storage: {
      getItem(key) {
        return uni.getStorageSync(key)
      },
      setItem(key, value) {
        uni.setStorageSync(key, value)
      }
    },
    // 指定需要持久化的字段
    paths: [
      'loginName',        // 持久化登录名
      'loginPassword',    // 持久化密码
      'loginTime',        // 持久化登录时间
    ]
  }
})