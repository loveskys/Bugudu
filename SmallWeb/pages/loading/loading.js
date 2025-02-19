// pages/loading/loading.js
import api from '../../utils/api.js';

Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.login({success: (wxres) => {
      console.log(wxres);
      if (wxres.code) {
        api.wxlogin({ code: wxres.code }).then(response => {
          console.log(response.code)
          if (response.code == 200) {
            wx.setStorageSync('userId', response.data.id);
            wx.setStorageSync('token', response.data.token);
            console.log(response.data.token);
            wx.reLaunch({ url: '/pages/home/home' });
          } else {
            wx.showToast({ title: '微信登录失败', icon: 'none', duration: 1000 });
          }
        });
      }
    },
    fail: (error) => {
      wx.showToast({ title: '请求微信code失败', icon: 'none', duration: 1000 });
    }
  });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})