import React, {useEffect, useRef, useState} from 'react';
import {Button} from 'antd';
import ProCard, {StatisticCard} from '@ant-design/pro-card';
import {stockDetail} from "@/services/manager/marketing/api";
const { Statistic } = StatisticCard;

const { Divider } = ProCard;

export type StockDetailProps = {
  match: any;
};

const StockDetail: React.FC<StockDetailProps> = (props) => {

  const [stock, setStock] = useState<API.Stock>();
  const [stockId, setStockId] = useState(null);

  useEffect(() => {
    setStockId(props.match.params.stockId);

    stockDetail(
      {
        stockId: props.match.params.stockId
      }).then((res: API.Response<API.Stock>) => {
      if (res.code === 200) {
        setStock(res.data!);
      }
    })
  }, [])

  const activate = () => {

  }

  if (undefined === stock) {
    return (
      <div>loading...</div>
    )
  }

  return (
    <div>
      <Button type={"primary"} onClick={activate}>激活</Button>
      <Divider />
      <ProCard.Group>
        <StatisticCard>
          <Statistic title="批次号" value={stock?.stockId} />
          <Statistic title="创建批次的商户号" value={stock?.stockCreatorMchid} />
          <Statistic title="批次名称" value={stock?.stockName} />
          <Statistic title="批次状态" value={stock?.status} />
          <Statistic title="创建时间" value={stock?.create_time} />
          <Statistic title="使用说明" value={stock?.description} />
          <Statistic title="发放总上限" value={stock?.stockUseRule.maxCoupons} />
          <Statistic title="总预算(单位：分)" value={stock?.stockUseRule.maxAmount} />
          <Statistic title="单天发放上限金额(单位：分)" value={stock?.stockUseRule.maxAmountByDay} />
          <Statistic title="面额(单位：分)" value={stock?.stockUseRule.fixedNormalCoupon.couponAmount} />
          <Statistic title="门槛(单位：分)" value={stock?.stockUseRule.fixedNormalCoupon.transactionMinimum} />
          <Statistic title="单个用户可领个数" value={stock?.stockUseRule.maxCouponsPerUser} />
        </StatisticCard>
        <Divider type={'vertical'} />
        <StatisticCard>
          <Statistic title="券类型" value={stock?.stockUseRule.couponType} />
          <Statistic title="订单优惠标记" value={stock?.stockUseRule.goodsTag.toString()} />
          <Statistic title="支付方式" value={stock?.stockUseRule.tradeType.toString()} />
          <Statistic title="是否可叠加其他优惠" value={stock?.stockUseRule.combineUse?'yes':'no'} />
          <Statistic title="可用开始时间" value={stock?.availableBeginTime} />
          <Statistic title="可用结束时间" value={stock?.availableEndTime} />
          <Statistic title="已发券数量" value={stock?.distributedCoupons} />
          <Statistic title="是否无资金流" value={stock?.noCash?'yes':'no'} />
          <Statistic title="激活批次的时间" value={stock?.startTime} />
          <Statistic title="终止批次的时间" value={stock?.stopTime} />
          <Statistic title="可用优惠的商品最高单价(单位：分)" value={stock?.cutToMessage?.singlePriceMax} />
          <Statistic title="减至后的优惠单价(单位：分)" value={stock?.cutToMessage?.cutToPrice} />
        </StatisticCard>
        <Divider type={'vertical'} />
        <StatisticCard>
          <Statistic title="是否单品优惠" value={stock?.singleitem?'yes':'no'} />
          <Statistic title="批次类型" value={stock?.stockType} />
        </StatisticCard>
      </ProCard.Group>
    </div>
  );
};

export default StockDetail;
