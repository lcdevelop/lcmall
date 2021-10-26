import React from 'react';
import {PageContainer} from '@ant-design/pro-layout';
import ProTable, {ProColumns} from "@ant-design/pro-table";
import {whitelist} from "@/services/manager/marketing/api";

const Whitelist: React.FC = () => {

  const columns: ProColumns<API.WxMarketingWhitelist>[] = [
    {
      title: '手机号',
      dataIndex: 'phoneNumber',
    },
    {
      title: '批号',
      dataIndex: 'batchNo',
    }
  ]

  return (
    <PageContainer>
      <ProTable<API.WxMarketingWhitelist, API.PageParams>
        headerTitle={'白名单'}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        request={
          async (params = {}) => {
            return whitelist({
              activityId: 1,
              ...params
            }).then(res => {
              return res;
            })
          }
        }
        columns={columns}
        pagination={{
          pageSize: 30
        }}
        scroll={{x: "100%"}}
      />
    </PageContainer>
  );
};

export default Whitelist;
