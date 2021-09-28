// @ts-ignore
/* eslint-disable */

import {op, OP_ADD, OP_DEL, OP_GET, OP_MOD, OptionsType, ParamsType} from "@/services/manager/restfulapi";

const apiPathPrefix = '/api/manager/product';
const categoryApiPath = apiPathPrefix + '/category';
const productApiPath = apiPathPrefix + '/product';
const skuApiPath = apiPathPrefix + '/sku';

/* category api */
export async function category(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Category>(categoryApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function addCategory(body?: API.Category, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Category>(categoryApiPath, OP_ADD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function updateCategory(body?: API.Category, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Category>(categoryApiPath, OP_MOD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function removeCategory(body?: API.Category, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Category>(categoryApiPath, OP_DEL, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

/* product api */
export async function product(params: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Product>(productApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function addProduct(body?: API.Product, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Product>(productApiPath, OP_ADD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function updateProduct(body?: API.Product, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Product>(productApiPath, OP_MOD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function removeProduct(body?: API.Product, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Product>(productApiPath, OP_DEL, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

/* sku api */
export async function sku(params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Sku>(skuApiPath, OP_GET, undefined, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function addSku(body?: API.Sku, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Sku>(skuApiPath, OP_ADD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function updateSku(body?: API.Sku, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Sku>(skuApiPath, OP_MOD, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}

export async function removeSku(body?: API.Sku, params?: ParamsType, options?: OptionsType) {
  return new Promise<any>((resolve, reject) => {
    op<API.Sku>(skuApiPath, OP_DEL, body, params, options)
      .then((res) => {resolve(res);}, (err) => {reject(err);})
  })
}
