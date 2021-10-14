import { PlusOutlined } from '@ant-design/icons';
import {Button, message, Popconfirm} from 'antd';
import React, {useState, useRef, useEffect} from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import {product, addProduct, updateProduct, removeProduct, category} from '@/services/manager/product/api';
import type {ProFormInstance} from "@ant-design/pro-form";
import ProductForm from "@/pages/Product/product/components/ProductForm";
import type {ProSchemaValueEnumObj} from "@ant-design/pro-utils/lib/typing";

const Product: React.FC = () => {

  const createFormRef = useRef<ProFormInstance<API.Product>>();
  const updateFormRef = useRef<ProFormInstance<API.Product>>();
  const [createModalVisible, handleCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.Product>();
  const [categories, setCategories] = useState<API.Category[]>([]);
  const [categoriesOptions, setCategoriesOptions] = useState<ProSchemaValueEnumObj>();
  const actionRef = useRef<ActionType>();

  useEffect(() => {
    category({pageSize: 100, current: 1}).then(res => {
      if (res.code === 200) {
        setCategories(res.data!);
      }
    })
  },[])

  useEffect(() => {
    const catOptions = {}
    categories.map((value) => {
      catOptions[value.id] = {
        text: value.name,
      }
      return value;
    })
    setCategoriesOptions(catOptions);
  }, [categories])

  const handleAdd = async (p: API.Product) => {
    console.log(p);
    const hide = message.loading('正在添加');

    try {
      const res = await addProduct(p);
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

  const handleUpdate = async (p: API.Product) => {
    const hide = message.loading('正在更新');

    try {
      const res = await updateProduct(p);
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

  const handleRemove = async (p: API.Product) => {
    const hide = message.loading('正在删除');
    try {
      const res = await removeProduct(p);
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

  const columns: ProColumns<API.Product>[] = [
    {
      title: '商品名称',
      dataIndex: 'name',
    },
    {
      title: '品类',
      render: (dom, entity) => {
        return (
          <div key={entity.id}>{entity.category.name}</div>
        );
      },
      dataIndex: 'categoryId',
      filters: true,
      onFilter: true,
      valueType: 'select',
      valueEnum: categoriesOptions,
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
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
            setCurrentRow(record);
            updateFormRef.current?.resetFields();
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
      <ProTable<API.Product, API.PageParams>
        headerTitle={'商品列表'}
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
        request={product}
        columns={columns}
        actionRef={actionRef}
      />

      <ProductForm
        title='创建商品'
        visible={createModalVisible}
        categories={categories}
        values={{}}
        onCancel={() => {
          handleCreateModalVisible(false);
          createFormRef.current?.resetFields();
        }}
        onSubmit={async (values) => {
          const p = values as API.Product;
          const success = await handleAdd(p);
          if (success) {
            actionRef.current?.reload();
            handleCreateModalVisible(false);
            createFormRef.current?.resetFields();
          }
        }}
        formRef={createFormRef}
      />

      <ProductForm
        title='编辑商品'
        visible={updateModalVisible}
        categories={categories}
        values={currentRow || {}}
        onCancel={() => {
          handleUpdateModalVisible(false);
          setCurrentRow(undefined);
        }}
        onSubmit={async (values) => {
          const p = values as API.Product;
          const success = await handleUpdate(p);
          if (success) {
            actionRef.current?.reload();
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
          }
        }}
        formRef={updateFormRef}
      />
    </PageContainer>
  );
};

export default Product;
