const envConf = {
  //开发版-本地环境
  develop: {
      mode: 'dev',
      DEBUG: false,
      // VCONSOLE: false,
      // appid: 'wx4729b3e165aa4e60',
      // API_BASE_URL: 'http://localhost:8080/api',
      appid: 'wxf2c08388dc2455bf',
      API_BASE_URL: 'https://bugudutechsz.cn/api',
  },

  //体验环境
  trial: {
      mode: 'test',
      DEBUG: false,
      VCONSOLE: false,
      appid: 'wxf2c08388dc2455bf',
      API_BASE_URL: 'https://bugudutechsz.cn/api',
  },

  //正式环境
  release: {
      mode: 'prod',
      DEBUG: false,
      VCONSOLE: false,
      appid: 'wxf2c08388dc2455bf',
      API_BASE_URL: 'https://bugudutechsz.cn/api',
  }
}
module.exports = {
  env: envConf[wx.getAccountInfoSync().miniProgram.envVersion]
}
