import React, {useEffect, useRef, useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {Button, Form, Input, message, Popconfirm} from "antd";
import ProForm, {ModalForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import {setCallbacks, getCallbacks} from "@/services/manager/marketing/api";
import {ProFormInstance} from "@ant-design/pro-form";

const System: React.FC = () => {

  const formRef = useRef<ProFormInstance<any>>();
  const [callbacksUrl, setCallbacksUrl] = useState<string>('');

  useEffect(() => {
    getCallbacks().then((res: API.Response<string>) => {
      if (res.code === 200) {
        setCallbacksUrl(res.data!);
      }
    })
  }, [])

  const onFinish = async (values: any) => {
    setCallbacks({
      notifyUrl: values.callbacks,
    }).then(res => {
      console.log(res);
      if (res.code === 200) {
        return true;
      } else {
        message.error(res.msg);
        return false;
      }
    })
  }

  return (
    <PageContainer>
      <ProForm
        onFinish={onFinish}
        submitter = {{
          render: (props, doms) => {
            return [
              <Popconfirm
                title="重要！！！请再三确认更新？"
                okText="Yes"
                cancelText="No"
                onConfirm={() => props.form?.submit?.()}
              >
                <Button type={"primary"} htmlType="submit">保存</Button>
              </Popconfirm>
            ]
          }
        }}
        formRef={formRef}
      >

        <ProFormText
          width="md"
          name="callbacks"
          label='核销回调url'
          placeholder='请技术人员配置'
        />
      </ProForm>
    </PageContainer>
  );
};

export default System;
