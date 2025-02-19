const appInstance = getApp();

//发送网络请求的封装函数
const request = (options) => {
  return new Promise((resolve, reject) => {
    beforeRequest(options);
    wx.request({
      ...options,
      method: "POST",
      // url: `${appInstance.globalData.API_BASE_URL}/${options.url}`,      
      url: `https://bugudutechsz.cn/api/${options.url}`,
      // url: `http://localhost:8080/api/${options.url}`,
      success: function (res) {
        afterResponse(res).then(resolve).catch(reject);
      },
      fail: function (err) {
        wx.showToast({ title: '请求服务器失败', icon: 'none', duration: 1000 });
      },
    });
  });
};


// 定义 fileUpload 函数
const myUpload = (url, fileName, filePath, formData) => {
  return new Promise((resolve, reject) => {
    const header = {};
    beforeRequest(header, true);
    wx.uploadFile({
      ...header,
      method: "POST",
      url: `https://bugudutechsz.cn/api/${url}`,
      // url: `${appInstance.globalData.API_BASE_URL}/${url}`,
      name: fileName,
      filePath: filePath,
      formData: formData,
      success: function (res) {
        afterResponse_Upload(res).then(resolve).catch(reject);
      },
      fail: function (err) {
        wx.showToast({ title: '请求服务器失败', icon: 'none', duration: 1000 });
      },
    });
  });
};

// const myUpload = (url, fileName, filePath, formData) => {
//   return new Promise((resolve, reject) => {
//     const header = {};
//     beforeRequest(header, true); // 设置请求头
    
//     wx.uploadFile({
//       ...header,
//       method: "POST",
//       url: `https://bugudutechsz.cn/api/${url}`,
//       name: fileName,
//       filePath: filePath,
//       formData: formData,
      
//       success: function (res) {
//         afterResponse_Upload(res).then(resolve).catch(reject);
//       },
//       fail: function (err) {
//         const errorMsg = err.errMsg || '请求服务器失败';
//         wx.showToast({ title: errorMsg, icon: 'none', duration: 1000 });
//         reject(err);  // 确保 Promise 在失败时被 reject
//       },
//     });
//   });
// };



const beforeRequest = (options, isMultipart = false) => {
  options = options || {};
  options.header = options.header || {};
  options.header['CLIENT_FLAG'] = 'SMALL_WEB';
  options.header['Content-Type'] = isMultipart ? 'multipart/form-data' : 'application/json';
  const token = wx.getStorageSync('token');
  if (token) {
    options.header['Authorization'] = token;
  }
};

const afterResponse = (response) => {
  return new Promise((resolve, reject) => {
    if (response.statusCode === 200) {
      switch (response.data.code) {
        case 401:
          wx.reLaunch({ url: '/pages/home/home' });
          // wx.showToast({ title: response.data.msg, icon: 'none', duration: 1000 });
          return;
      }
      resolve(response.data);
    }
  });
};


const afterResponse_Upload = (response) => {
  return new Promise((resolve, reject) => {
    if (response.statusCode === 200) {
      var pd = JSON.parse(response.data);
      console.log(pd)
      switch (pd.code) {
        case 401:
          wx.reLaunch({ url: '/pages/home/home' });
          // wx.showToast({ title: pd.msg, icon: 'none', duration: 1000 });
          return;
      }
      resolve(pd);
    }
  });
};




module.exports = {
  myUpload,
  request
};