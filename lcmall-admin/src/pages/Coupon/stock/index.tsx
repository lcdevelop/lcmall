import React, {useEffect, useState} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {stock} from "@/services/manager/marketing/api";
import {OptionsType, ParamsType} from "@/services/manager/restfulapi";

type StockProps = {
  location: any;
}

const Stock: React.FC<StockProps> = (props: StockProps) => {

  const [status, setStatus] = useState<string>('none');

  useEffect(() => {
    setStatus(props.location.query.status);
  }, [])

  const columns: ProColumns<API.Stock>[] = [
    {
      title: '批次号',
      dataIndex: 'stockId',
      render: (_, entity) => <a href={'#/marketing/stock/detail/' + entity.stockId}>{entity.stockId}</a>,
    },
    {
      title: '批次名称',
      dataIndex: 'stockName',
    },
    {
      title: '批次状态',
      dataIndex: 'status',
    },
    {
      title: '创建时间',
      dataIndex: 'create_time',
    },
    {
      title: '描述信息',
      dataIndex: 'description',
      ellipsis: true,
    },
    {
      title: '可用开始时间',
      dataIndex: 'availableBeginTime',
    },
    {
      title: '可用结束时间',
      dataIndex: 'availableEndTime',
    },
    {
      title: '已发券数量',
      dataIndex: 'distributedCoupons',
    },
  ];



  const stockInternal = async (params?: ParamsType, options?: OptionsType) => {
    return stock({status: status, ...params}, options);
  }

  return (
    <PageContainer>
      <ProTable<API.Stock, API.PageParams>
        headerTitle={'批次列表'}
        rowKey="stockId"
        search={{
          labelWidth: 120,
        }}
        request={stockInternal}
        columns={columns}
        pagination={{
          pageSize: 10
        }}
        scroll={{x: "100%"}}
      />

    </PageContainer>
  );
};

export default Stock;
