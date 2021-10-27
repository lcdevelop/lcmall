import React from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {couponStatistics} from "@/services/manager/marketing/api";
import ProTable, {ProColumns} from "@ant-design/pro-table";
import {Button, Divider} from "antd";

export type StatisticsProps = {
  match: any;
};

const Statistics: React.FC<StatisticsProps> = (props: StatisticsProps) => {

  const columns: ProColumns<API.CouponStatistics>[] = [
    {
      title: '电话号码',
      dataIndex: 'whitelistPhoneNumber',
    },
    {
      title: '是否获取手机号',
      render: (_, entity) => {
        if (entity.wxMaUserHasPhoneNumber) {
          return '是'
        } else {
          return '否'
        }
      }
    },
    {
      title: '领券数',
      render: (_, entity) => (
        entity.couponsInfo?.coupons?.length || 0
      )
    },
    {
      title: '核销券数',
      render: (_, entity) => (
        entity.couponsInfo?.consumeCount || 0
      )
    },
    {
      title: '核销金额',
      render: (_, entity) => (
        entity.couponsInfo?.consumeAmount / 100.0 || 0.0
      )
    },
  ]

  return (
    <PageContainer>
      <ProTable<API.CouponStatistics, API.PageParams>
        headerTitle={'活动ID:' + props.match.params.activityId + ' 白名单数据统计'}
        rowKey="whitelistPhoneNumber"
        search={{
          labelWidth: 120,
        }}
        request={async (params = {}) => {
          return couponStatistics({
            activityId: props.match.params.activityId,
            ...params
          }).then(res => {
            return res;
          })
        }}
        columns={columns}
        pagination={{
          pageSize: 10
        }}
        scroll={{x: "100%"}}
      />
    </PageContainer>
  );
};

export default Statistics;
