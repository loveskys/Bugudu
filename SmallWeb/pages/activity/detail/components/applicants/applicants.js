Component({
  properties: {
    // 从父组件获取活动参加者信息
    "applicants": {
      "type": "Array",
      "value": []
    }
  },

  data: {
    applicantsData: []
  },

  attached() {
    if (this.properties.applicants && this.properties.applicants.length > 0) {
      // 删除第一条数据，只保留后面的部分
      this.setData({ applicantsData: this.properties.applicants.slice(1) });
    }
  },

  methods: {

    // 透明灰色背景蒙版，点击退出组件
    closeApplicants() {
      this.triggerEvent('closeApplicants');
    },

    // 查看活动参加者用户主页
    toUserPreview(e) {
      const index = e.currentTarget.dataset.index
      wx.navigateTo({
        url: `/pages/index/my/preview/preview?userId=${this.data.applicantsData[index].userId}`
      });
    },

    // 复制联系方式
    copyContact(e) {
      const index = e.currentTarget.dataset.index
      wx.setClipboardData({
        data: this.data.applicantsData[index].contact,
        success: function () {
          wx.getClipboardData({
            success: function (respone2) {
              wx.showToast({
                title: '已复制到剪贴板',
                icon: 'none',
                duration: 1000
              });
            }
          });
        }
      });
    },
  }
})