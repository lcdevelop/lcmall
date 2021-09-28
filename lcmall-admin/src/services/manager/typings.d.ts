// @ts-ignore
/* eslint-disable */

declare namespace API {

  type Response<T> = {
    data?: T;
    code: number;
    msg: string;
  }

  type PageParams = {
    current?: number;
    pageSize?: number;
  };
}
