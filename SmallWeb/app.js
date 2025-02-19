import config from './env.config.js';
import api from './utils/api.js';

App({
  onLaunch: function () {
    console.log("App Launch");
 
    // 检查并创建文件
    const fs = wx.getFileSystemManager();
    const logDirPath = `${wx.env.USER_DATA_PATH}/miniprogramLog`;
    const logFiles = ['log1'];

    // // 创建目录
    // fs.access({
    //     path: logDirPath,
    //     success: () => {
    //         console.log('Directory exists');
    //         checkAndCreateFiles();
    //     },
    //     fail: () => {
    //         console.log('Directory does not exist, creating it');
    //         fs.mkdir({
    //             dirPath: logDirPath,
    //             success: () => {
    //                 console.log('Directory created successfully');
    //                 checkAndCreateFiles();
    //             },
    //             fail: err => {
    //                 console.error('Failed to create directory', err);
    //             }
    //         });
    //     }
    // });
    // function checkAndCreateFiles() {
    //     logFiles.forEach(logFile => {
    //         const logFilePath = `${logDirPath}/${logFile}`;
    //         fs.access({
    //             path: logFilePath,
    //             success: () => {
    //                 console.log(`${logFile} exists`);
    //             },
    //             fail: () => {
    //                 console.log(`${logFile} does not exist, creating it`);
    //                 fs.writeFile({
    //                     filePath: logFilePath,
    //                     data: '',
    //                     success: res => {
    //                         console.log(`${logFile} created successfully`);
    //                     },
    //                     fail: err => {
    //                         console.error(`Failed to create ${logFile}`, err);
    //                     }
    //                 });
    //             }
    //         });
    //     });
    // }
    // this.globalData.API_BASE_URL = config.env.API_BASE_URL;
    console.log('登录小程序');

    // 获取当前运行环境版本
    const envVersion = wx.getAccountInfoSync().miniProgram.envVersion;
    let currentEnv;
    if (envVersion === 'develop') {
      currentEnv = '开发版';
    } else if (envVersion === 'trial') {
      currentEnv = '测试版';
    } else if (envVersion === 'release') {
      currentEnv = '正式版';
    }

    console.log('当前环境:', currentEnv+"-"+envVersion);
  },

  onShow: function() {

  },

  globalData: {
    userInfo: null,
    API_BASE_URL: 'https://bugudutechsz.cn/api'
  }
});