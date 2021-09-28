// @ts-ignore
/* eslint-disable */

declare namespace API {

  type OrderAffiliate = {
    id: number;
    sku: Sku;
    count: number;
    price: number;
  }

  type Address = {
    id: number;
    name: string;
    phone: string;
    address: string;
  }

  type ExpressType = {
    id: number;
    name: string;
    code: string;
  }

  type ExpressQueryTrackData = {
    ftime: string;
    context: string;
  }

  type Order = {
    id: number;
    appId: string;
    wxMaUser: WxUser;
    address: Address;
    outTradeNo: string;
    totalFee: number;
    status: number;
    tradeStatus: string;
    createTime: string;
    updateTime: string;
    successTime: string;
    statusStr: string;
    affiliateList: OrderAffiliate[];
    expressType: ExpressType;
    expressTypeId: number; // 辅助用
    expressNo: string;
  }

}
