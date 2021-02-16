import axios from 'axios'
import {Message} from "element-ui";
import router from "../router";
axios.defaults.baseURL = "http://localhost:8081"
//请求拦截器
// axios.interceptors.request.use(config => {
//     //如果存在token，请求携带这个token
//     if (window.sessionStorage.getItem('tokenStr')) {
//         config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
//     }
//     return config;
// }, error => {
//     console.log(error);
// })

//响应拦截器
axios.interceptors.response.use(success => {
    if (success.status && success.status == 200) {
        if (success.data.code == 500 || success.data.code == 401 || success.data.code == 403 || success.data.code == 400) {
            Message.error({message: success.data.msg});
            return success.data;
        }
    }
    return success;
}, error => {
    if (error.response.code == 504 || error.response.code == 404) {
        Message.error({message: '服务器被吃了'});
    } else if (error.response.code == 403) {
        Message.error({message: '权限不足'});
    } else if (error.response.code == 401) {
        Message.error({message: '请先登录'});
        router.replace('/')
    } else {
        if (error.response.data.message) {
            Message.error({message: error.response.data.message});
        } else {
            Message.error({message: '未知错误'});
        }
    }
    return;
});

let base = '';
export const postRequest = (url, parms) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: parms
    })
};

export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
};
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
};
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
};
