import { PlusOutlined } from '@ant-design/icons';
import {Button, message, Popconfirm, Select} from 'antd';
import React, {useState, useRef, useEffect} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {sku, addSku, updateSku, removeSku, product} from '@/services/manager/product/api';
import type {ProFormInstance} from "@ant-design/pro-form";
import SkuForm from "@/pages/Product/sku/components/SkuForm";
import type {UploadFile} from "antd/es/upload/interface";

const Sku: React.FC = () => {

  const createFormRef = useRef<ProFormInstance<API.Sku>>();
  const updateFormRef = useRef<ProFormInstance<API.Sku>>();
  const [createModalVisible, handleCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.Sku>();
  const [products, setProducts] = useState<API.Product[]>([]);
  const actionRef = useRef<ActionType>();

  useEffect(() => {
    product({pageSize: 100, current: 1}).then(res => {
      if (res.code === 200) {
        setProducts(res.data!);
      }
    })
  },[])

  const handleAdd = async (s: API.Sku) => {
    const hide = message.loading('正在添加');

    try {
      const res = await addSku(s);
      if (res.code === 407) {
        message.error('您没有权限，请联系管理员').then();
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

  const handleUpdate = async (s: API.Sku) => {
    const hide = message.loading('正在更新');

    try {
      const res = await updateSku(s);
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

  const handleRemove = async (p: API.Sku) => {
    const hide = message.loading('正在删除');
    try {
      const res = await removeSku(p);
      if (res.code === 407) {
        message.error('您没有权限，请联系管理员').then();
        return false;
      }
      hide();
      message.success('删除成功，即将刷新');
      return true;
    } catch (error) {
      hide();
      message.error('删除失败，请重试');
      return false;
    }
  };

  const columns: ProColumns<API.Sku>[] = [
    {
      title: '可售卖项名称',
      dataIndex: 'name',
    },
    {
      title: '商品',
      valueType: 'text',
      render: (dom, entity) => {
        return (
          <div key={entity.id}>{entity.product.name}</div>
        );
      },
      hideInSearch: true,
    },
    {
      title: '展示图片',
      dataIndex: 'image',
      valueType: "image",
      hideInSearch: true,
    },
    {
      title: '价格',
      valueType: 'text',
      render: (dom, entity) => {
        return (
          <div key={entity.id}>{entity.price.price / 100.0}</div>
        );
      },
      hideInSearch: true,
    },
    {
      title: '属性',
      valueType: 'text',
      render: (dom, entity) => {
        if (entity.specList != null) {
          return (
            <Select
              key={entity.id}
              mode="tags"
              disabled
              bordered={false}
              value={entity.specList}
            />
          )
        }
        return <div />
      },
      hideInSearch: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="editable"
          onClick={() => {
            updateFormRef.current?.resetFields();
            setCurrentRow(record);
            handleUpdateModalVisible(true);
          }}
        >
          编辑
        </a>,
        <Popconfirm
          title="确定要删除?"
          okText="Yes"
          cancelText="No"
          key="delete"
          onConfirm={() => {
            handleRemove(record).then(() => {
              actionRef.current?.reload();
            });
          }}
        >
          <a
            href='#'
          >
            删除
          </a>
        </Popconfirm>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Sku, API.PageParams>
        headerTitle={'可售卖项列表'}
        rowKey="id"
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
        request={sku}
        columns={columns}
        actionRef={actionRef}
      />

      <SkuForm
        title='创建可售卖项'
        visible={createModalVisible}
        products={products}
        values={{}}
        onCancel={() => {
          handleCreateModalVisible(false);
          createFormRef.current?.resetFields();
        }}
        onSubmit={async (values) => {
          const pri: API.Price = {
            id: -1,
            policy: 1,
            price: values.price_value!,
          }
          const pro: API.Product = {
            id: values.product_id!, name: "", description: "", category: {id: 0, name: "", appId: ""}
          }
          const imageObj: UploadFile<API.Response<string>> = values.image?.at(0) as unknown as UploadFile<API.Response<string>>;
          const s: API.Sku = {
            price_value: 0, specList: [],
            id: -1,
            name: values.name!,
            image: imageObj.response?.data || '',
            product: pro,
            price: pri,
            specs: values.specs?.toString() || '',
          };
          const success = await handleAdd(s);
          if (success) {
            actionRef.current?.reload();
            handleCreateModalVisible(false);
            createFormRef.current?.resetFields();
            return true;
          }
          return false;
        }}
        formRef={createFormRef}
        isNew={true}
      />

      <SkuForm
        title='编辑可售卖项'
        visible={updateModalVisible}
        products={products}
        values={currentRow || {}}
        onCancel={() => {
          handleUpdateModalVisible(false);
          setCurrentRow(undefined);
          updateFormRef.current?.resetFields();
        }}
        onSubmit={async (values) => {
          const pri: API.Price = {
            id: currentRow!.price.id,
            policy: currentRow!.price.policy,
            price: values.price_value!,
          }
          const pro: API.Product = {
            id: values.product_id!, name: "", description: "", category: {id: 0, name: "", appId: ""}
          }
          const imageObj: UploadFile<API.Response<string>> = values.image?.at(0) as unknown as UploadFile<API.Response<string>>;
          const s: API.Sku = {
            price_value: 0, specList: currentRow!.specList,
            id: currentRow!.id,
            name: values.name!,
            image: imageObj.response?.data || currentRow!.image,
            product: pro,
            price: pri,
            specs: values.specs?.toString() || '',
          };
          const success = await handleUpdate(s);
          if (success) {
            actionRef.current?.reload();
            handleUpdateModalVisible(false);
            updateFormRef.current?.resetFields();
            return true;
          }
          return false;
        }}
        formRef={updateFormRef}
        isNew={false}
      />
    </PageContainer>
  );
};

export default Sku;
