import api from '../../../utils/api.js';

Page({
  data: {
    userInfo: {},
    activityInfo: {},
    location: null,
    highlightedIndex: 1
  },

  onLoad(options) {
    const activityInfo = JSON.parse(decodeURIComponent(options.activityInfo));
    this.setData({activityInfo: activityInfo});
    console.log(this.data.activityInfo)
    api.student_info({ userId: activityInfo.issue }).then(res => {
      if (res.code == 200) {
        this.setData({ userInfo: res.data });
      } else {
        wx.showToast({ title: res.msg, icon: 'none', duration: 1000 });
      }
    });
  },
  toActivityTag(e) {
    this.setData({ highlightedIndex: parseInt(e.currentTarget.dataset.index) });
  },
  previewImage() {
    wx.previewImage({
      current: this.data.activityInfo.image,
      urls: [this.data.activityInfo.image]
    });
  },

  showContact() {
    if(this.data.activityInfo.contactType == "微信号") {
      wx.showModal({
        title: '联系发布人',
        content: '微信号: 1234567',
        showCancel: true,
        cancelText: '取消',
        confirmText: '复制',
        success: function(res) {
          if (res.confirm) {
            wx.setClipboardData({
              data: '1234567',
              success: function() {
                wx.getClipboardData({
                  success: function(res) {
                    console.log('已复制微信号到剪贴板');
                  }
                });
              },
              fail: function(err) {console.error('复制微信号失败:', err);}
            });
          } else if (res.cancel) {console.log('用户取消了复制');}
        }
      });
    }
    if(this.data.activityInfo.contactType == "手机号") {
      wx.makePhoneCall({
        phoneNumber: '1234567'
      });
    }
  },

  toBack() {
    wx.navigateBack({ delta: 1 });
  },

})