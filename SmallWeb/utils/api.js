import { myUpload,request} from './request';

const api = {
  //登录相关
  login: (data) => request({ url: `user/student_login`, data }),
  wxlogin: (data) => request({ url: `user/student_wx_login`, data }),
  register: (data) => request({ url: 'user/student_register', data }),
  student_info: (data) => request({ url: 'user/student_info', data }),
  student_save: (data) => request({ url: 'user/student_save', data }),

  //认证相关
  authen_submit: (filePath, formData) => myUpload('authen/submit', 'authenImg', filePath, formData),
  authen_resubmit: (data) => request({ url: 'authen/resubmit', data }),
  authen_detail: (data) => request({ url: 'authen/authen_detail', data }),

  //活动相关
  originate: (filePath, formData) => myUpload('activity/originate', 'activityImg', filePath, formData),
  originate_contact: (filePath, formData) =>  myUpload('activity/originate_contact', 'contactImg', filePath, formData),
  get_contact: (data) => request({url:'activity/get_contact',data}),
  reoriginate: (data) => request({url: 'activity/reoriginate', data}),
  user_list: (data) => request({ url: 'activity/user_list', data }),
  activity_detail: (data) => request({ url: 'activity/detail', data }),
  activity_collect: (data) => request({ url: 'activity/collect', data }),
  activity_get_leave: (data) => request({ url: 'activity/get_leaves', data }),
  activity_send_leave: (data) => request({ url: 'activity/send_leaves', data }),
  activity_delete_leave: (data) => request({ url: 'activity/delete_leaves', data }),
  activity_apply: (data) => request({ url: 'activity/apply', data }),
  activity_apply_cancel: (data) => request({ url: 'activity/apply_cancel', data }),
  activity_delete: (data) => request({url:'activity/delete', data}),

  activity_return_applicants: (data) => request({url: 'activity/return_applicants', data}),

  //首页搜索相关
  swiper_list: () => request({ url: 'home/swiper_list' }),
  home_list: (data) => request({ url: 'home/home_list', data }),
  get_search_history: (data) => request({ url: 'home/get_search_history', data }),
  to_search: (data) => request({ url: 'home/to_search', data }),

  //首页消息相关
  msg_list: (data) => request({ url: 'msg/my_msg_list', data }),
  msg_read: (data) => request({ url: 'msg/read', data }),
  msg_read_all: (data) => request({ url: 'msg/readAll', data }),
  
}; 

export default api;