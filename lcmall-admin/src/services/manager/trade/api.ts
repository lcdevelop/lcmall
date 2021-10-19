// @ts-ignore
/* eslint-disable */

import {request} from "@@/plugin-request/request";
import {op, OP_GET, OP_MOD, OptionsType, ParamsType} from "@/services/manager/restfulapi";

const apiPathPrefix = '/api/manager/trade';
const orderApiPath = apiPathPrefix + '/order';
const expressTypeApiPath = apiPathPrefix + '/expressTypes';
const expressTrackPath = apiPathPrefix + '/expressTrack';

export enum OrderStatus {
  OS_INIT = 0,
  OS_PAID,
  OS_TRANSIT,
  OS_REFUND,
  OS_SUCCESS
}

/* category api */
export async function order(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Order>(orderApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function updateOrder(body?: API.Order, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Order>(orderApiPath, OP_MOD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function expressTypes(
  options?: { [key: string]: any },
) {
  return request<API.Response<API.ExpressType[]>>(expressTypeApiPath, {
    method: 'GET',
    ...(options || {}),
  });
}

export async function expressTrack(
  params: {
    outTradeNo: string,
  }, options?: { [key: string]: any },
) {
  return request<API.Response<API.ExpressQueryTrackData[]>>(expressTrackPath, {
    method: 'GET',
    params: {
      outTradeNo: params.outTradeNo,
    },
    ...(options || {}),
  });
}
