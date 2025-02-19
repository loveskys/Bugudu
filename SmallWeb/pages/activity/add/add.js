import api from '../../../utils/api.js';

// 选择图片
function chooseImage(context, imageKey) {
  wx.chooseMedia({
    count: 1,
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
    camera: 'back',
    success(res) {
      const tempFile = res.tempFiles[0].tempFilePath;
      const tempSize = res.tempFiles[0].size;

      // 临时设置图片路径
      context.setData({
        [imageKey]: tempFile
      });

      // 获取图片信息
      wx.getImageInfo({
        src: tempFile,
        success: function () {
          if (tempSize > 1024 * 1024) {
            console.log("大于1M：" + tempSize);
            // 压缩图片
            wx.compressImage({
              src: tempFile,
              quality: 50,
              success: (compressedRes) => {
                const compressedTempFilePath = compressedRes.tempFilePath;
                context.setData({
                  [imageKey]: compressedTempFilePath
                });
              },
              fail: (error) => {
                console.error('压缩失败:', error);
              }
            });
          } else {
            console.log("小于1M：" + tempSize);
          }
        },
        fail: function (error) {
          console.error('获取图片信息失败:', error);
        }
      });
    }
  });
}

// 删除图片
function deleteImage(context, imageKey) {
  context.setData({
    [imageKey]: ''
  });
}

// 预览图片
function previewImage(context, currentImage, fallbackImage) {
  wx.previewImage({
    current: currentImage,
    urls: [currentImage || fallbackImage]
  });
}


