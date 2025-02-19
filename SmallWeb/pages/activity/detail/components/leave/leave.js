import api from '../../../../../utils/api.js';

Component({
  properties: {
    activityId: {
      type: String,
      value: ''
    }
  },
  data: {
    activityId: "",
    leaveCont: "",
    isSubmitting: false
  },
  attached () {
    if(this.properties.activityId) {
      this.setData({ activityId: this.properties.activityId });
    }
  },
  methods: {
    submitSendLeave() {
      if (!this.data.leaveCont) {
        wx.showToast({ title: '请填写留言内容', icon: 'none', duration: 1000 });
        return;
      }
      if (this.data.isSubmitting) {
        wx.showToast({ title: '请不要点击过快', icon: 'none', duration: 1000 });return;
      }

      this.data.isSubmitting = true;
      api.activity_send_leave({ activityId: this.data.activityId, leaveCont: this.data.leaveCont  }).then(res => {
        if (res.code === 200) {
          wx.showToast({ title: '留言成功', icon: 'none', duration: 1000 });
          this.data.isSubmitting = false;
          this.closeSendLeave();
        } else {
          this.data.isSubmitting = false;
          wx.showToast({title: res.msg, icon: 'none', duration: 1000});
        }
        this.data.isSubmitting = false;
      });
    },

    closeSendLeave() {
      this.triggerEvent('closeSendLeave');
      this.data.isSubmitting = false;
    }
  }
})