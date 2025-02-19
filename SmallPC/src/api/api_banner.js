import request from "@/utils/request";

// 轮播图相关
// 获取轮播图列表
export function getLoopImageList(info) {
  return request({
    url: "/loopimg/list",  // 获取轮播图列表的API路径
    method: "post",
    data: info,  // 传递分页等查询参数
  });
}

// 上传轮播图
export function uploadLoopImage(info) {
  return request({
    url: "/loopimg/upload",  
    method: "post",
    data: info,  
    headers: {
      'Content-Type': 'multipart/form-data'  
    }
  });
}

// 删除轮播图
export function deleteLoopImage(info) {
  return request({
    url: "/loopimg/delete",  // 删除轮播图的API路径
    method: "post",
    data: info,  // 传递删除所需的信息，如轮播图ID
  });
}
