import request from "@/utils/request";

//活动相关
//搜索
export function search(info) {
  return request({
    url: "/activity/list",
    method: "post",
    data: info,
  });
}

//活动审核
export function originateAudit(info) {
  return request({
    url: "/activity/originate_audit",
    method: "post",
    data: info,
  });
}

//学生报名审核
export function applyAudit(info) {
  return request({
    url: "/activity/apply_audit",
    method: "post",
    data: info,
  });
}

//删除活动（设置为失效）
export function deleteActivity(info) {
  return request({
    url: "/activity/delete",
    method: "post",
    data: info,
  });
}


//设置推荐活动
export function recommendActivity(info) {
    return request({
      url: "/activity/recommend",
      method: "post",
      data: info,
    });
  }

// 设置推荐轮播图
export function setCarousel(info) {
  return request({
    url: "/activity/setCarousel", // 
    method: "post",
    data: info,
  });
}

//根据id获取活动详情
export function pc_detail(info) {
    return request({
        url: "/activity/pc_detail",
        method: "post",
        data: info,
    });
}
  
