import React from 'react';
import {useModel} from "@@/plugin-model/useModel";
import {Image} from 'antd';

import styles from './index.less';

export type HeaderInfoProps = {
};

const HeaderInfo: React.FC<HeaderInfoProps> = (props) => {
  const { initialState } = useModel('@@initialState');

  return (
    <div className={styles.info}>
      <Image className={styles.wxaCode} src={initialState?.currentWxUser?.sessionWxApp?.wxaCodeUrl} />
      <span className={styles.appid}>{initialState?.currentWxUser?.sessionWxApp?.appId}</span>
    </div>
  );
};

export default HeaderInfo;
