// 写请求实例封装，主要是拦截器和统一配置


// 添加拦截器
// 1.拦截request请求
// 2. 拦截 uploadFile 文件上传
// TODO：
// 1. 非http开头需拼接地址
// 2. 请求超时
// 3. 添加小程序请求头标识
// 4. 添加token请求头标识 

// import {useMemberStore} from '../stores/modules/member'
import { getToken, removeToken } from './token.js'

const baseURL = 'http://121.43.104.168:8080'



// 添加拦截器
const httpInterceptor = {
  // 拦截前触发
  invoke(options) {
    // 调试
    console.log('最终请求方法:', options.method) // 检查方法是否正确
    console.log('请求URL:', options.url)
    console.log('请求方法:', options.method) 
    console.log('请求头:', options.header)
    console.log('请求数据:', options.data)

      // 1. 非http开头需拼接地址
    if(!options.url.startsWith('http')){
      options.url = baseURL + options.url
    }
    // 2. 请求超时
    options.timeout = 30000
    // 判断是否为FormData
    const isFormData = options.data instanceof FormData
    console.log(options)
    // 3. 添加app端请求头标识
    // options.header = {
    //   'source-client': 'miniapp'
    // }
    // 添加 Token 到请求头
    const token = getToken()

    // 添加文档中要求的请求头
    if(token) {
      //拦截器内部
      options.header = options.header || {}
      const token = getToken()
      if(token){
        options.header.Authorization = `Bearer ${String(token)}`
      }
      // options.header = {
      // // 如果有,保留login.js中原有的header标识
      // ...(options.header || {}),  // 保留原有的 header
      // // 'Content-Type': 'application/x-www-form-urlencoded'
      // 'Authorization': token
      // //  'Authorization': `Bearer ${token}`  
      // }

      if (!isFormData) {
        // 只有在不是 FormData 的情况下才设置默认 Content-Type
        options.header = {
          ...options.header,
          'Content-Type': 'application/json'
        }
      }
    }
     // 5. 数据转换（只对非 FormData 处理）
    if (!isFormData && 
        options.header['Content-Type'] === 'application/x-www-form-urlencoded' && 
        options.data) {
        const params = new URLSearchParams()
        for (let key in options.data) {
            params.append(key, options.data[key])
        }
        options.data = params
    }
    
    console.log('最终请求配置:', options)

    // // 数据转换
    // if (options.header['Content-Type'] === 'application/x-www-form-urlencoded' && options.data) {
    //     const params = new URLSearchParams()
    //     for (let key in options.data) {
    //         params.append(key, options.data[key])
    //     }
    //     options.data = params
    // }
  }

}
uni.addInterceptor('request', httpInterceptor)
uni.addInterceptor('uploadFile', httpInterceptor)


// 请求函数
// 返回promise对象
// 请求成功，请求失败
export const http = (options) => {
  // 返回promise对象
  return new Promise((resolve,reject) => {
    // 提前获取
    const memberStore = useMemberStore()
    uni.request({
      // 拿到的promise应用起来
      ...options,
      // 响应 成功
      success(res) {
        // 状态码2xx,axios就是这样设计的
        if(res.statusCode >= 200 && res.statusCode < 300) {
          //提取核心数据
          resolve(res.data)
        }else if(res.statusCode === 401){
          // 401错误，清理用户信息，跳转到登录页
          // const memberStore = useMemberStore()
          memberStore.clearProfile()
          uni.navigateTo({ url: '/pages/login' })
          reject(res)
        }else {
          // 其他错误
          uni.showToast({
            icon: 'none',
            title: '用户名或密码不正确'
          })
          reject(res)
        }
      },
      // 响应失败
      fail(err) {
        uni.showToast({
          icon: 'none',
          title: '网络错误，换个网络试试'
        })
        reject(err)
      }
    })
  })
}

export default httpInterceptor