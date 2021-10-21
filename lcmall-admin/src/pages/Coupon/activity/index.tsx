import React, {useRef} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {activity, generateMaLink, updateActivity} from "@/services/manager/marketing/api";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {useModel} from "@@/plugin-model/useModel";

const Statistics: React.FC = () => {

  const { initialState } = useModel('@@initialState');
  const actionRef = useRef<ActionType>();

  const columns: ProColumns<API.WxMarketingActivity>[] = [
    {
      title: 'id',
      dataIndex: 'id',
    },
    {
      title: 'name',
      dataIndex: 'name',
    },
    {
      title: 'templateId',
      dataIndex: 'templateId',
    },
    {
      title: 'stockIdList',
      dataIndex: 'stockIdList',
    },
    {
      title: 'createTime',
      dataIndex: 'createTime',
    },
    {
      title: 'urlLink',
      render: (_, entity) => {
        if (entity.urlLink === null || entity.urlLink === '') {
          return (
            <a onClick={() => {
              generateMaLink({
                appId: initialState?.currentWxUser?.sessionWxApp.appId!,
                activityId: entity.id,
                templateId: entity.templateId,
              }).then(res => {
                console.log(res);
                if (res.code === 200) {
                  entity.urlLink = res.data;
                  updateActivity(entity).then(updateRes => {
                    console.log(updateRes);
                    if (updateRes.code === 200) {
                      actionRef.current?.reload();
                    }
                  })
                }
              })
            }}>生成</a>
          )
        } else {
          return <div>{entity.urlLink}</div>
        }
      }
    },
  ]

  return (
    <PageContainer>
      <ProTable<API.WxMarketingActivity, API.PageParams>
        headerTitle={'活动列表'}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        request={activity}
        columns={columns}
        pagination={{
          pageSize: 10
        }}
        scroll={{x: "100%"}}
        actionRef={actionRef}
      />
    </PageContainer>
  );
};

export default Statistics;
