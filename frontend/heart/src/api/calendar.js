import { getToken } from "@/utils/token.js"

//日历视图
export const getCalendarApi = (month) => {
    const token = getToken().token
    return uni.request({
        url: '/api/diaries/calendar',
        data: { month: month },
        header: {
            Accept: 'application/json',
            "Authorization": `Bearer ${token}`,
        },
        method: 'GET',
    })
}
//折线
export const getMoodTrendApi = (range) => {
    const token = getToken().token
    return uni.request({
        url: '/api/stats/trend ',
        data: { range: range },
        header: {
            Accept: 'application/json',
            "Authorization": `Bearer ${token}`,
        },
        method: 'GET',
    })
}

//饼图
export const getEmotionDistributionApi = (circle) => {
    const token = getToken().token
    return uni.request({
        url: ' /api/stats/emotion-distribution',
        data: { circle: circle },
        header: {
            Accept: 'application/json',
            "Authorization": `Bearer ${token}`,
        },
        method: 'GET',
    })
}