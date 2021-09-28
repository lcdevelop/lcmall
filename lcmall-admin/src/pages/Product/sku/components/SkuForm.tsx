import React, {useEffect, useState} from "react";
import {ModalForm, ProFormDigit, ProFormSelect, ProFormText, ProFormUploadButton} from "@ant-design/pro-form";
import type {ProFormInstance} from "@ant-design/pro-form/lib/BaseForm";
import {UploadFile} from "antd/es/upload/interface";

export type SkuFormProps = {
  title: string;
  visible: boolean;
  products: API.Product[];
  values: Partial<API.Sku>;
  onCancel: () => void;
  onSubmit: (values: Partial<API.Sku>) => any;
  formRef: React.MutableRefObject<ProFormInstance<API.Sku> | undefined>;
  isNew: boolean;
};


const SkuForm: React.FC<SkuFormProps> = (props) => {

  const [fileList, setFileList] = useState<UploadFile<API.Response<string>>[]>([]);

  useEffect( () => {
    // 如果是编辑操作，则展示上传图片
    if (props.values.image) {
      setFileList([{
        uid: '-1',
        name: 'xxx.png',
        status: 'done',
        thumbUrl: props.values.image,
      }])
    } else {
      setFileList([]);
    }
  }, [props.values])

  return (
    <ModalForm
      width={400}
      title={props.title}
      visible={props.visible}
      onFinish={async (values) => {
        const ret = await props.onSubmit(values);
        if (ret) {
          setFileList([]);
        }
      }}
      modalProps={{
        onCancel: () => {
          props.onCancel()
          setFileList([]);
        },
      }}
      formRef={props.formRef}
    >
      <ProFormText
        hidden
        name="id"
        initialValue={props.values.id}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '可售卖项为必填项',
          },
        ]}
        width="md"
        name="name"
        label="商品名"
        placeholder='请输入可售卖项名'
        initialValue={props.values.name}
      />
      <ProFormSelect
        name='product_id'
        label='商品'
        request={async () => props.products.map(value => {
               return {label: value.name, value: value.id}
             })}
        placeholder='请选择一个商品'
        rules={[{required: true, message: '请选择一个商品'}]}
        initialValue={props.values.product?.id}
      />
      <ProFormUploadButton
        rules={[
          {
            required: false,
          },
        ]}
        name="image"
        label="展示图片"
        max={1}
        fieldProps={{
          name: 'image',
          listType: 'picture-card',
        }}
        action="/api/manager/uploadImage"
        extra="请上传一张图片"
        fileList={fileList}
        initialValue={props.isNew ? []: fileList}
        onChange={(e) => {
          setFileList(e.fileList);
        }}

      />
      <ProFormDigit
        rules={[
          {
            required: true,
            message: '价格为必填项',
          },
        ]}
        width="md"
        name="price_value"
        label="价格（单位：分）"
        placeholder='请输入价格'
        initialValue={props.values.price?.price}
      />
      <ProFormSelect
        name="specs"
        label="属性标签"
        mode='tags'
        initialValue={props.values.specList != null ? props.values.specList : []}
      />
    </ModalForm>
  )
};

export default SkuForm;
