import axios from 'axios';
import { getToken, removeAll } from '@/utils/localstore';
import { ElMessage } from 'element-plus'

const baseurl = import.meta.env.VITE_APP_API;
const requestApi = axios.create({
    baseURL: baseurl,
    timeout: 30*1000,  //请求超时时间30秒
});

requestApi.interceptors.request.use(
    config => {
        var token = getToken();
        if (token) {
            config.headers.Authorization = token;
            config.headers.CLIENT_FLAG = 'SMALL_PC';
        }
        return config;
    }, error => {
        return Promise.reject(error);
    }
);

//返回拦截器
requestApi.interceptors.response.use(function (response) {
    if (response.data.code == 401) {
        ElMessage.error({ message: response.data.msg, duration: 1200 });
        removeAll();
        window.router.replace({ path: '/login' });
        return null;
    }
    return response.data;
}, error => {
    if (error == 'Error: Network Error' || error == 'Error: Request failed with status code 500') {
        ElMessage.error({ message: '服务器连接失败', duration: 1200 });
    }
    return Promise.reject(error);
});

export default requestApi;
