import { http } from "../utils/request";
import { getToken } from "../utils/token";

// 查看所有日记
export const checkAllDiaryApi = ()  =>{
const token = getToken().token
  return uni.request({
    url:'/api/diaries',
    method:'GET',
    header: {
        "Authorization":`Bearer ${token}`,
        'Content-Type': 'application/json'
    }
  })
}
// 查看单条日记
export const checkDiaryApi = ()  =>{
const token = getToken().token
  return uni.request({
    url:`/api/diaries/${id}`,
    method:'GET',
    header: {
        "Authorization":`Bearer ${token}`,
        'Content-Type': 'application/json'
    }
  })
}

