// utils/token.js

// Token 存储的键名
const TOKEN_KEY = 'token'

// 存储 Token
export const setToken = (token) => {
  uni.setStorageSync(TOKEN_KEY, token)
  console.log('Token 已存储')
}

// 获取 Token
// export const getToken = () => {
//   return uni.getStorageSync(TOKEN_KEY)
// }
//utils/token.js
export function getToken(){
  const storageVal = uni.getStorageSync('token')
  //兜底：如果存的是对象，取出里面的token字段，否则直接返回
  if(storageVal && typeof storageVal === 'object' && storageVal.token){
    return storageVal.token
  }
  return storageVal || ''
}

// 移除 Token
export const removeToken = () => {
  uni.removeStorageSync(TOKEN_KEY)
  console.log('Token 已移除')
}

// 检查是否有 Token
export const hasToken = () => {
  return !!getToken()
}

