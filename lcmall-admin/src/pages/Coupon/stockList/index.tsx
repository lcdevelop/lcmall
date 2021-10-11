import {Card, Col, Row} from 'antd';
import React, {useEffect} from 'react';

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

  return (
    <div>
      <Row gutter={16}>
        <Col span={6}>
          <Card title='运行中' bordered={false} onClick={onCardClicked.bind(this, STATUS_RUNNING)}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='暂停中' bordered={false} onClick={onCardClicked.bind(this, STATUS_PAUSED)}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='未激活' bordered={false} onClick={onCardClicked.bind(this, STATUS_UNACTIVATED)}>
            点击查看详情
          </Card>
        </Col>
        <Col span={6}>
          <Card title='已停止' bordered={false} onClick={onCardClicked.bind(this, STATUS_STOPED)}>
            点击查看详情
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default StockList;
