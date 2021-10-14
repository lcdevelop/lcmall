import React from "react";
import {ModalForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import type {ProFormInstance} from "@ant-design/pro-form/lib/BaseForm";

export type ProductFormProps = {
  title: string;
  visible: boolean;
  categories: API.Category[];
  values: Partial<API.Product>;
  onCancel: () => void;
  onSubmit: (values: Partial<API.Product>) => Promise<void>;
  formRef: React.MutableRefObject<ProFormInstance<API.Product> | undefined>;
};

const ProductForm: React.FC<ProductFormProps> = (props) => {
  return (
    <ModalForm
      title={props.title}
      visible={props.visible}
      onFinish={props.onSubmit}
      modalProps={{
        onCancel: () => props.onCancel(),
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
            message: '商品名称为必填项',
          },
        ]}
        width="md"
        name="name"
        placeholder='请输入商品名'
        initialValue={props.values.name}
      />
      <ProFormSelect
        name='categoryId'
        label='品类'
        request={async () => props.categories.map(value => {
               return {label: value.name, value: value.id}
             })}
        placeholder='请选择一个品类'
        rules={[{required: true, message: '请选择一个品类'}]}
        initialValue={props.values.category?.id}
      />
      <ProFormTextArea
        width="md"
        name="description"
        placeholder='请输入详细描述'
        initialValue={props.values.description}
      />
    </ModalForm>
  )
};

export default ProductForm;
