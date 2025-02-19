Page({
  goBackToLogin() {
    // wx.redirectTo({ url: '/pages/login/login' });
    wx.navigateBack({delta: 1});
  }
});