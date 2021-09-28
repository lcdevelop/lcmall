import React, {useEffect, useState} from "react";
import {ModalForm, ProFormSelect, ProFormText} from "@ant-design/pro-form";
import type {ProFormInstance} from "@ant-design/pro-form/lib/BaseForm";
import {expressTypes as getExpressTypes} from "@/services/manager/trade/api";

export enum OpType {
  OP_MOD_PRICE,
  OP_SEND_EXPRESS,
}

export type OrderFormProps = {
  title: string;
  visible: boolean;
  values: Partial<API.Order>;
  onCancel: () => void;
  onSubmit: (values: Partial<API.Order>) => any;
  formRef: React.MutableRefObject<ProFormInstance<API.Order> | undefined>;
  opType: OpType,
};

const OrderForm: React.FC<OrderFormProps> = (props) => {

  const [expressTypes, setExpressTypes] = useState<API.ExpressType[]>([]);

  useEffect(() => {
    getExpressTypes().then((res) => {
      if (res.code === 200) {
        setExpressTypes(res.data!);
      }
    })
  }, []);

  if (props.opType === OpType.OP_MOD_PRICE) {
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
              message: '价格为必填项',
            },
          ]}
          label='价格(单位：分)'
          width="md"
          name="totalFee"
          placeholder='请输入价格'
          initialValue={props.values.totalFee}
        />
      </ModalForm>
    )
  }

  if (props.opType === OpType.OP_SEND_EXPRESS) {
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
        <ProFormSelect
          name='expressTypeId'
          showSearch
          label='快递公司'
          request={async () => expressTypes.map(value => {
            return {label: value.name, value: value.id}
          })}
          placeholder='请选择一个快递公司'
          rules={[{required: true, message: '请选择一个快递公司'}]}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: '快递单号为必填项',
            },
          ]}
          width="md"
          name="expressNo"
          label="快递单号"
          placeholder='请输入快递单号'
        />
      </ModalForm>
    )
  }

  return <div />;
};

export default OrderForm;
