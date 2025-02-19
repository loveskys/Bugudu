Component({
  properties: {
    apply: {
      type: Object,
      value: {}
    }
  },
  data: {
    applyData: {},
  },
  attached () {
    if(this.properties.apply) {
      this.setData({ applyData: this.properties.apply });
    }
  },
  methods: {
    bindConditKeyInput(e) {
      const key = e.currentTarget.dataset.key, value = e.detail.value;
      this.setData({ [key]: value });
    },

    submitApply() {
      if (!this.data.applyData.contact) {
        wx.showToast({ title: '请填写联系方式', icon: 'none', duration: 1000 });
        return;
      }
      if (!this.data.applyData.nickName) {
        wx.showToast({ title: '请填写您的称呼', icon: 'none', duration: 1000 });
        return;
      }

      this.triggerEvent('submitApply', this.data.applyData);
    },

    closeApply() {
      this.triggerEvent('closeApply');
    }
  }
})