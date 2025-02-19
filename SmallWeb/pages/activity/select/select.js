import api from '../../../utils/api.js';

Page({
  data: {
    // selectIndex: 0,
    userInfo: {}
  },

  onShow() {
    // wx.setStorageSync('isselect', 0);
    // this.data.selectIndex = 0;
    this.getData();
  },

  getData() {
    api.student_info({}).then(res => {
      if (res.code == 200) {
        this.setData({
          userInfo: res.data
        });
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none',
          duration: 1000
        });
      }
    });
  },

  handleHome(e) {
    wx.switchTab({
      url: '/pages/home/home'
    });
  },

  confirm(e) {
    const i = parseInt(e.currentTarget.dataset.index)

    if (this.data.userInfo.infoPerfect != 1) {
      wx.showModal({
        title: '个人信息不完整',
        content: '请完善个人信息，再通过认证后才能发布活动，是否前往？',
        cancelText: '取消',
        confirmText: '完善信息',
        success: function (respone) {
          if (respone.confirm) {
            wx.navigateTo({
              url: '/pages/index/my/detail/detail'
            });
          } else if (respone.cancel) {}
        }
      });
      return;
    }

    if (this.data.userInfo.authStatus != 2) {
      wx.showModal({
        title: '认证通过后才能发布活动',
        content: '是否前往认证？',
        cancelText: '取消',
        confirmText: '前往认证',
        success: function (respone) {
          if (respone.confirm) {
            wx.navigateTo({
              url: '/pages/index/setting/authen/authen'
            });
            return;
          } else if (respone.cancel) {}
        }
      });
      return;
    }

    if (i == 1) {
      console.log(i)
    } else if (i == 2) {
      wx.navigateTo({
        url: '/pages/activity/add/add'
      });
    }
  },
})