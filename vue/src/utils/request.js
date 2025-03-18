import axios from "axios"
import {ElMessage} from "element-plus";

const request = axios.create({
    baseURL: 'http://localhost:9999',
    timeout: 30000
})

request.interceptors.request.use(config =>{
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("code_user") || '{}')
    config.headers['token'] = user.token
    return config;
}, error => {
    return Promise.reject(error)
})

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res == 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response && error.response.status) {
            if (error.response.status === 404) {
                ElMessage.error('未找到请求接口');
            } else if (error.response.status === 500) {
                ElMessage.error('系统异常，请查看后端控制台报错');
            } else {
                console.error(error.message);
            }
        } else {
            ElMessage.error("网络异常，请重试");
        }
        return Promise.reject(error);    }
)

export default request