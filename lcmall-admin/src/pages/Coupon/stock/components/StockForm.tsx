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
        initialValue={"2021-10-09 00:00:00"}
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
        initialValue={"2021-10-31 00:00:00"}
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
        label="maxCoupons发放总上限(个)"
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
        label="maxAmount总预算(单位：分)"
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
        name="maxAmountByDay"
        label="maxAmountByDay单天预算发放上限(单位：分)"
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
        name="maxCouponsPerUser"
        label="maxCouponsPerUser单个用户可领个数"
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
        name="couponAmount"
        label="couponAmount面额(单位：分)"
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
        initialValue={10}
      />
      <ProFormSwitch
        width="md"
        name="naturalPersonLimit"
        label="是否开启自然人限制"
        initialValue={true}
      />
      <ProFormSwitch
        width="md"
        name="preventApiAbuse"
        label="是否开启防刷拦截"
        initialValue={true}
      />
      <ProFormSwitch
        width="md"
        name="noCash"
        label="营销经费"
        initialValue={false}
      />
    </ModalForm>
  )
};

export default StockForm;
