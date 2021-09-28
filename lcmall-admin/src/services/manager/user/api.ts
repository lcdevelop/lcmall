// @ts-ignore
/* eslint-disable */
import { request } from 'umi';
import {op, OP_GET, OptionsType, ParamsType} from "@/services/manager/restfulapi";

const userApiPath = '/api/manager/user/user';

export async function appList(
  options?: { [key: string]: any },
) {
  return request<API.Response<API.WxApp[]>>('/api/manager/appList', {
    method: 'GET',
    ...(options || {}),
  });
}

export async function checkLogined(
  options?: { [key: string]: any },
) {
  return request<API.Response<API.WxUser>>('/api/manager/user/checkLogined', {
    method: 'GET',
    ...(options || {}),
  });
}

export async function currentUser(options?: { [key: string]: any }) {
  return request<API.Response<API.WxUser>>('/api/manager/user/currentUser', {
    method: 'GET',
    ...(options || {}),
  });
}

export async function outLogin(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/manager/user/outLogin', {
    method: 'POST',
    ...(options || {}),
  });
}

export async function updateRole(params: {
  userId: number,
  role: string
}, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/manager/user/updateRole', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function user(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.WxUser>(userApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}
