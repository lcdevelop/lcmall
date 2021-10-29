// @ts-ignore
/* eslint-disable */

declare namespace API {

  type FixedNormalCoupon = {
    couponAmount: number;
    transactionMinimum: number;
  }

  type LimitCard = {
    name: string;
    bin: string[];
  }

  type StockUseRule = {
    maxCoupons: number;
    maxAmount: number;
    maxAmountByDay: number;
    maxCouponsPerUser: number;
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
    goodsTag: string[];
    limitPay: string[];
    combineUse: boolean;
    availableMerchants: string[];
  }

  type PatternInfo = {
    description: string;
    merchantLogo: string;
    merchantName: string;
    backgroundColor: string;
  }

  type FavorStocksCreateRequest = {
    stockName: string;
    belongMerchant: string;
    availableBeginTime: string;
    availableEndTime: string;
    stockUseRule: StockUseRule;
    patternInfo: PatternInfo;
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
    batchNo: number;
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
    consumeTime: string;
    consumeMchid: string;
    transactionId: string;
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

  type WxMarketingActivity = {
    id: number;
    name: string;
    wxAppId: number;
    templateType: number;
    stockIdList: string;
    urlLink: string;
    createTime: string;
  }

  type WxMarketingActivityExtra = {
    id: number;
    groupId: number;
    content: string;
    styleType: number;
  }

  type WxMarketingActivityExtraGroupEx = {
    id: number;
    activityId: number;
    name: string;
    extras: WxMarketingActivityExtra[];
  }

  type FlowStatistics = {
    pv: number;
    uv: number;
    clickPv: number;
    clickUv: number;
  }

  type TrendStat = {
    keys: string[];
    values: number[];
  }
}
