import React from "react";
import {ModalForm, ProFormDateTimePicker, ProFormSwitch, ProFormText} from "@ant-design/pro-form";
import type {ProFormInstance} from "@ant-design/pro-form/lib/BaseForm";

export type StockFormProps = {
  title: string;
  visible: boolean;
  values: Partial<API.FavorStocksCreateRequest>;
  onCancel: () => void;
  onSubmit: (values: Partial<API.FavorStocksCreateRequest>) => any;
  formRef: React.MutableRefObject<ProFormInstance<API.FavorStocksCreateRequest> | undefined>;
  isNew: boolean;
};


const StockForm: React.FC<StockFormProps> = (props) => {

  return (
    <ModalForm
      width={400}
      title={props.title}
      visible={props.visible}
      onFinish={async (values) => {
        await props.onSubmit(values);
      }}
      modalProps={{
        onCancel: () => {
          props.onCancel()
        },
      }}
      formRef={props.formRef}
    >
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="stockName"
        label="批次名"
        initialValue={'test'}
      />
      <ProFormDateTimePicker
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="availableBeginTime"
        label="可用时间-开始时间"
        initialValue={"2021-10-29 00:00:00"}
      />
      <ProFormDateTimePicker
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="availableEndTime"
        label="可用时间-结束时间"
        initialValue={"2021-11-29 00:00:00"}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="maxCoupons"
        label="发放总上限"
        initialValue={10}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="maxAmount"
        label="总预算"
        initialValue={100}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="maxCouponsPerUser"
        label="单个用户可领个数"
        initialValue={1}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="couponAmount"
        label="面额(单位：分)"
        initialValue={10}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="transactionMinimum"
        label="使用券金额门槛(单位：分)"
        initialValue={20}
      />
      <ProFormSwitch
        width="md"
        name="naturalPersonLimit"
        label="是否开启自然人限制"
        initialValue={false}
      />
      <ProFormSwitch
        width="md"
        name="preventApiAbuse"
        label="是否开启防刷拦截"
        initialValue={false}
      />
      <ProFormSwitch
        width="md"
        name="noCash"
        label="营销经费"
        initialValue={false}
      />
      <ProFormText
        rules={[
          {
            required: true,
            message: '必填项',
          },
        ]}
        width="md"
        name="outRequestNo"
        label="商户单据号"
        initialValue={"test_lichuang_"}
      />
    </ModalForm>
  )
};

export default StockForm;
