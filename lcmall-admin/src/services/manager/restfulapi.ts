// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

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
    return request<API.Response<T[]>>(apiPath, {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    });
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
