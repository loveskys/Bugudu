import api from '../../utils/api.js';

Page({
  data: {
    plateIndex: 0,
    swiperList: [],
    activityList: [],
    istoken: false,
  },

  onLoad(options) {
    this.getSwiperList();
    this.getActivityList();
    this.getToken();
    // 自定义tabBar页面刷新
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0
      })
    }
},

  onShow() {

  },
  // 下拉刷新时触发
  onPullDownRefresh() {
    // 重新加载数据
    this.getSwiperList();
    this.getActivityList();
    // 数据加载完成后停止刷新动画
    setTimeout(() => {
      wx.stopPullDownRefresh();
    }, 200);  // 可以根据实际情况调整时间或在数据加载完毕后调用
  },

getToken() {
  return new Promise((resolve, reject) => {
    // const token = wx.getStorageSync('token')
    // if(token) {
    //   this.setData({istoken : true});
    //   resolve(true);
    // }
    wx.login({success: (wxres) => {
      if (wxres.code) {
        api.wxlogin({ code: wxres.code }).then(response => {
          if (response.code == 200) {
            wx.setStorageSync('userId', response.data.id);
            wx.setStorageSync('token', response.data.token);
            console.log("yes yes get token")
            this.setData({istoken : true});
            resolve(true);
          } else {
            reject(false);
          }
        });
      }
    },
    fail: (error) => {
      reject(false);
      }
    });
  });
},

  onShareAppMessage: function (options) {  
    return {  
      title: '不咕嘟 欢迎你来', // 分享标题  
      path: '/pages/home/home', // 分享路径  
      imageUrl: '/images/login/logo.png', // 自定义分享图片  
    }  
  },

 async getSwiperList() {
    api.swiper_list().then(res => {
      if (res.code == 200) {
        this.setData({swiperList: res.data});
        console.log(res);
      } else {
        console.log(res.msg);
        wx.showToast({ title: res.msg, icon: 'none', duration: 1000 });
      }
    });
  },
  
  toPlateTag(e) {
    this.setData({ plateIndex: parseInt(e.currentTarget.dataset.index) });
    this.setData({ activityList: [] });
    this.getActivityList();
  },

  async getActivityList() {
    api.home_list({ tag: this.data.plateIndex }).then(res => {
      if (res.code == 200) {
        this.setData({activityList: res.data});
        console.log(res);
      } else {
        wx.showToast({ title: res.msg, icon: 'none', duration: 1000 });
      }
      this.CalRelativeDate(); // 用于计算相对日期
    });
  },

  CalRelativeDate(){
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

  toSwiperItem(e) {
    const index = e.currentTarget.dataset.index;
    const type = this.data.swiperList[index].type;
    console.log(type);
    if(type == 0) //活动
    {
      wx.navigateTo({url: `/pages/activity/detail/detail?activityId=${this.data.swiperList[index].activityId}`});
    }
    else if(type == 1) //文章
    {
      wx.navigateTo({
        url: '/pages/webview/webview?url=' + encodeURIComponent(this.data.swiperList[index].url),
      });
    }

  },
  
  toActivityItem(e) {
    // 判断是否登录
    let userId = wx.getStorageSync('userId');
    if (!userId) {
      console.log('未登录')
    }

    const index = e.currentTarget.dataset.index;
    wx.navigateTo({url: `/pages/activity/detail/detail?activityId=${this.data.activityList[index].id}`});
  },

  toSearch() {
    wx.navigateTo({url: '/pages/home/search/search'});
  },
  toMsg() {
    wx.navigateTo({url: '/pages/home/message/message'});
  },


});