// @ts-ignore
/* eslint-disable */

import {op, OP_ADD, OP_GET, OP_MOD, OptionsType, ParamsType} from "@/services/manager/restfulapi";
import {request} from "@@/plugin-request/request";

const apiPathPrefix = '/api/manager/marketing';
const stockApiPath = apiPathPrefix + '/stock';
const stockDetailApiPath = apiPathPrefix + '/stockDetail';
const startStockApiPath = apiPathPrefix + '/startStock';
const pauseStockApiPath = apiPathPrefix + '/pauseStock';
const restartStockApiPath = apiPathPrefix + '/restartStock';
const wxMarketingStockApiPath = apiPathPrefix + '/wxMarketingStock';
const whitelistApiPath = apiPathPrefix + '/whitelist';

export async function stock(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Stock>(stockApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function addStock(body?: API.FavorStocksCreateRequest, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.FavorStocksCreateRequest>(stockApiPath, OP_ADD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function stockDetail(
  params: {
    stockId: string,
  }, options?: { [key: string]: any },
) {
  return request<API.Response<API.Stock>>(stockDetailApiPath, {
    method: 'GET',
    params: {
      stockId: params.stockId,
    },
    ...(options || {}),
  });
}

export async function startStock(params: {
  stockId: string,
}, options?: { [key: string]: any }) {
  return request<API.Response<any>>(startStockApiPath, {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function pauseStock(params: {
  stockId: string,
}, options?: { [key: string]: any }) {
  return request<API.Response<any>>(pauseStockApiPath, {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function restartStock(params: {
  stockId: string,
}, options?: { [key: string]: any }) {
  return request<API.Response<any>>(restartStockApiPath, {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function generateMaLink(params: {
  appId: string,
  stockId: string,
}, options?: { [key: string]: any }) {
  return request<API.Response<any>>(`/api/wx/ma/${params.appId}/marketing/generateUrlLink`, {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function updateWxMarketingStock(body?: API.WxMarketingStock, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.WxMarketingStock>(wxMarketingStockApiPath, OP_MOD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function getWxMarketingStockByStockId(params: {
  stockId: string
}, options?: { [key: string]: any }) {
  return request<API.Response<API.WxMarketingStock>>(wxMarketingStockApiPath, {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function whitelist(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.WxMarketingWhitelist>(whitelistApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}
