import api from '../../../utils/api.js';

Page({
  data: {
    officialList: [],

    commentsAndCollectList: [],
    commentsAndCollectAllRead: true,

    activityMsgList: [],
    activityMsgAllRead: true
  },

  onShow(options) {
    this.getMessageList();
  },

  getMessageList() {
    api.msg_list({}).then(response => {
      if (response.code === 200) {
        this.setData({
          officialList: response.data.officialList,
          commentsAndCollectList: response.data.commentsAndCollectList,
          commentsAndCollectAllRead: response.data.commentsAndCollectAllRead,
          activityMsgList: response.data.activityMsgList,
          activityMsgAllRead: response.data.activityMsgAllRead
        });
        console.log(response);
      }
    });
  },

  toPlateTag(e) {
    this.setData({ plateIndex: parseInt(e.currentTarget.dataset.index) });
  },

  toLookMsg(e) {
    const index = e.currentTarget.dataset.index;
    const msgInfo = encodeURIComponent(JSON.stringify(this.data.officialList[index]));
    wx.navigateTo({ url: `/pages/home/message/msg-look/msg-look?msgInfo=${msgInfo}` });
  },


  toCommentsAndCollect() {
    let msgArray = encodeURIComponent(JSON.stringify(this.data.commentsAndCollectList));
    wx.navigateTo({ url: `/pages/home/message/leave-collect-msg/leave-collect-msg?msgArray=${ msgArray }` });
  },

  toActivityMsg() {
    let msgArray = encodeURIComponent(JSON.stringify(this.data.activityMsgList));
    wx.navigateTo({ url: `/pages/home/message/activity-msg/activity-msg?msgArray=${ msgArray }` });
  },
  
  readAll() {
    api.msg_read_all({})
    .then(response => {
      if (response.code === 200) {
        console.log(response);
        this.getMessageList();
        wx.showToast({
          title: '消息已读',
          icon: 'success', // 成功图标
          duration: 2000 // 持续时间 2 秒
        });
      }


    });
  },

})