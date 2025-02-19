import api from '../../../../utils/api.js';

Page({
  data: {
    authStatus: '0',
    authenInfo: {},

    schoolNameIndex: 0,
    schoolNameArray: ['哈工大（深圳）','清华大学深圳研究生院', '北京大学深圳研究生院', '南方科技大学', '深圳职业技术大学', '深圳大学'],

    authenTypeIndex: 0,
    authenTypeArray: ['录取通知书','学生卡','学生证'],
    authenImg: '',

    isSubmitting: false
  },
  
  //提交
  submit() {
    if (this.data.authenInfo.schoolName === undefined) {
      wx.showToast({ title: '请填写所在学校', icon: 'none', duration: 1000 });return;
    }
    if (!this.data.authenInfo.studentNum) {
      wx.showToast({ title: '请填写学号', icon: 'none', duration: 1000 });return;
    }
    if (!this.data.authenInfo.studentNum2) {
      wx.showToast({ title: '请再次填写学号', icon: 'none', duration: 1000 });return;
    }
    if (this.data.authenInfo.studentNum !== this.data.authenInfo.studentNum2) {
      wx.showToast({ title: '请确保两次填写的学号一致', icon: 'none', duration: 1000 });return;
    }
    if (this.data.authenInfo.authenType === undefined) {
      wx.showToast({ title: '请填写用于认证的证件', icon: 'none', duration: 1000 });return;
    }

    let submit_tt = 0;
    if(!this.data.authenInfo.authenImg && this.data.authenImg == '') {
       wx.showToast({ title: '请上传证件照片', icon: 'none', duration: 1000 });return;
    }
    if(this.data.authenInfo.authenImg && this.data.authenImg == '') {
      submit_tt = 1;
    }


    if (this.data.isSubmitting) {
      wx.showToast({ title: '请不要重复点击', icon: 'none', duration: 1000 });return;
    }
    this.data.isSubmitting = true;

    const that = this;
    wx.showModal({title: '请确认', content: '信息确认无误后提交', success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '提交中',mask: true });
          if(submit_tt===0) { //提交
            api.authen_submit(that.data.authenImg, that.data.authenInfo).then((response) => {
              if(response.code == 200) {
                this.data.isSubmitting = false;
                wx.hideLoading();
                wx.showToast({title: '提交成功', icon: 'success', duration: 1000});
                that.getInfoAuthenInfo();
              } else {
                wx.showToast({title: response.msg, icon: 'none', duration: 1000});
              }
            }).catch((error) => {
              this.data.isSubmitting = false;
              wx.hideLoading();
              wx.showToast({ title: '请求失败', icon: 'none', duration: 1000 });
            });
          }

          if(submit_tt===1) {  //退回再提交
            api.authen_resubmit(that.data.authenInfo).then((response) => {
              if (response.code === 200) {
                this.data.isSubmitting = false;
                wx.hideLoading();
                wx.showToast({title: '提交成功', icon: 'success', duration: 1000});
                that.getInfoAuthenInfo();
              } else {
                this.data.isSubmitting = false;
                wx.hideLoading();
                wx.showToast({title: response.msg, icon: 'none', duration: 1000});
              }
            }).catch((error) => {
              wx.hideLoading();
              this.data.isSubmitting = false;
              wx.showToast({ title: '请求失败', icon: 'none', duration: 1000 });
            });
          }
        } else if (res.cancel) {
          this.data.isSubmitting = false;
          wx.hideLoading();
        }
        
      }
    });
  },
  onShow() {
    this.getInfoAuthenInfo()
  },

  getInfoAuthenInfo() {
    api.authen_detail({}).then(response => {
      if (response.code == 200) {
        this.setData({ authenInfo: response.data });
        this.setData({ authStatus: response.data.status });
      } else {
        console.log(response)
      }
    });
  },

  bindPickerschoolName(e) {
    this.setData({ schoolNameIndex: e.detail.value });
    this.setData({ 'authenInfo.schoolName': this.data.schoolNameArray[e.detail.value] });
  },
  bindPickerAuthenType(e) {
    this.setData({ authenTypeIndex: e.detail.value });
    this.setData({ 'authenInfo.authenType': this.data.authenTypeArray[e.detail.value] });
  },
  bindKeyInput(e) {
    const key = e.currentTarget.dataset.key, value = e.detail.value;
    this.setData({ [key]: value });
  },
  
  addAuthenImg() {
    const that = this;
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      camera: 'back',
      success(res) {
        const tempFile = res.tempFiles[0].tempFilePath;
        const tempSize = res.tempFiles[0].size
        that.setData({ authenImg: tempFile });
        wx.getImageInfo({
          src: tempFile,
          success: function (info) {
            if (tempSize > 1024 * 1024) {
              console.log("大于1M："+tempSize)
              wx.compressImage({
                src: tempFile,
                quality: 50,
                success: (compressedRes) => {
                  const compressedTempFilePath = compressedRes.tempFilePath;
                  that.setData({authenImg: compressedTempFilePath});
                }, fail: (error) => {console.error('压缩失败:', error)}
              });
            } else {
              console.log("小于1M："+tempSize)
              that.setData({authenImg: tempFile});
            }
          }, fail: function (error) { console.error('获取图片信息失败:', error) }
        });
      }
    });
  },
  

  toPrivacy() {
    wx.navigateTo({url: '/pages/privacy/privacy'});
  },
  previewImage() {
    wx.previewImage({
      current: this.data.authenInfo.authenImg,
      urls: [this.data.authenInfo.authenImg]
    });
  },
});