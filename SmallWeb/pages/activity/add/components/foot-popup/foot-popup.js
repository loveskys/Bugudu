Component({
  properties: {
    condit: {
      type: Object,
      value: {}
    }
  },
  data: {
    gradeArray: ["大一","大二","大三","大四","研究生","博士"],
    gradeIndex: 0,

    sexArray: ["不限男女","限男生", "限女生"],
    sexIndex: 0,

    condit: {},
    conditArray: []
  },
  attached () {
    if(this.properties.condit) {
      this.setData({ condit: this.properties.condit });
    }
  },
  methods: {
    bindGradeSelect(e) {
      this.setData({  gradeIndex: e.detail.value });
      this.setData({ 'condit.grade': this.data.gradeArray[e.detail.value] });
    },
    bindSexSelect(e) {
      this.setData({ sexIndex: e.detail.value });
      this.setData({ 'condit.sex': this.data.sexArray[e.detail.value] });
    },
    bindConditKeyInput(e) {
      const key = e.currentTarget.dataset.key, value = e.detail.value;
      this.setData({ [key]: value });
    },

    submitCondit() {
      if (this.data.condit == undefined || !this.data.condit.peopleNum) {
        wx.showToast({ title: '请选择活动人数', icon: 'none', duration: 1000 });
        return;
      }
      const peopleNum = this.data.condit.peopleNum;
      const isPositiveInteger = /^\d+$/.test(peopleNum);
      if (!isPositiveInteger || peopleNum==0) {
        wx.showToast({ title: '人数必须填阿拉伯数字', icon: 'none', duration: 1000 });
        return;
      }
      if(peopleNum > 199) {
        wx.showToast({ title: '活动人数不能大于199人', icon: 'none', duration: 1000 });
        return;
      }

      this.data.conditArray = []
      if (this.data.condit.peopleNum) {
        this.data.conditArray.push(this.data.condit.peopleNum+'人');
      } if (this.data.condit.grade) {
        this.data.conditArray.push(this.data.condit.grade);
      } if(this.data.condit.sex) {
        this.data.conditArray.push(this.data.condit.sex);
      } if (this.data.condit.other) {
        this.data.conditArray.push(this.data.condit.other);
      }
      const combinedData = { condit: this.data.condit, conditArray: this.data.conditArray };
      this.triggerEvent('submitCondit', combinedData);
      this.closeCondit();
    },

    closeCondit() {
      this.triggerEvent('closeCondit');
    }
  }
})