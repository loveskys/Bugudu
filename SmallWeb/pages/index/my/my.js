import api from '../../../utils/api.js';

Page({
  data: {
    userInfo: {},
    hobbyArray: [], // 包含星座、MBTI、兴趣的数组（如果存在的话）
    plateIndex: 1,
    activityList: [],
    vertification: [
    "/images/vertification/developerVertification.png",
    "/images/vertification/ambassadorVertification.png",
    "/images/vertification/officialVertification.png"
    ],
  },

  onShow() {
    this.getUserInfo();
    this.getActivityList();

    // 自定义tabBar页面刷新
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      })
    }
  },

  getUserInfo() {
    api.student_info({}).then(response => {
      if (response.code == 200) {
        this.setData({
          userInfo: response.data
        });
        console.log(response.data);
        // 包含星座、MBTI、兴趣的数组（如果存在的话）
        let hobbyArray = [
          this.data.userInfo.constellation,
          ...(this.data.userInfo.mbti ? [this.data.userInfo.mbti] : []),
          ...(this.data.userInfo.hobbyArray ? this.data.userInfo.hobbyArray : [])
        ];
        this.setData({
          hobbyArray: hobbyArray
        });
    
      } else {
        wx.showToast({
          title: response.msg,
          icon: 'none',
          duration: 1000
        });
      }
    });
  },

  delete_test() {
    api.activity_delete({
      id: 28 //测试
    }).then(
      response => {
        if (response.code == 200) {
          console.log(response);
        } else {
          wx.showToast({
            title: response.msg,
            icon: 'none',
            duration: 1000
          });
        }
      }
    )
  },

  toSetting() {
    wx.navigateTo({
      url: '/pages/index/setting/setting'
    });
  },
  toUserEdit() {
    wx.navigateTo({
      url: '/pages/index/my/detail/detail'
    });
  },
  toUserAuthen() {
    if (this.data.userInfo.infoPerfect != 1) {
      wx.showModal({
        title: '个人信息不完整',
        content: '请先完善个人信息，再进行认证，是否前往？',
        cancelText: '取消',
        confirmText: '完善信息',
        success: function (respone) {
          if (respone.confirm) {
            wx.navigateTo({
              url: '/pages/index/my/detail/detail'
            });
          }
        }
      });
      return;
    }
    wx.navigateTo({
      url: '/pages/index/setting/authen/authen'
    });
  },

  toPlateTag(e) {
    this.setData({
      plateIndex: parseInt(e.currentTarget.dataset.index)
    });
    this.getActivityList();
  },

  getActivityList() {
    api.user_list({
      tag: this.data.plateIndex
    }).then(response => {
      if (response.code === 200) {
        this.setData({ activityList: response.data });
      }
      this.calcRelativeDate(); // 用于计算相对日期
    });
  },

  calcRelativeDate(){
    let activityList = this.data.activityList;
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = (currentDate.getMonth() + 1).toString().padStart(2, '0'); // 月份从0开始
    const currentDay = currentDate.getDate().toString().padStart(2, '0');
    const formattedCurrentDate = `${currentYear}-${currentMonth}-${currentDay}`;
    if (Array.isArray(activityList)) 
    {
      // 如果 activityList 是一个数组，则进行遍历和修改
      activityList.forEach(activity => {
        const activityDate = activity.activityStart.split(" ")[0];
        const activityEndDate = activity.activityEnd.split(" ")[0];
        // 将当前日期和活动日期转为 Date 对象
        const currentDateObj = new Date(formattedCurrentDate);
        const activityDateObj = new Date(activityDate);
        const activityEndDateObj = new Date(activityEndDate);
        const activityDateMaD = `${(activityDateObj.getMonth() + 1).toString().padStart(2, '0')}-${activityDateObj.getDate().toString().padStart(2, '0')}`;
        const timeDifference = activityDateObj - currentDateObj;
        const timeDifference2 = activityEndDateObj - currentDateObj;
        const daysRemaining = Math.ceil(timeDifference / (1000 * 60 * 60 * 24)); // 毫秒转天数
        const daysRemaining2 = Math.ceil(timeDifference2 / (1000 * 60 * 60 * 24)); // 毫秒转天数
        // 判断剩余天数，并更新 RelativeDate
        if (daysRemaining > 0 && daysRemaining < 6) {
          activity.RelativeDate = `${daysRemaining}天后`;
        } 
        else if (daysRemaining > 5)
        {
          activity.RelativeDate = activityDateMaD;
        }
        else if (daysRemaining === 0) {
          activity.RelativeDate = '今天';
        } 
        else{
          if(daysRemaining2 >= 0){
            activity.RelativeDate = '进行中';
          }
          else{
            activity.RelativeDate = '已过期';
          }

        }
      });
      this.setData({activityList: activityList});
    }
    else {
      console.error("activityList 未定义或不是数组");
    }
  },

  toActivityItem(e) {
    const index = e.currentTarget.dataset.index;
    wx.navigateTo({
      url: `/pages/activity/detail/detail?activityId=${this.data.activityList[index].id}`
    });
  }

});