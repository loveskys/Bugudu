import request from '@/utils/request'

//学生审核
export function search(info) {
    return request({
        url: '/authen/list',
        method: 'post',
        data: info
    })
}

export function updateStatus(info) {
    return request({
        url: '/authen/updateStatus',
        method: 'post',
        data: info
    })
}

// 修改学生身份
export function updateRole(info) {
    return request({
      url: "/user/student_update_category",
      method: "post",
      data: info,
    });
  }