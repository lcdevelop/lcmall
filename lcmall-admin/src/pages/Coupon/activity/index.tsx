import React, {useRef, useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {activity, generateMaLink, updateActivity} from "@/services/manager/marketing/api";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {Button, message} from "antd";
import {useModel} from "@@/plugin-model/useModel";
import {PlusOutlined} from "@ant-design/icons";
import CreateActivityForm from "@/pages/Coupon/activity/components/CreateActivityForm";

const Statistics: React.FC = () => {

  const { initialState } = useModel('@@initialState');
  const actionRef = useRef<ActionType>();
  const [createModalVisible, handleCreateModalVisible] = useState<boolean>(false);

  const columns: ProColumns<API.WxMarketingActivity>[] = [
    {
      title: '活动ID',
      dataIndex: 'id',
    },
    {
      title: '活动名称',
      dataIndex: 'name',
    },
    {
      title: '界面模板类型',
      dataIndex: 'templateType',
      render: (_, entity) => {
        if (entity.templateType === 1) {
          return "苏恒模板"
        } else if (entity.templateType === 2) {
          return "赵婉莹模板"
        } else {
          return entity.templateType
        }
      }
    },
    {
      title: '批次列表',
      render: (_, entity) => {
        return entity.stockIdList.split(',').map((value, index) => {
          return (
            <div key={index}>
              <a href={'#/marketing/stock/detail/' + value}>{value}</a>
            </div>
          )
        })
      }
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '短信链接',
      render: (_, entity) => {
        return (
          <div>
            {entity.urlLink}
            <br />
            <a onClick={() => {
              generateMaLink({
                appId: initialState?.currentWxUser?.sessionWxApp.appId || '',
                activityId: entity.id,
                templateType: entity.templateType,
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
                } else {
                  message.error(res.msg).then();
                }
              })
            }}>
              {entity.urlLink === null || entity.urlLink === '' ?
              "生成"
              :
              "重新生成"
              }
            </a>
          </div>
        )
      }
    },
    {
      title: '数据统计',
      render: (_, entity) => {
        return (
          <div>
            <a href={'#/marketing/flowstat/' + entity.id}>日维度统计</a>
            <br />
            <a href={'#/marketing/statistics/' + entity.id}>白名单数据统计</a>
            <br />
            <a href={'#/marketing/trendstat/' + entity.id}>趋势统计</a>
          </div>
        )
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
        columns={columns}
        pagination={{
          pageSize: 10
        }}
        scroll={{x: "100%"}}
        actionRef={actionRef}
      />

      <CreateActivityForm
        visible={createModalVisible}
      >

      </CreateActivityForm>
    </PageContainer>
  );
};

export default Statistics;
