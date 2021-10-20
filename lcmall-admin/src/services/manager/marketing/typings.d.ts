// @ts-ignore
/* eslint-disable */

declare namespace API {

  type FixedNormalCoupon = {
    couponAmount: number;
    transactionMinimum: number;
  }

  type StockUseRule = {
    maxCoupons: number;
    maxAmount: number;
    maxAmountByDay: number;
    fixedNormalCoupon: FixedNormalCoupon;
    maxCouponsPerUser: number;
    couponType: string;
    goodsTag: string[];
    tradeType: string[];
    combineUse: boolean;
    naturalPersonLimit: boolean;
    preventApiAbuse: boolean;
  }

  type CutToMessage = {
    singlePriceMax: number;
    cutToPrice: number;
  }

  // 代金券批次
  type Stock = {
    stockId: string;
    stockCreatorMchid: string;
    stockName: string;
    status: string;
    create_time: string;
    description: string;
    stockUseRule: StockUseRule;
    availableBeginTime: string;
    availableEndTime: string;
    distributedCoupons: string;
    noCash: boolean;
    startTime: string;
    stopTime: string;
    cutToMessage: CutToMessage;
    singleitem: boolean;
    stockType: string;
  }

  type CouponUseRule = {
    fixedNormalCoupon: FixedNormalCoupon;
    availableMerchants: string[];
  }

  type FavorStocksCreateRequest = {
    stockName: string;
    belongMerchant: string;
    availableBeginTime: string;
    availableEndTime: string;
    stockUseRule: StockUseRule;
    couponUseRule: CouponUseRule;
    noCash: boolean;
    stockType: string;
    outRequestNo: string;
  }

  type WxMarketingStock = {
    stockId: string;
    cardId: string;
  }

  type WxMarketingWhitelist = {
    id: number;
    phoneNumber: string;
  }

  type FavorCallbacksSaveResult = {
    updateTime: string;
    notifyUrl: string;
  }

  type CouponItem = {
    stockId: string;
    couponId: string;
    status: string;
    transactionMinimum: number;
    couponAmount: number;
  }

  type CouponsInfo = {
    coupons: CouponItem[];
    consumeCount: number;
    consumeAmount: number;
  }

  type CouponStatistics = {
    whitelistPhoneNumber: string;
    wxMaUserHasPhoneNumber: boolean;
    couponsInfo: CouponsInfo;
  }
}
