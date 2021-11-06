import {Card, Col, Divider, Row} from 'antd';
import React, {useEffect} from 'react';
// import {addStock} from "@/services/manager/marketing/api";

type StockListProps = {
  history: any;
}

const StockList: React.FC<StockListProps> = (props: StockListProps) => {

  const STATUS_RUNNING: number = 1;
  const STATUS_PAUSED: number = 2;
  const STATUS_UNACTIVATED: number = 3;
  const STATUS_STOPED: number = 4;

  useEffect(() => {
    console.log(props)
  }, [])


  const onCardClicked = (status: number) => {
    console.log(status);
    switch (status) {
      case STATUS_RUNNING:
        props.history.push('/marketing/stock/data?status=running');
        break;
      case STATUS_PAUSED:
        props.history.push('/marketing/stock/data?status=paused');
        break;
      case STATUS_UNACTIVATED:
        props.history.push('/marketing/stock/data?status=unactivated');
        break;
      case STATUS_STOPED:
        props.history.push('/marketing/stock/data?status=stoped');
        break;
    }

  }

  // const onAddStock = () => {
  //   const request = {
  //     stockName: '接口批次-指定商户test1',
  //     availableBeginTime: '2021-11-02 10:30:00',
  //     availableEndTime: '2021-11-02 23:59:59',
  //     stockUseRule: {
  //       maxCoupons: 10,
  //       maxAmount: 1000,
  //       maxAmountByDay: 1000,
  //       maxCouponsPerUser: 10,
  //       naturalPersonLimit: true,
  //       preventApiAbuse: true,
  //     },
  //     patternInfo: {
  //       description: '这是pattern',
  //       merchantLogo: 'http://res.wx.qq.com/zh_CN/htmledition/images/mmpaybanklogo/PayCardGSlogo@2x.png',
  //       merchantName: '工商银行储蓄卡',
  //       backgroundColor: 'COLOR100',
  //     },
  //     couponUseRule: {
  //       fixedNormalCoupon: {
  //         couponAmount: 100,
  //         transactionMinimum: 101,
  //       },
  //       goodsTag: [],
  //       limitPay: ['ICBC_DEBIT'],
  //       combineUse: true,
  //       availableMerchants: ['1488848612'],
  //     },
  //     noCash: false,
  //   }
  //
  //   addStock(request).then(res => {
  //     console.log(res);
  //   })
  // }

  return (
    <div>
      <Row gutter={16}>
        <Col span={6}>
          <Card title='运行中' bordered={false} onClick={() => {onCardClicked(STATUS_RUNNING)}}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='暂停中' bordered={false} onClick={() => {onCardClicked(STATUS_PAUSED)}}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='未激活' bordered={false} onClick={() => {onCardClicked(STATUS_UNACTIVATED)}}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='已停止' bordered={false} onClick={() => {onCardClicked(STATUS_STOPED)}}>
            点击查看详情
          </Card>
        </Col>
      </Row>

      <Divider />
      {/*<Button type={"primary"} onClick={onAddStock}>手工新建批次</Button>*/}
    </div>
  );
};

export default StockList;
