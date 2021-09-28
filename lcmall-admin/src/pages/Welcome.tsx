import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import {Card, Alert} from 'antd';
import {useModel} from "@@/plugin-model/useModel";

export default (): React.ReactNode => {
  const { initialState } = useModel('@@initialState');

  if (!initialState) {
    return <div>加载中</div>;
  }

  const { currentWxUser } = initialState;

  return (
    <PageContainer>
      <Card>
        <Alert
          message={`hi ${currentWxUser?.nickName}, 欢迎来到这里。`}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
      </Card>
    </PageContainer>
  );
};
