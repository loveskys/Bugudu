import request from '@/utils/request'

//管理员登录
export function adminLogin(userInfo) {
  return request({
    url: '/admin/login',
    method: 'post',
    data: {
        userName: userInfo.username,
        passWord: userInfo.password
    }
  })
}