Page({
  data: {
    activityInfo: {
      theme: "",
      category: "",
      intro: "",
      image: "",
      contactImage: "",
      checkingImage: "",
      contactType: "微信号",
      contact: "",
      location: {
        locationName: '', //位置名称
        locationAddress: '', //详细地址
        latitude: null, //纬度
        longitude: null, //经度
      }
    },

    activityImg: '',
    contactImg: '',
    checkingImg: '',
    submit_tt: 0,
    isSubmitting: false,
    isSubmitted: false, // 活动是否创建过，false为正在创建，true为正在编辑

    conditPopupVisible: false,
    contactArray: ["微信号", "手机号", "二维码"],
    contactIndex: 0,
    startdate: '',
    starttime: '',
    enddate: '',
    endtime: '',
  },

  onLoad(options) {
    if (options.selectIndex) {
      if (options.selectIndex == 1) {
        this.setData({
          'activityInfo.category': "娱乐"
        });
      }
      if (options.selectIndex == 2) {
        this.setData({
          'activityInfo.category': "公益"
        });
      }
      if (options.selectIndex == 3) {
        this.setData({
          'activityInfo.category': "运动"
        });
      }
      if (options.selectIndex == 4) {
        this.setData({
          'activityInfo.category': "学习"
        });
      }
    }
    if (options.activityInfo) {
      let activityInfoStr = decodeURIComponent(options.activityInfo);
      this.setData({
        activityInfo: JSON.parse(activityInfoStr),
        isSubmitted: true
      });
      // console.log(this.data.activityInfo)
    }
    this.setDefaultDates(); // 设置默认活动时间

  },

  onShow() {
    // 自定义tabBar页面刷新
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 1
      })
    }
  },


  // 设置默认活动时间
  // 如果是编辑活动状态，则从活动详情页面获取时间数据
  setDefaultDates() {
    // 如果默认活动时间不为空（即编辑活动状态）
    if (this.data.activityInfo.activityStart && this.data.activityInfo.activityEnd) {
      const [startdate, starttime] = this.data.activityInfo.activityStart.split(' ');
      const [enddate, endtime] = this.data.activityInfo.activityEnd.split(' ');

      this.setData({
        startdate: startdate,
        starttime: starttime,
        enddate: enddate,
        endtime: endtime
      });
    // 如果默认活动时间为空（即创建活动状态）
    } else {
      const today = new Date();
      const twoDaysLater = new Date(today.getTime() + 2 * 24 * 60 * 60 * 1000); // 两天后（毫秒数）

      function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      const defaultStartDate = formatDate(today);
      const defaultEndDate = formatDate(twoDaysLater);
      this.setData({
        startdate: defaultStartDate,
        enddate: defaultEndDate,
        starttime: '09:00',
        endtime: '15:00'
      });
    }
  },

  // 添加活动图片
  addActImage() {
    chooseImage(this, 'activityImg');
  },

  // 删除活动图片
  deleteImage() {
    deleteImage(this, 'activityImg');
    this.setData({
      'activityInfo.image': ''
    });
  },

  // 预览活动图片
  previewImage() {
    previewImage(this, this.data.activityImg, this.data.activityInfo.image);
  },

  // 添加联系方式图片
  addContactImage() {
    chooseImage(this, 'contactImg');
  },

  // 删除联系方式图片
  deleteContactImage() {
    deleteImage(this, 'contactImg');
    this.setData({
      'activityInfo.contactImage': ''
    });
  },

  // 预览联系方式图片
  previewContactImage() {
    previewImage(this, this.data.contactImg, this.data.activityInfo.contactImage);
  },

  // 添加审核表图片
  addCheckingImage() {
    chooseImage(this, 'checkingImg');
  },

  // 删除审核表图片
  deleteCheckingImage() {
    deleteImage(this, 'checkingImg');
    this.setData({
      'activityInfo.checkingImage': ''
    });
  },

  // 预览审核表图片
  previewCheckingImage() {
    previewImage(this, this.data.checkingImg, this.data.activityInfo.checkingImage);
  },


  //打开腾讯地图并显示位置,调用wx.chooseLocation()选择位置
  getLocation() {
    const that = this;
    wx.authorize({
      scope: 'scope.userLocation',
      success: function (res) {
        wx.chooseLocation({
          success: (res2) => {
            console.log("选择的位置: " + JSON.stringify(res2))
            that.setData({
              'activityInfo.location.locationName': res2.name,
              'activityInfo.location.locationAddress': res2.address,
              'activityInfo.location.latitude': res2.latitude,
              'activityInfo.location.longitude': res2.longitude,
            })
          }
        });
      },
      fail: function () {
        wx.showModal({
          title: '提示',
          content: '您拒绝了位置权限，请在打开位置权限以正常使用本功能',
          showCancel: false
        });
      }
    })
  },

  // 参加者要求弹窗
  condit_open() {
    this.setData({
      conditPopupVisible: true
    });
  },
  condit_select(e) {
    const condit = e.detail;
    this.setData({
      'activityInfo.condit': condit.condit,
      'activityInfo.conditArray': condit.conditArray
    });
  },
  condit_close() {
    this.setData({
      conditPopupVisible: false
    });
  },
  condit_delete(e) {
    const index = e.currentTarget.dataset.index;
    const updatedArray = [...this.data.activityInfo.conditArray];
    const updatedCondit = {
      ...this.data.activityInfo.condit
    };
    const deletedItem = updatedArray.splice(index, 1)[0];
    const keys = Object.keys(updatedCondit);
    keys.forEach(key => {
      let conditionValue = updatedCondit[key];
      if (key === 'peopleNum') {
        conditionValue += '人';
      }
      if (String(conditionValue) === String(deletedItem)) {
        delete updatedCondit[key];
      }
    });
    this.setData({
      'activityInfo.condit': updatedCondit,
      'activityInfo.conditArray': updatedArray
    });
    console.log('Deleted Item:', deletedItem);
    console.log('Updated Array:', updatedArray);
    console.log('Updated Condit:', updatedCondit);
  },

  startdateChange(e) {
    this.setData({
      startdate: e.detail.value
    });
    this.setData({
      'activityInfo.startDate': this.data.startdate
    });
  },
  starttimeChange(e) {
    this.setData({
      starttime: e.detail.value
    });
    this.setData({
      'activityInfo.startTime': this.data.starttime
    });
  },
  enddateChange(e) {
    this.setData({
      enddate: e.detail.value
    });
    this.setData({
      'activityInfo.endDate': this.data.enddate
    });
  },
  endtimeChange(e) {
    this.setData({
      endtime: e.detail.value
    });
    this.setData({
      'activityInfo.endTime': this.data.endtime
    });
  },
  bindPickerSelect(e) {
    this.setData({
      contactIndex: e.detail.value
    });
    this.setData({
      'activityInfo.contactType': this.data.contactArray[e.detail.value]
    });
  },

  bindKeyInput(e) {
    const key = e.currentTarget.dataset.key,
      value = e.detail.value;
    this.setData({
      [key]: value
    });
  },


  //处理数据
  clData() {
    this.data.isSubmitting = false;
    if (this.data.activityInfo.theme === '') {
      wx.showToast({
        title: '请填写活动标题',
        icon: 'none',
        duration: 1000
      });
      return false;
    }
    if (this.data.activityInfo.intro === '') {
      wx.showToast({
        title: '请填写活动描述',
        icon: 'none',
        duration: 1000
      });
      return false;
    }
    if (this.data.activityInfo.contact === '' && !this.data.contactImg) {
      wx.showToast({
        title: '请填写联系方式',
        icon: 'none',
        duration: 1000
      });
      return false;
    }
    if (this.data.activityInfo.location.latitude == null) {
      wx.showToast({
        title: '请选择活动地址',
        icon: 'none',
        duration: 1000
      });
      return false;
    }
    if (this.data.activityInfo.condit == undefined) {
      wx.showToast({
        title: '请添加人数',
        icon: 'none',
        duration: 1000
      });
      return false;
    }

    if (this.data.activityInfo.image == '' && this.data.activityImg == '') {
      this.data.submit_tt = 0;
      wx.showToast({
        title: '请上传活动照片',
        icon: 'none',
        duration: 1000
      });
      return;
    }
    if (this.data.activityInfo.image && this.data.activityImg == '') {
      this.data.submit_tt = 1;
    }

    this.setData({
      'activityInfo.startDate': this.data.startdate
    });
    this.setData({
      'activityInfo.startTime': this.data.starttime
    });
    this.setData({
      'activityInfo.endDate': this.data.enddate
    });
    this.setData({
      'activityInfo.endTime': this.data.endtime
    });

    console.log(this.data.activityInfo)
    return true;
  },

  //存草稿
  savedraft() {
    wx.showToast({
      title: '该功能待开发~',
      icon: 'none',
      duration: 1500
    });
    return;
  },

  //预览活动
  previewact() {
    if (this.clData()) {
      let activityInfo = this.data.activityInfo;
      activityInfo.image = this.data.activityImg ? this.data.activityImg : this.data.activityInfo.image;
      const encodedActivityInfo = encodeURIComponent(JSON.stringify(activityInfo));
      wx.navigateTo({
        url: `/pages/activity/preview/preview?activityInfo=${encodedActivityInfo}`
      });
    }
  },

  // 删除活动
  deleteActivity() {
    api.activity_delete({
      id: this.data.activityInfo.id
    }).then(res => {
      console.log(res)
      wx.showModal({
        title: '温馨提示',
        content: '活动一经删除无法恢复\n已报名的参与者将会收到通知\n确认删除活动？',
        complete: (res) => {
          if (res.cancel) {
            return;
          }

          if (res.confirm) {
            wx.showToast({
              title: '活动已删除',
              duration: 1500
            })
            setTimeout(() => {
              wx.navigateBack({ delta: 2 }); // 从add页面退回detail页面，再退回上一个页面
            }, 1500);

          }
        }
      })
    })
  },

  //发布
  submit() {
    if (this.data.isSubmitting) {
      wx.showToast({
        title: '请不要重复点击',
        icon: 'none',
        duration: 1000
      });
      return;
    }
    this.data.isSubmitting = true;


    if (this.clData()) {
      wx.showModal({
        title: '请确认发布',
        content: '审核会在1个工作日内完成',
        complete: (res) => {
          if (res.cancel) {

          }

          if (res.confirm) {
            wx.showLoading({
              title: '提交中',
              mask: true
            });
            if (this.data.submit_tt == 0) {
              const activityFilePath = this.data.activityImg; // 上传活动的文件路径  
              const contactFilePath = this.data.contactImg; // 上传联系方式的文件路径  

              const formData = {
                activityInfo: JSON.stringify(this.data.activityInfo)
              };
              // 首先执行 originate 请求  
              console.log('开始执行api.originate');
              console.log(activityFilePath);
              console.log(formData);
              api.originate(activityFilePath, formData)
                .then(response => {
                  console.log("进入response");
                  if (response.code === 200) {
                    console.log("originate执行成功");
                    // 如果 originate 成功，继续执行 originate_contact 请求  
                    if (contactFilePath) {
                      console.log('contacting...')
                      api.originate_contact(contactFilePath, formData)
                        .then(contactResponse => {
                          console.log(contactResponse)
                          if (contactResponse.code === 200) {
                            wx.reLaunch({
                              url: '/pages/home/home'
                            });
                            wx.hideLoading();
                            wx.showToast({
                              title: '操作成功',
                              icon: 'success',
                              duration: 1500
                            });
                          } else {
                            // 如果 originate_contact 失败，抛出错误  
                            wx.hideLoading();
                            throw new Error(contactResponse.msg || '未知错误');
                          }
                        })
                    } else {
                      console.log('else')
                      wx.reLaunch({
                        url: '/pages/home/home'
                      });
                      wx.hideLoading();
                      wx.showToast({
                        title: '操作成功',
                        icon: 'success',
                        duration: 1500
                      });
                    }
                    console.log('if success')
                  } else {
                    // 如果 originate 失败，抛出错误  
                    console.log(" originate 失败")
                    throw new Error(response.msg || '未知错误');
                  }
                })
                .catch(error => {
                  console.log("进入catch error");
                  wx.hideLoading();
                  this.data.isSubmitting = false;
                  wx.showToast({
                    title: error.message || '请求失败',
                    icon: 'none',
                    duration: 1000
                  });
                });
                console.log('api.originate执行完成');
            } else {
              api.reoriginate(this.data.activityInfo).then(respone => {
                if (respone.code == 200) {
                  wx.reLaunch({ url: '/pages/home/home' });
                  wx.hideLoading();
                  this.data.isSubmitting = false;
                  wx.showToast({ title: '更新成功', icon: 'success', duration: 1500 });
                } else {
                  wx.hideLoading();
                  this.data.isSubmitting = false;
                  wx.showToast({ title: respone.msg, icon: 'none', duration: 1000 });
                }
              }).catch((error) => {
                wx.hideLoading();
                this.data.isSubmitting = false;
                wx.showToast({ title: '请求失败', icon: 'none', duration: 1000 });
              });
            }
          }
        }
      })
    }






  },










  //发布
  // submit() {
  //   if (this.data.isSubmitting) {
  //     wx.showToast({
  //       title: '请不要重复点击',
  //       icon: 'none',
  //       duration: 1000
  //     });
  //     return;
  //   }
  //   this.data.isSubmitting = true;
  //   if (this.clData()) {
  //     wx.showModal({
  //       title: '请确认发布',
  //       content: '审核会在1个工作日内完成',
  //       success: (res) => {
  //         if (res.confirm) {
  //           this.data.isSubmitting = false;
  //           wx.showLoading({
  //             title: '提交中',
  //             mask: true
  //           });
  //           if (this.data.submit_tt == 0) {
  //             let id = this.data.activityInfo.id;
  //             api.originate(this.data.activityImg, {
  //               activityInfo: JSON.stringify(this.data.activityInfo)
  //             }).then(respone => {
  //               if (respone.code == 200) {
  //                 this.data.isSubmitting = false;
  //                 wx.reLaunch({
  //                   url: '/pages/home/home'
  //                 });
  //                 wx.hideLoading();
  //                 wx.showToast({
  //                   title: id ? '更新成功' : '发布成功',
  //                   icon: 'success',
  //                   duration: 1500
  //                 });
  //               } else {
  //                 wx.hideLoading();
  //                 this.data.isSubmitting = false;
  //                 wx.showToast({
  //                   title: respone.msg,
  //                   icon: 'none',
  //                   duration: 1000
  //                 });
  //               }
  //             }).catch((error) => {
  //               wx.hideLoading();
  //               this.data.isSubmitting = false;
  //               wx.showToast({
  //                 title: '请求失败',
  //                 icon: 'none',
  //                 duration: 1000
  //               });
  //             });
  //           }

  //           if (this.data.submit_tt == 1) {
  //             api.reoriginate(this.data.activityInfo).then(respone => {
  //               if (respone.code == 200) {
  //                 wx.reLaunch({
  //                   url: '/pages/home/home'
  //                 });
  //                 wx.hideLoading();
  //                 this.data.isSubmitting = false;
  //                 wx.showToast({
  //                   title: '更新成功',
  //                   icon: 'success',
  //                   duration: 1500
  //                 });
  //               } else {
  //                 wx.hideLoading();
  //                 this.data.isSubmitting = false;
  //                 wx.showToast({
  //                   title: respone.msg,
  //                   icon: 'none',
  //                   duration: 1000
  //                 });
  //               }
  //             }).catch((error) => {
  //               wx.hideLoading();
  //               this.data.isSubmitting = false;
  //               wx.showToast({
  //                 title: '请求失败',
  //                 icon: 'none',
  //                 duration: 1000
  //               });
  //             });
  //           }

  //         } else if (res.cancel) {
  //           wx.hideLoading();
  //           this.data.isSubmitting = false;
  //         }

  //       }
  //     });
  //   }
  // },
});