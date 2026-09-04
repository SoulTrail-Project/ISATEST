import http from "../utils/request"
import {getToken} from "@/utils/token.js"

// 用户登录
export const loginAPI = (username, password) => {
    return uni.request({
        method: 'POST',
        timeout: 30000,
        url: '/user/login',
        data: {
            username,
            password
        },
        header: {
            'Content-Type': 'application/json'
        }
    })
}

// 更新日记接口
export const addDiaryApi = (params)  =>{
const token = getToken().token
  return uni.request({
    url:'/api/diaries',
    method:'POST',
    data:params,
    header: {
        "Authorization":`Bearer ${token}`,
        'Content-Type': 'application/json'
    }
  })
}

