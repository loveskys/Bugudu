// import { View } from 'XrFrame/kanata/lib/index';
import api from '../../../utils/api.js';

Page({
  data: {
    userInfo: {},
    displayAuthenArray: [], // 活动发起用户的学校和年级信息

    // 报名用户认证信息
    authStatus: '0', // 0为未认证，1为正在审核，2为已认证
    authenInfo: {},

    activityId: '',
    activityInfo: {},
    participationArray: [],

    leaves: [],
    leavePopupVisible: false,

    highlightedIndex: 1,

    applyPopupVisible: false,
    apply: {},
    
    // 活动参加者
    applicantsVisible: false,
    applicants: [],

    isSubmitting: false,
    isreqc: false,
    isshare: false,
    emptyHead: "/images/activity/emptyhead.png",
    pages:{},
    //认证图标
    vertification: [
      "/images/vertification/developerVertification.png",
      "/images/vertification/ambassadorVertification.png",
      "/images/vertification/officialVertification.png"
      ],
    url: "https://mp.weixin.qq.com/s/KSAtjnK04LMH1FxZKm0kFg",
    wxcontent:"",
    loading:true
  },
  onShow() {
    this.getInfoAuthenInfo() // 页面显示时获取报名用户认证信息
  },
  onLoad(options) { 
    const pages = getCurrentPages(); // 获取当前页面栈
    this.setData({
      'pages' : pages
    })
    // 如果从分享进入页面，options.isshare为1，否则为undefined
    if (pages.length == 1) {
      this.setData({
        'isshare': true
      })
    }
    this.data.activityId = options.activityId;
    this.getActInfo();
  },


  getToken() {  
    return new Promise((resolve, reject) => {  
        const token = wx.getStorageSync('token');  
        if (token) {  
            resolve(true);  
            return; // 确保在resolve后退出函数  
        }  
        
        wx.login({  
            success: (wxres) => {  
                if (wxres.code) {  
                    api.wxlogin({ code: wxres.code }).then(response => {  
                        if (response.code == 200) {  
                            wx.setStorageSync('userId', response.data.id);  
                            wx.setStorageSync('token', response.data.token);  
                            console.log("成功获取到token：",response.data.token);
                            resolve(true);  
                        } else {  
                          console.log("没有获取到token");
                            reject(false);  
                        }  
                    }).catch(error => {  
                        reject(false); // 处理api调用失败的情况  
                        console.log("api.login调用失败");
                    });  
                } else {  
                    console.log("wx.login调用失败");
                    reject(false); // 处理wx.login失败的情况  
                }  
            },  
            fail: (error) => { 
                console.log("wx.login显示fail"); 
                reject(false);  
            }  
        });  
    });  
},

  // 获取报名用户认证信息
  async getInfoAuthenInfo() {
    if(!await this.getToken()) {
      return false;
    }
    api.authen_detail({}).then(response => {
      if (response.code == 200) {
        this.setData({ authenInfo: response.data });
        this.setData({ authStatus: response.data.status });
      } else {
        console.log(response)
      }
    });
  },

  // 获取活动发起用户的学校和年级信息
  getDisplayAuthenArray() {
    if (Array.isArray(this.data.userInfo.authenArray)) {
      return this.data.userInfo.authenArray.slice(0, 2);

    } else {
      console.error("不是数组")
    }
    return [];
  },

  async getUserDetail() {
    if(!await this.getToken()) {
      return false;
    }
    api.student_info({
      userId: this.data.activityInfo.issue
    }).then(res => {
      if (res.code == 200) {
        this.setData({
          userInfo: res.data
        });
        // console.log(this.data.userInfo);
        // console.log(res.data)
        this.setData({
          displayAuthenArray: this.getDisplayAuthenArray() // 获取活动发起用户的学校和年级信息
          // displayAuthenArray: this.data.userInfo.authenArray.slice(0, 2);
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

  async getActInfo() {
    if(!await this.getToken()) {
      return false;
    }
    api.activity_detail({
      id: this.data.activityId
    }).then(res => {
      if (res.code === 200) {
        this.setData({
          activityInfo: res.data,
        });
        console.log(res);
        this.getUserDetail();
        this.articleRequest();
      }
    });
  },

  toActivityTag(e) {
    let index = parseInt(e.currentTarget.dataset.index);
    console.log(e.currentTarget)
    this.setData({
      highlightedIndex: index
    });
    if (index == 3) {
      this.getLeaves()
    }
  },

  // 获取留言
  async getLeaves() {
    if(!await this.getToken()) {
      return false;
    }
    api.activity_get_leave({
      activityId: this.data.activityInfo.id
    }).then(res => {
      if (res.code === 200) {
        this.setData({
          // 倒序列表实现留言按新到旧排列
          leaves: res.data.reverse()
        });
      }
    });
  },

  previewImage() {
    wx.previewImage({
      current: this.data.activityInfo.image,
      urls: [this.data.activityInfo.image]
    });
  },

  previewContactImage() {
    wx.previewImage({
      current: this.data.activityInfo.contactImage,
      urls: [this.data.activityInfo.contactImage]
    });
  },

  // 这个函数有什么用？
  showContact() {
    if (this.data.activityInfo.contactType == "微信号") {
      wx.showModal({
        title: '联系发布人',
        content: '微信号: 1234567',
        showCancel: true,
        cancelText: '取消',
        confirmText: '复制',
        success: function (res) {
          if (res.confirm) {
            wx.setClipboardData({
              data: '1234567',
              success: function () {
                wx.getClipboardData({
                  success: function (res2) {
                    console.log('已复制微信号到剪贴板');
                  }
                });
              },
              fail: function (err) {
                console.error('复制微信号失败:', err);
              }
            });
          } else if (res.cancel) {
            console.log('用户取消了复制');
          }
        }
      });
    }
    if (this.data.activityInfo.contactType == "手机号") {
      wx.makePhoneCall({
        phoneNumber: '1234567'
      });
    }
  },

  showLocal() {
    const that = this;
    wx.authorize({
      scope: 'scope.userLocation',
      success: function () {
        // 打开目标位置的地图  
        wx.openLocation({
          latitude: parseFloat(that.data.activityInfo.latitude), // 你的目标纬度  
          longitude: parseFloat(that.data.activityInfo.longitude), // 你的目标经度  
          name: that.data.activityInfo.locationName, // 目标位置名称 
          scale: 10 // 缩放比例  
        });
      },
      fail: function () {
        wx.showModal({
          title: '提示',
          content: '您拒绝了位置权限，请打开位置权限以正常使用本功能',
          showCancel: false
        });
      }
    });
  },

  //查看发布者用户
  toUserPreview() {
    if (this.data.activityInfo.itSme == 1) {
      wx.switchTab({
        url: '/pages/index/my/my'
      });
    } else {
      wx.navigateTo({
        url: `/pages/index/my/preview/preview?userId=${this.data.activityInfo.issue}`
      });
    }
  },

  // 提供可复制的主办方联系方式的弹窗，需要传入弹窗标题及文本内容作为参数
  // 如果发起者上传的是二维码，则跳出另一个弹窗，点击可查看二维码图片
  showUserInfo(that, title, content){
    if (that.data.activityInfo.contactImage) {
      wx.showModal({
        title: title,
        cancelText: '取消',
        confirmText: '二维码', // confirmText超过4个汉字则无法显示
        success: function (response) {
          if (response.confirm) {
            that.previewContactImage();
          } else if (response.cancel) {
            that.apply_close();
          }
        }
      });
    } else {
      wx.showModal({
        title: title,
        content: content,
        cancelText: '取消',
        confirmText: '复制', //confirmText超过4个字将不会显示弹框（小程序的限制）
        success: function (response) {
          if (response.confirm) {
            wx.setClipboardData({
              data: that.data.activityInfo.contact,
              success: function () {
                wx.getClipboardData({
                  success: function (respone2) {
                    wx.showToast({
                      title: '已复制到剪贴板',
                      icon: 'none',
                      duration: 1000
                    });
                    that.getActInfo();
                    that.apply_close();
                  }
                });
              }
            });
          } else if (response.cancel) {
            that.apply_close();
          }
        }
      });
    }
  },

  //查看参与用户
  toUserDetail(e) {
    const index = e.currentTarget.dataset.index;
    const person = this.data.activityInfo.enrolledDetails[index];
    if (person.userId && person.userHeadUrl) {
      wx.navigateTo({
        url: `/pages/index/my/preview/preview?userId=${person.userId}`
      });
    }
    // console.log(person.itSme)
    // if (person.itSme == 1) {
    //   wx.switchTab({ url: '/pages/index/my/my' });
    // } else {
    // }
  },

  toLeavesUserDetail(e) {
    const index = e.currentTarget.dataset.index;
    const levalObj = this.data.leaves[index];
    wx.navigateTo({
      url: `/pages/index/my/preview/preview?userId=${levalObj.sendUserId}`
    });
    // if (levalObj.itSme == 1) {
    //   wx.switchTab({ url: '/pages/index/my/my' });
    // } else {
    // }
  },

  // 打开留言组件
  async sendLeave_open() {
    if(!await this.getToken()) {
      return false;
    }
    // 若未认证则弹窗跳转认证页面
    if (this.data.authStatus != '2') {
      wx.showModal({
        title: '温馨提示',
        content: '请先认证后再留言哦~',
        cancelText: '取消',
        confirmText: '前往认证', // confirmText超过4个汉字则无法显示
        success: function (response) {
          wx.navigateTo({ url: '/pages/index/setting/authen/authen' });
        }
      })
    } else {
      this.setData({ leavePopupVisible: true });
    }
  },
  // 该方法绑定leave组件closeSendLeave事件
  sndLeave_close() {
    this.setData({
      leavePopupVisible: false
    });
    this.getLeaves() // 留言成功自动刷新留言
  },
  //收藏
  favorite() {
    if(!this.getToken()){
      return false;
    }
    if (this.data.isreqc) {
      wx.showToast({
        title: '请勿点击太快',
        icon: 'none',
        duration: 1000
      });
      return;
    }
    if (this.data.activityInfo.itSme != 1) {
      if (!this.data.activityInfo.isCollect) {
        this.setData({
          'activityInfo.isCollect': this.data.activityInfo.id
        });
      } else {
        this.setData({
          'activityInfo.isCollect': null
        });
      }
      this.data.isreqc = true;
      api.activity_collect({
        activityId: this.data.activityInfo.id
      }).then(res => {
        if (res.code === 200) {}
        this.data.isreqc = false;
      });
    } else {
      wx.showToast({
        title: '自己发布的活动不能收藏哦~',
        icon: 'none',
        duration: 1000
      });
    }
  },

  //去报名
  toApply() {
    let userId = wx.getStorageSync('userId');
    if (!userId) {
      console.log('未登录')
      wx.showModal({
        title: '您未登录',
        content: '是否前往登录？',
        complete: (res) => {
          if (res.cancel) {
            // 关闭apply-popup弹窗
            this.setData({ applyPopupVisible: false });
          }
          if (res.confirm) {
            wx.navigateTo({ url: '/pages/login/login', })
          }
        }
      })
    }

    if (this.data.activityInfo.atFull == 1) {
      wx.showToast({
        title: '当前活动人数已满哦~',
        icon: 'none',
        duration: 1000
      });
      return;
    }
    const sex = this.data.activityInfo.condit.sex;
    if (sex) {
      if (sex === "限女生") {
        if (this.data.userInfo.mySex == 1) {
          wx.showToast({
            title: '只允许女生报名哦~',
            icon: 'none',
            duration: 1500
          });
          return;
        }
      }
      if (sex === "限男生") {
        if (this.data.userInfo.mySex == 2) {
          wx.showToast({
            title: '只允许男生报名哦~',
            icon: 'none',
            duration: 1500
          });
          return;
        }
      }
    }
    const grade = this.data.activityInfo.condit.grade;
    if (grade) {
      if (grade != this.data.userInfo.myAuthenCont) {
        wx.showToast({
          title: '只允许' + grade + '报名哦~',
          icon: 'none',
          duration: 1500
        });
        return;
      }
    }
    this.setData({
      applyPopupVisible: true,
      'apply.activityId': this.data.activityInfo.id,
      'apply.category': this.data.activityInfo.category,
      'apply.theme': this.data.activityInfo.theme,
      'apply.activityStart': this.data.activityInfo.activityStart,
      'apply.activityEnd': this.data.activityInfo.activityEnd,
      'apply.locationName': this.data.activityInfo.locationName,
      'apply.locationAddress': this.data.activityInfo.locationAddress,
      'apply.conditArray': this.data.activityInfo.conditArray
    });
  },

  //报名提交
  async apply_submit(e) {
    if(!await this.getToken()) {
      return false;
    }
    if (this.data.isSubmitting) {
      wx.showToast({
        title: '请不要重复点击',
        icon: 'none',
        duration: 1000
      });
      return;
    }
    this.data.isSubmitting = true;
    const apply = e.detail;
    const that = this;
    api.activity_apply(apply).then(res => {
      if (res.code == 200) {
        this.data.isSubmitting = false;
        if (res.data == 2) {
          // 若报名时已认证
          that.setData({
            'activityInfo.isApply': 0,
            'activityInfo.isCancelApply': 1,
          });

          // 报名成功，可供复制主办方联系方式的弹窗
          const title = '报名成功';
          const content = '已将您的信息发送给主办方，请及时赴约哦；\n主办方' + this.data.activityInfo.contactType + '：' + this.data.activityInfo.contact;
          this.showUserInfo(that, title, content)

        } else if (res.data == 1) {
          // 若报名时尚未认证
          that.setData({
            'activityInfo.isApply': 0,
            'activityInfo.isCancelApply': 1,
          });
          wx.showModal({
            title: '报名成功，待生效',
            content: '请在活动开始的12小时之前完成校园认证，以确保报名生效，是否前往认证？',
            cancelText: '取消',
            confirmText: '前往认证',
            success: function (respone) {
              if (respone.confirm) {
                that.getActInfo();
                that.toUserAuthen();
              } else if (respone.cancel) {
                that.apply_close();
              }
            }
          });


        } else if (res.data == 0) {
          wx.showModal({
            title: res.msg,
            content: '报名失败',
            showCancel: false
          });
        }
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none',
          duration: 1000
        });
      }
    });
    setTimeout(() => {
      this.data.isSubmitting = false;
    }, 3000);
  },
  apply_close() {
    this.setData({ applyPopupVisible: false });
  },

  //取消报名
  async toCancelApply() {
    if(!await this.getToken()) {
      return false;
    }
    wx.showModal({
      title: '温馨提示',
      content: '确认取消报名？\n若活动尚有名额\n取消后可再次报名',
      success: (res) => {

        if (res.confirm) {
          api.activity_apply_cancel({
            activityId: this.data.activityInfo.id
          }).then(res => {

            if (res.code == 200) {
              this.setData({
                'activityInfo.isApply': 1,
                'activityInfo.isCancelApply': 0
              });
              this.getActInfo();
              wx.showModal({
                title: '温馨提示',
                content: '活动主办方将会收到通知\n若有疑问，请及时与主办方联系',
                showCancel: false
              });

            } else {
              wx.showToast({
                title: '取消报名失败',
                icon: 'none',
                duration: 2000
              });
            }
          }).catch(error => {
            console.log(error)
            wx.showToast({
              title: '请求失败',
              icon: 'none',
              duration: 2000
            });
          });
        }
      }
    });
  },

  //去编辑
  toEdit() {
    let activityInfoStr = JSON.stringify(this.data.activityInfo);
    wx.navigateTo({
      url: `/pages/activity/add/add?activityInfo=${encodeURIComponent(activityInfoStr)}`
    });
  },

  // 若报名时未认证，与页面组件交互跳转到认证页面
  toUserAuthen() {
    wx.navigateTo({ url: '/pages/index/setting/authen/authen' });
  },

  // 若报名时已认证，在横幅上点击查看主办方联系方式跳出弹窗
  toUserInfo() {
    const that = this;
    const title = '已报名活动';
    const content = '主办方' + this.data.activityInfo.contactType + '：' + this.data.activityInfo.contact;
    this.showUserInfo(that, title, content);
  },

  // 若用户为活动发起者，跳转到活动参加者展示的applicants自定义组件
  toApplicantsInfo() {
    // TODO 目前使用的applyPeople列表缺少学校字段，需要修改请求api
    // api.activity_return_applicants({
    //   activityId: this.data.activityInfo.id
    // }).then(res => {
    //   console.log(res)
    //   this.setData({
    //     applicants: res,
    //     applicantsVisible: true,
    //   })
    // })

    this.setData({
      applicants: this.data.activityInfo.applyPeople,
      applicantsVisible: true,
    })

  },

  // 点击灰色蒙版退出活动参与者组件
  applicants_close() {
    this.setData({ applicantsVisible: false });
  },


  onShareAppMessage() {
    return {
      title: '分享活动' + this.data.activityInfo.theme,
      path: 'pages/activity/detail/detail?isshare=1&activityId=' + this.data.activityId
    }
  },
  backHome() {
    this.setData({
      isshare: false
    })
    wx.switchTab({
      url: '/pages/home/home'
    })
  },
  //公众号文章型活动
  articleRequest(){
    const url = this.data.activityInfo.wxUrl;
    if(url == null)
    {
      console.log("非公众号类文章");
      return;
    }
    wx.request({
      url: this.data.activityInfo.wxUrl,
      method:'GET',
      timeout: 5000,
      success:res=>{
        var html = res.data
        const start = res.data.indexOf('<div id="img-content"')
        const end = res.data.indexOf('<div id="js_tags_preview_toast"')
        html = html .slice(start,end)
        html = html.replace('visibility: hidden; opacity: 0; ','').replaceAll('data-src','src')
        // 使用正则表达式移除不需要的部分
        html = html.replace(/<h1[^>]*?>[\s\S]*?<\/h1>/g, '');  // 删除 <h1> 标签及其中的所有内容
        // 移除“原创”部分
        html = html.replace(/<span[^>]*?class="[^"]*?rich_media_meta[^"]*?"[^>]*?>[\s\S]*?<\/span>/g, '');
        // 移除“大学城 不咕嘟”部分
        html = html.replace(/<a[^>]*?id="js_name"[^>]*?>[\s\S]*?<\/a>/, '');

        // console.log(html)
        wx.showToast({
          title: '操作成功',
          icon: 'success',  // 可以选择 'success', 'loading' 或 'none'
          duration: 2000    // 提示显示时间（毫秒）
        })
        
        this.setData({
          wxcontent:html,
          loading:false
        })
      },
      fail: err => {
        console.error('请求失败:', err);
        wx.showToast({
          title: '请求超时，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        
        this.setData({
          loading: false
        });
      }
    })
  },
})