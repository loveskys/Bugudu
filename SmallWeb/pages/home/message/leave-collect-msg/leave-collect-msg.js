import api from '../../../../utils/api.js';

Page({
  data: {
    msgArray: []
  },

  onLoad(options) {
    if(options.msgArray) {
      const msgArray = JSON.parse(decodeURIComponent(options.msgArray));
      this.setData({msgArray: msgArray});
      console.log(this.data.msgArray)
    }
  },


  toLookMsg(e) {
    const index = e.currentTarget.dataset.index;
    // const msgInfo = encodeURIComponent(JSON.stringify(this.data.msgArray[index]));
    // wx.navigateTo({ url: `/pages/home/message/msg-look/msg-look?msgInfo=${msgInfo}` });

    api.msg_read({id: this.data.msgArray[index].id}).then(response => {
      if (response.code === 200) {
        if (response.code === 200) {
          let items = this.data.msgArray;
          items[index].isRead = 1;
          this.setData({ msgArray: items });
        }
      }
    });
    wx.navigateTo({url: `/pages/activity/detail/detail?activityId=${this.data.msgArray[index].activityId}`});
  },


  back() {
    wx.navigateBack({ delta: 1 })
  },

})