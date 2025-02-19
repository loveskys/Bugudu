import request from "@/utils/request";

//消息相关
//获取列表
export function search(info) {
  return request({
    url: "/msg/page_list",
    method: "post",
    data: info,
  });
}

//发送
export function send(info) {
    return request({
        url: "/msg/send_official",
        method: "post",
        data: info,
    });
};

export function deleteMsg(info) {
    return request({
        url: "/msg/delete_msg",
        method: "post",
        data: info,
    });
}
