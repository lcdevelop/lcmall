import React, {useState} from 'react';
import {EditableProTable, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {category, addCategory, removeCategory, updateCategory} from "@/services/manager/product/api";
import {message, Popconfirm} from "antd";
// import styles from './index.less';

const Category: React.FC = () => {

  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const [dataSource, setDataSource] = useState<API.Category[]>([]);


  const onDelete = (record: API.Category) => {
    console.log('onDelete', record);
    removeCategory(record).then(res => {
      if (res.code === 200) {
        setDataSource(dataSource.filter((item) => item.id !== record.id));
      } else if (res.code === 406) {
        message.error('有关联项，无法删除').then();
      } else if (res.code === 407) {
        message.error('您没有权限，请联系管理员').then();
      }
    })

  }

  const columns: ProColumns<API.Category>[] = [
    {
      title: '品类名称',
      dataIndex: 'name',
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
            <Popconfirm
              title="确定要删除?"
              onConfirm={() => onDelete(record)}
              okText="Yes"
              cancelText="No"
              key="delete"
            >
              <a
                href='#'
              >
                删除
              </a>
            </Popconfirm>,
          ]
        }
        return <div/>
      },
    },
  ];


  const onSave = async (key: any, record: API.Category, originRow: API.Category) => {
    return new Promise<any>((resolve, reject) => {
      console.log("onSave", key, record, originRow);
      if (record.id > 0) {
        updateCategory(record).then(res => {
          if (res.code === 200) {
            return resolve(true);
          }

          if (res.code === 407) {
            message.error('您没有权限，请联系管理员').then();
            return reject();
          }

          return reject();
        })
      } else {
        addCategory(record).then(res => {
          if (res.code === 200) {
            return resolve(true);
          }

          if (res.code === 407) {
            message.error('您没有权限，请联系管理员').then();
            return reject();
          }

          return reject();
        }, () => {
          return reject();
        })
      }
    });
  }

  return (
    <PageContainer>
      <EditableProTable<API.Category>
        rowKey="id"
        headerTitle="品类"
        maxLength={100}
        recordCreatorProps={
          {
            position: 'bottom',
            creatorButtonText: '新增一个品类',
            record: {id: -Date.now()/10000, wxAppId:-1, name:''},
          }
        }
        request={category}
        columns={columns}
        value={dataSource}
        onChange={setDataSource}
        editable={{
          type: 'multiple',
          editableKeys,
          onSave,
          onChange: setEditableRowKeys,
        }}
      />

    </PageContainer>
  );
};

export default Category;
