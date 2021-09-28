import {Avatar, message, Modal, Space, Timeline} from 'antd';
import React, {useRef, useState} from 'react';
import {PageContainer} from '@ant-design/pro-layout';
import type {ActionType, ProColumns} from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import type {ProFormInstance} from "@ant-design/pro-form";
import OrderForm, {OpType} from "@/pages/Trade/order/components/OrderForm";
import {order, updateOrder, expressTrack, OrderStatus} from "@/services/manager/trade/api";

import styles from './index.less';

const Order: React.FC = () => {

  const updateFormRef = useRef<ProFormInstance<API.Order>>();
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.Order>();
  const [opType, setOpType] = useState<OpType>(OpType.OP_MOD_PRICE);
  const [expressQueryTrackData, setExpressQueryTrackData] = useState<API.ExpressQueryTrackData[]>([]);
  const actionRef = useRef<ActionType>();

  const handleUpdate = async (s: API.Order) => {
    const hide = message.loading('正在更新');

    try {
      const res = await updateOrder(s);
      if (res.code === 407) {
        message.error('您没有权限，请联系管理员').then();
        return false;
      }
      hide();
      message.success('更新成功');
      return true;
    } catch (error) {
      hide();
      message.error('更新失败请重试！');
      return false;
    }
  };

  const updateExpressContent = (entity: API.Order) => {
    expressTrack({outTradeNo: entity.outTradeNo}).then(res => {
      if (res.code === 200) {
        setExpressQueryTrackData(res.data!);
        Modal.info({
          title: `${entity.expressType.name} 单号:${entity.expressNo}`,
          content: <Timeline className={styles.timeLine}>
            {expressQueryTrackData.map((value) => {
              return (
                <Timeline.Item key={value.ftime}>
                  {value.ftime}
                  <br />
                  {value.context}
                </Timeline.Item>
              )
            })}
          </Timeline>,
        })
      } else {
        setExpressQueryTrackData([]);
      }
    });
  }

  const columns: ProColumns<API.Order>[] = [
    {
      title: '订单编号',
      dataIndex: 'outTradeNo',
      valueType: "text",
    },
    {
      title: '用户',
      dataIndex: 'wxMaUser.avatarUrl',
      valueType: 'avatar',
      render: (dom, entity) => (
        <Space>
          <Avatar src={entity.wxMaUser.avatarUrl} />
          {entity.wxMaUser.nickName}
        </Space>
      ),
      hideInSearch: true,
    },
    {
      title: '邮寄地址',
      dataIndex: 'address',
      valueType: 'text',
      render: (dom, entity) => (
        <Space>
          {entity.address.name}
          {entity.address.phone}
          {entity.address.address}
        </Space>
      ),
      hideInSearch: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
    },
    {
      title: '完成支付时间',
      dataIndex: 'successTime',
      valueType: 'dateTime',
      hideInSearch: true,
    },
    {
      title: '价格',
      valueType: 'text',
      render: (dom, entity) => {
        return (
          <div key={entity.id}>{entity.totalFee / 100.0}</div>
        );
      },
      hideInSearch: true,
    },
    {
      title: '状态',
      dataIndex: 'statusStr',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const modPriceDom = <a
            key="editable"
            onClick={() => {
              updateFormRef.current?.resetFields();
              setCurrentRow(record);
              setOpType(OpType.OP_MOD_PRICE);
              handleUpdateModalVisible(true);
            }}
          >
            改价
          </a>;

        const sendExpressDom = <a
            key="editable"
            onClick={() => {
              updateFormRef.current?.resetFields();
              setCurrentRow(record);
              setOpType(OpType.OP_SEND_EXPRESS);
              handleUpdateModalVisible(true);
            }}
          >
            发货
          </a>;

        const expressTrackDom = <a
            key="editable"
            onClick={() => {
              setExpressQueryTrackData([]);
              updateExpressContent(record)
            }}
          >
            查看物流
          </a>;

        // 订单不同状态展示不同按钮

        if (record.status === OrderStatus.OS_INIT) {
          return (
            [
              modPriceDom,
              sendExpressDom,
            ]
          )
        }

        if (record.status === OrderStatus.OS_PAID) {
          return (
            [
              sendExpressDom,
              expressTrackDom,
            ]
          )
        }

        if (record.status === OrderStatus.OS_TRANSIT) {
          return (
            [
              expressTrackDom,
            ]
          )
        }

        return (
          [
            modPriceDom,
            sendExpressDom,
            expressTrackDom,
          ]
        )
      },
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Order, API.PageParams>
        headerTitle={'订单列表'}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        request={order}
        columns={columns}
        actionRef={actionRef}
      />


      <OrderForm
        title='编辑订单'
        visible={updateModalVisible}
        values={currentRow || {}}
        onCancel={() => {
          handleUpdateModalVisible(false);
          setCurrentRow(undefined);
          updateFormRef.current?.resetFields();
        }}
        onSubmit={async (values) => {
          if (opType === OpType.OP_MOD_PRICE) {
            const s: API.Order = currentRow!;
            s.totalFee = values.totalFee!;
            const success = await handleUpdate(s);
            if (success) {
              actionRef.current?.reload();
              handleUpdateModalVisible(false);
              updateFormRef.current?.resetFields();
              return true;
            }
          }

          if (opType === OpType.OP_SEND_EXPRESS) {
            const s: API.Order = currentRow!;
            s.expressType = {id: values.expressTypeId!, name: '', code: ''}
            s.expressNo = values.expressNo || '';
            s.status = OrderStatus.OS_TRANSIT;
            const success = await handleUpdate(s);
            if (success) {
              actionRef.current?.reload();
              handleUpdateModalVisible(false);
              updateFormRef.current?.resetFields();
              return true;
            }
          }
          return false;
        }}
        opType={opType}
        formRef={updateFormRef}
      />
    </PageContainer>
  );
};

export default Order;
