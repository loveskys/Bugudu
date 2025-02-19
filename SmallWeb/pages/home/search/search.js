import api from '../../../utils/api.js';

Page({
  data: {
    searchText: '',
    plateIndex: 1,
    userList: [],
    activityList: [],
    historyArray: [],
    showHistory: false,
  },

  onFocus(e) {
    this.setData({ showHistory: true });
    this.getSearchHistory();
  },
  onBlur(e) {
    setTimeout(() => { this.setData({ showHistory: false }) }, 200);
  },
  onConfirm() {
    this.getSearchHistory();
    this.toSearch()
  },


  toPlateTag(e) {
    this.setData({ plateIndex: parseInt(e.currentTarget.dataset.index) });
    this.toSearch()
  },

  getSearchHistory() {
    api.get_search_history({searchText: this.data.searchText}).then(response => {
      if (response.code === 200) {
        this.setData({ historyArray: response.data });
      }
    });
  },

  to_History_Text_Search(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ searchText: this.data.historyArray[index].searchText });
    this.setData({ plateIndex: parseInt(this.data.historyArray[index].searchType) });
    this.toSearch();
  },

  //进行搜索
  toSearch() {
    const searchText = this.data.searchText.trim();
    console.log('-->'+this.data.searchText+'<--and-->'+searchText+'<--and-->'+!searchText+'<--', 'searching')
    if (!searchText) {
      // 如果搜索文本为空，则不执行搜索，并清空搜索结果
      this.setData({
        activityList: [],
        userList: []
      });
      wx.hideLoading();
      console.log('return')
      return;
    }
    wx.showLoading({ title: '搜索中' });
    api.to_search({
      searchText: this.data.searchText,
      searchType: this.data.plateIndex
    }).then(response => {
      if (response.code === 200) {
        if (this.data.plateIndex == 1) {
          this.setData({ activityList: response.data });
        } else if (this.data.plateIndex == 2) {
          this.setData({ userList: response.data });
          wx.hideLoading();
        }
      } else {
        this.setData({ activityList: [] });
        this.setData({ userList: [] });
        wx.hideLoading();
      }
    });
    setTimeout(() => {
      wx.hideLoading();
    }, 1000);
  },

  toActivityItem(e) {
    const index = e.currentTarget.dataset.index;
    wx.navigateTo({url: `/pages/activity/detail/detail?activityId=${this.data.activityList[index].id}`});
  },
  toUserPreview(e) {
    const index = e.currentTarget.dataset.index;
    wx.navigateTo({url: `/pages/index/my/preview/preview?userId=${this.data.userList[index].studentUser.id}`});
  },

})