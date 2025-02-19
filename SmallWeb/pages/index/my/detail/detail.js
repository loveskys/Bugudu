import api from '../../../../utils/api.js';

Page({
  data: {
    userInfo: {
      birthdate: "2002-01-01",
    },
    avatarUrl: "/images/updateHeadUrl.png",
    sex: 0,

    mbtiPopupVisible: false,
    mbtiOptions: ['INTJ','INTP','ENTJ','ENTP','INFJ','INFP','ENFJ','ENFP','ISTJ','ISFJ','ESTJ','ESFJ','ISTP','ISFP','ESTP','ESFP'],

    hobbyPopupVisible: false,
    hobbyOptions: ['篮球','羽毛球','骑行','排球','游泳','跑步','足球','乒乓球','网球','滑雪','冲浪','攀岩','瑜伽','舞蹈','绘画','摄影','书法','吉他','钢琴','象棋','围棋','阅读','写作','旅行','园艺','烹饪','钓鱼','打游戏']
  },

  onLoad() {
    api.student_info({}).then(response => {
      if (response.code == 200) {
        this.setData({ avatarUrl: response.data.headUrl});
        this.setData({ userInfo: response.data });
      } else {
        wx.showToast({ title: response.msg, icon: 'none', duration: 1000 });
      }
    })
  },

  mbti_open() {
    this.setData({ mbtiPopupVisible: true });
  },
  mbti_select(e) {
    this.setData({ mbtiPopupVisible: false, 'userInfo.mbti': e.detail.selectedItems[0] });
  },
  mbti_close() {
    this.setData({ mbtiPopupVisible: false });
  },

  onMbtiChange: function(event) {
    const selectedIndex = event.detail.value;
    this.setData({
      'userInfo.mbti': this.data.mbtiOptions[selectedIndex]
    });
  },

  hobby_open() {
    this.setData({ hobbyPopupVisible: true });
  },
  hobby_select(e) {
    this.setData({ hobbyPopupVisible: false, 'userInfo.hobby': e.detail.selectedItems });
  },
  hobby_close() {
    this.setData({ hobbyPopupVisible: false });
  },

  saveSubmit() {
    if(this.data.sex) {
      this.data.userInfo.sex = this.data.sex;
    }
    if (typeof this.data.userInfo.hobby === 'string') {
      let hobbyArray = this.data.userInfo.hobby.split(',');
      this.setData({ 'userInfo.hobby': hobbyArray });
    }

    api.student_save(this.data.userInfo).then(response => {
      if (response.code == 200) {
        wx.showToast({ title: '保存成功', icon: 'success', duration: 1000 });
        setTimeout(function(){wx.navigateBack({delta: 1});}, 800);
      } else {
        wx.showToast({ title: response.msg, icon: 'none', duration: 1000 });
      }
    });
  },

  onChooseAvatar(e) {
    // 更换头像后会刷新页面，如果使用onShow函数获取学生信息，则会在更换头像后将已编辑但未保存的数据覆盖掉
    const { avatarUrl } = e.detail
    this.setData({ avatarUrl });
    this.base64(avatarUrl, "png").then(res => {
      this.setData({'userInfo.headUrl': res});
    });
  },
  //转base64
  base64(url, type) {
    return new Promise((resolve, reject) => {
      wx.getFileSystemManager().readFile({
        filePath: url, encoding: 'base64',
        success: res => {
          resolve('data:image/' + type.toLocaleLowerCase() + ';base64,' + res.data)
        }, fail: res => reject(res.errMsg)
      })
    });
  },
  bindKeyInput(e) {
    const key = e.currentTarget.dataset.key, value = e.detail.value;
    this.setData({ [key]: value });
  }

});