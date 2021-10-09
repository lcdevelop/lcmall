import { PlusOutlined } from '@ant-design/icons';
import {Button, message} from 'antd';
import React, {useRef, useState} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import StockForm from "@/pages/Coupon/stock/components/StockForm";
import {ProFormInstance} from "@ant-design/pro-form";
import {stock, addStock} from "@/services/manager/marketing/api";

const Stock: React.FC = () => {

  const createFormRef = useRef<ProFormInstance<API.FavorStocksCreateRequest>>();
  const [createModalVisible, handleCreateModalVisible] = useState<boolean>(false);

  const handleAdd = async (p: API.FavorStocksCreateRequest) => {
    const hide = message.loading('正在添加');

    try {
      const res = await addStock(p);
      if (res.code === 407) {
        message.error('您没有权限，请联系管理员').then();
        return false;
      }
      if (res.code !== 200) {
        message.error(res.msg).then();
        return false;
      }
      hide();
      message.success('添加成功');
      return true;
    } catch (error) {
      hide();
      message.error('添加失败请重试！');
      return false;
    }
  };


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
    {
      title: '是否无资金流',
      dataIndex: 'noCash',
    },
    {
      title: '激活批次的时间',
      dataIndex: 'startTime',
    },
    {
      title: '终止批次的时间',
      dataIndex: 'stopTime',
    },
    {
      title: '是否单品优惠',
      dataIndex: 'singleitem',
    },
    {
      title: '批次类型',
      dataIndex: 'stockType',
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Stock, API.PageParams>
        headerTitle={'批次列表'}
        rowKey="stockId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleCreateModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={stock}
        columns={columns}
        pagination={{
          pageSize: 10
        }}
        scroll={{x: "100%"}}
      />

      <StockForm
        isNew={true}
        title='创建批次'
        visible={createModalVisible}
        values={{}}
        onCancel={() => {
          handleCreateModalVisible(false);
          createFormRef.current?.resetFields();
        }}
        onSubmit={async (values) => {
          const p = values as API.FavorStocksCreateRequest;
          p.stockUseRule = {
            maxCoupons: values['maxCoupons'],
            maxAmount: values['maxAmount'],
            maxAmountByDay: values['maxAmountByDay'],
            maxCouponsPerUser: values['maxCouponsPerUser'],
            naturalPersonLimit: values['naturalPersonLimit'],
            preventApiAbuse: values['preventApiAbuse'],
            combineUse: false,
            couponType: "",
            fixedNormalCoupon: {
              couponAmount: 0,
              transactionMinimum: 0,
            },
            goodsTag: [],
            tradeType: [],
          }
          p.couponUseRule = {
            fixedNormalCoupon: {
              couponAmount: values['couponAmount'],
              transactionMinimum: values['transactionMinimum']
            },
            availableMerchants: ['1488848612']
          }
          const success = await handleAdd(p);
          if (success) {
            handleCreateModalVisible(false);
            createFormRef.current?.resetFields();
          }
        }}
        formRef={createFormRef}
      />

    </PageContainer>
  );
};

export default Stock;
