import React, {useState} from 'react';
import {EditableProTable, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {updateRole, user} from "@/services/manager/user/api";
import {Space} from "antd";

const User: React.FC = () => {

  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const [dataSource, setDataSource] = useState<API.WxUser[]>([]);

  const columns: ProColumns<API.WxUser>[] = [
    {
      title: '所属App',
      dataIndex: 'wxApp',
      valueType: 'text',
      editable: false,
      render: (dom, entity) => (
        <Space>
          {entity.wxApp.name}
        </Space>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      editable: false,
    },
    {
      title: '昵称',
      dataIndex: 'nickName',
      valueType: 'text',
      editable: false,
    },
    {
      title: '头像',
      dataIndex: 'avatarUrl',
      valueType: "image",
      hideInSearch: true,
      editable: false,
    },
    {
      title: '性别',
      dataIndex: 'gender',
      valueType: 'text',
      editable: false,
    },
    {
      title: '权限(admin/appId)',
      dataIndex: 'role',
      valueType: 'text',
    },
    {
      title: '操作',
      valueType: 'option',
      width: 200,
      render: (text, record, _, action) => {
        if (record.id > 0) {
          return [
            <a
              key="editable"
              onClick={() => {
                action?.startEditable?.(record.id);
              }}
            >
              编辑
            </a>,
          ]
        }
        return <div/>
      },
    },
  ];


  const onSave = async (key: any, record: API.WxUser, originRow: API.WxUser) => {
    return new Promise<any>((resolve, reject) => {
      updateRole({userId: record.id, role: record.role}).then(res => {
        if (res.code === 200) {
          resolve(true);
        }
        reject();
      }, () => {
        reject();
      });
    });
  }

  return (
    <PageContainer>
      <EditableProTable<API.WxUser>
        rowKey="id"
        headerTitle="用户"
        maxLength={100}
        request={user}
        columns={columns}
        value={dataSource}
        onChange={setDataSource}
        editable={{
          type: 'multiple',
          editableKeys,
          onSave,
          onChange: setEditableRowKeys,
          actionRender: (row, config, dom) => [dom.save, dom.cancel],
        }}
        recordCreatorProps={false}
      />

    </PageContainer>
  );
};

export default User;
