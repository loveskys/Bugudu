import api from '../../utils/api.js';

Component({
  data: {
    isLogin: true,
    isChecked: 0,
    isSubmitting: false,
    loginInfo: {
      userName: '',
      passWord: '',
    },
  },

  methods: {
    onShareAppMessage: function () {  
      return {  
        title: '不咕嘟 欢迎你来', // 分享标题  
        path: '/pages/login/login', // 分享路径  
        imageUrl: '/images/login/logo.png', // 自定义分享图片  
      }  
    },


    wxlogin(e) {
      if(this.data.isChecked == 0) {
        wx.showToast({ title: '请同意不咕嘟服务及隐私协议', icon: 'none', duration: 1000 });
        return;
      }

      if (this.data.isSubmitting) {
        wx.showToast({ title: '请不要点击过快', icon: 'none', duration: 1500 });return;
      }

      this.data.isSubmitting = true;
      wx.login({success: (wxres) => {
          if (wxres.code) {
            api.wxlogin({ code: wxres.code }).then(response => {
              if (response.code == 200) {
                wx.setStorageSync('userId', response.data.id);
                wx.setStorageSync('token', response.data.token);
                wx.switchTab({ url: '/pages/home/home' });
              } else {
                wx.showToast({ title: '微信登录失败', icon: 'none', duration: 1000 });
              }
              this.data.isSubmitting = false;
            });
          }
        },
        fail: (error) => {
          this.data.isSubmitting = false;
          wx.showToast({ title: '请求微信code失败', icon: 'none', duration: 1000 });
        }
      });
      setTimeout(() => { this.data.isSubmitting = false; }, 3000);
    },

    checkboxChange(e) {
      const values = e.detail.value;
      this.setData({ isChecked: values.length });
    },

    toProtocol() {
      wx.navigateTo({url: '/pages/protocol/protocol'});
    },

    toPrivacy() {
      wx.navigateTo({url: '/pages/privacy/privacy'});
    },

    phonelogin() {
      // wx.showToast({ title: '该功能正在开发', icon: 'none', duration: 1000 });
      wx.switchTab({
        url: '/pages/home/home',
      })
    }

  },
})