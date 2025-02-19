import api from '../../../utils/api.js';

Page({
  data: {
    userInfo: {}
  },

  onShow() {
    api.student_info({}).then(response => {
      if (response.code == 200) {
        this.setData({ userInfo: response.data });
      } else {
        wx.showToast({ title: response.msg, icon: 'none', duration: 1000 });
      }
    });
  },

  toSettingPage(e) {
      const index = e.currentTarget.dataset.index;
      if(index==1) {
        if(this.data.userInfo.infoPerfect!=1) {
          wx.showModal({
            title: '个人信息不完整', content: '请先完善个人信息，再进行认证，是否前往？',
            cancelText: '取消', confirmText: '完善信息',
            success: function(respone) {
              if(respone.confirm) {
                wx.navigateTo({url: '/pages/index/my/detail/detail'});
              }
            }
          });
          return;
        }
        wx.navigateTo({url: '/pages/index/setting/authen/authen'});
      }
      if (index == 2) {
        wx.navigateTo({url: '/pages/index/setting/about/about'});
      }
      if (index == 3) {
        wx.navigateTo({url: '/pages/protocol/protocol'});
      }
      if (index == 4) {
        wx.navigateTo({url: '/pages/privacy/privacy'});
      }
      if (index == 5) {
        wx.showToast({ title: '该功能正在开发中~', icon: 'none', duration: 1000 });
        // wx.navigateTo({url: '/pages/index/setting/account/account'});
      }
  },

  // 删除“退出登陆”按钮
  logout() {
    wx.clearStorageSync();
    wx.reLaunch({url: '/pages/login/login'});
  },

});