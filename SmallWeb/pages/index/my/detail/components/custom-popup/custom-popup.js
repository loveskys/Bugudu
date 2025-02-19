Component({
  properties: {
    title: {
      type: String,
      value: ''
    },
    options: {
      type: Array,
      value: []
    },
    isSingleSelect: {
      type: Boolean,
      value: false
    },
    maxSelections: {
      type: Number,
      value: 5
    }
  },

  data: {
    selectedItems: [],
  },

  methods: {
    handleOptionTap(e) {
      const index = e.currentTarget.dataset.index;
      const item = this.properties.options[index];
      if (this.properties.isSingleSelect) {
        this.setData({ selectedItems: [item] });
      } else {
        if (this.data.selectedItems.includes(item)) {
          const indexToRemove = this.data.selectedItems.indexOf(item);
          this.data.selectedItems.splice(indexToRemove, 1);
        } else if (this.data.selectedItems.length < this.properties.maxSelections) {
          this.data.selectedItems.push(item);
        } else if (this.data.selectedItems.length == this.properties.maxSelections) {
          wx.showToast({ title: '最多选择'+this.properties.maxSelections+'项', icon: 'none', duration: 1000 });
          return;
        }
        this.setData({ selectedItems: this.data.selectedItems.slice()});
      }
    },

    submitSelections() {
      if (this.data.selectedItems.length === 0) {
        wx.showToast({ title: '至少选择一项', icon: 'none', duration: 1000 });
        return;
      }
      this.triggerEvent('selectionsSubmitted', { selectedItems: this.data.selectedItems });
      this.closePopup();
    },

    closePopup() {
      this.triggerEvent('popupClosed');
    }
  }
})