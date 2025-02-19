Component({
  
  data: {
    selected: 0,
    "list": [{
      // "pagePath"属性在这里需要加一个开头的斜杠/表示根目录相对路径，而app.json中的"pagePath"属性却不能加开头的斜杠
        "pagePath": "/pages/home/home",
        "iconPath": "/images/home/home.png",
        "selectedIconPath": "/images/home/home_select.png",
        "text": "首页"
      },
      {
        "pagePath": "/pages/activity/selectcenter/selectcenter",
        "iconPath": "/images/home/add.png",
        "selectedIconPath": "/images/home/add_select.png",
        "text": "发布活动"
      },
      {
        "pagePath": "/pages/index/my/my",
        "iconPath": "/images/home/my.png",
        "selectedIconPath": "/images/home/my_select.png",
        "text": "个人"
      }
    ],
    "color": "#000000",
    "backgroundColor": "#F0F0F0",
    "borderStyle": "white",
    "selectedColor": "#FF9500"

  },

  attached() {
  },

  methods: {
    switchTab(e) {
      const newData = e.currentTarget.dataset
      wx.switchTab({url:newData.path})
      this.setData({
        selected: newData.index
      })
    }
  }
})