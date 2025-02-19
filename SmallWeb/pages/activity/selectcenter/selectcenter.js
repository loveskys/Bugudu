Page({
  onLoad(e) {
    wx.setStorageSync('isselect', 1);
  },
  onShow(e) {
    let isselect = wx.getStorageSync('isselect');
    if(isselect==1) {
      setTimeout(() => {
        wx.navigateTo({url: '/pages/activity/select/select'});
      }, 320);
    } else {
      wx.reLaunch({ url: '/pages/home/home' });
    }
  }
})