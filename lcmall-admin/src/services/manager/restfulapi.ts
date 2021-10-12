// @ts-ignore
/* eslint-disable */
import {request} from 'umi';
import {message} from "antd";

export type ParamsType = {
  current?: number;
  pageSize?: number;
  status?: string;
};

export type OptionsType = {
  [key: string]: any
}

export const OP_GET = "GET";
export const OP_ADD = "POST";
export const OP_MOD = "PUT";
export const OP_DEL = "DELETE";

export async function op<T>(
  apiPath: string,
  opType: string,
  body?: T,
  params?: ParamsType,
  options?: OptionsType,
) {
  if (opType === OP_GET) {
    return new Promise<any>((resolve, reject) => {
      request<API.Response<T[]>>(apiPath, {
        method: 'GET',
        params: {
          ...params,
        },
        ...(options || {}),
      }).then((res) => {
        if (res.code === 200) {
          resolve(res);
        } else {
          message.error(res.msg).then();
        }
      }, (err) => {reject(err);})
    })
  }

  if (opType === OP_ADD) {
    return request<API.Response<T>>(apiPath, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    });
  }

  if (opType === OP_MOD) {
    return request<API.Response<T>>(apiPath, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    });
  }

  if (opType === OP_DEL) {
    return request<API.Response<T>>(apiPath, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    });
  }

  return;
}
