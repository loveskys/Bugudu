import api from '../../../../utils/api.js';

Page({
  data: {
    msgInfo: {},
  },

  onLoad(options) {
    const msgInfo = JSON.parse(decodeURIComponent(options.msgInfo));
    this.setData({msgInfo: msgInfo});
    this.readMsg();
  },


  readMsg() {
    api.msg_read({id: this.data.msgInfo.id}).then(response => {
      if (response.code === 200) {}
    });
  },


  back() {
    wx.navigateBack({ delta: 1 })
  },

})