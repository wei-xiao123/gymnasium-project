import axios from "axios";
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import { ElMessage } from "element-plus";
import { userStore } from "@/store/user";

// axios 请求配置
const config = {
  // baseURL: "http://localhost:8089", // 真实请求接口的地址
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  withCredentials: true //解决sesson不一致的问题
};

// 定义返回值类型
export interface Result<T = any> {
  code: number;
  msg: string;
  data: T;
}

class Http {
  // Axios 实例
  private instance: AxiosInstance;

  // 构造函数初始化
  constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config);
    // 定义拦截器
    this.interceptors();
  }

  // 请求/响应拦截器
  private interceptors() {
  // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        // 在请求头部携带 token
        const token = userStore().getToken()
        if (token) {
          config.headers["token"] = token
        }
        return config
      },
      (error: any) => {
        error.data = {};
        error.data.msg = "服务器异常，请联系管理员!";
        return error;
      }
    );

    // 响应拦截器
    this.instance.interceptors.response.use(
      (res: AxiosResponse) => {
        // 直接返回响应数据，由调用方处理业务逻辑
        // 不在这里弹窗提示
        return res.data;
      },
      (error) => {
        // 只处理网络错误，此时才弹窗
        let errorMsg = "连接到服务器失败";
        
        if (error && error.response) {
          switch (error.response.status) {
            case 400:
              errorMsg = "错误请求";
              break;
            case 401:
              errorMsg = "未授权，请重新登录";
              break;
            case 403:
              errorMsg = "拒绝访问";
              break;
            case 404:
              errorMsg = "请求错误，未找到该资源";
              break;
            case 405:
              errorMsg = "请求方法未允许";
              break;
            case 408:
              errorMsg = "请求超时";
              break;
            case 500:
              errorMsg = "服务器端出错";
              break;
            case 501:
              errorMsg = "网络未实现";
              break;
            case 502:
              errorMsg = "网络错误";
              break;
            case 503:
              errorMsg = "服务不可用";
              break;
            case 504:
              errorMsg = "网络超时";
              break;
            case 505:
              errorMsg = "HTTP 版本不支持该请求";
              break;
            default:
              errorMsg = error.response.data?.msg || `连接错误${error.response.status}`;
          }
        } else if (error.request) {
          // 请求已发送但未收到响应
          errorMsg = "未收到服务器响应";
        }
        
        ElMessage.error(errorMsg);
        return Promise.reject(errorMsg);
      }
    );
  }

  // RESTful API 封装

  // GET 方法
  get<T = Result>(url: string, params?: object): Promise<T> {
    return this.instance.get(url, { params });
  }

  // POST 方法
  post<T = Result>(url: string, data?: object): Promise<T> {
    return this.instance.post(url, data);
  }

  // PUT 方法
  put<T = Result>(url: string, data?: object): Promise<T> {
    return this.instance.put(url, data);
  }

  // DELETE 方法
  delete<T = Result>(url: string): Promise<T> {
    return this.instance.delete(url);
  }

  // 图片上传
  upload<T = Result>(url: string, params?: object): Promise<T> {
    return this.instance.post(url, params, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });
  }
}

export default new Http(config);