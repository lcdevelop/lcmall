import React, {useEffect, useState} from 'react';
import {appList, checkLogined, refreshSession} from "@/services/manager/user/api";
import { history } from 'umi';
import {Col, Row, Image, Card, message, Modal} from "antd";
import Meta from "antd/es/card/Meta";
import styles from './index.less';
import {HomeOutlined, PlusOutlined} from "@ant-design/icons";

const Login: React.FC = () => {

  const [appListState, setAppListState] = useState<API.WxApp[]>([]);
  const [showAppIndexState, setShowAppIndexState] = useState<number>(-1);

  const tryLogin = () => {
    checkLogined().then(res => {
      if (res.code === 200) {
        const {pathname, query} = history.location;
        if (pathname === '/user/login') {
          let {redirect} = query as {
            redirect: string;
          };
          if (redirect === undefined) {
            redirect = '/'
          }
          window.location.href = `/`;
        }
      } else if (res.code === 407) {
        message.error('您没有权限').then();
        setTimeout(tryLogin, 1000);
      } else {
        setTimeout(tryLogin, 1000);
      }
    }, () => {
      message.error('服务器发生异常').then();
    });
  }

  useEffect(() => {
    appList().then((res) => {
      if (res.code === 200) {
        setAppListState(res.data!);
      } else {
        message.error(res.msg).then();
      }
    });

    tryLogin();
  }, []);

  const addApp = () => {
    Modal.info({
      title: '扫码加微信(备注：新建app)，由工程师为您配置',
      content: (
        <div>
          <img alt={''} className={styles.addAppQrCode} src={'https://zsenselink.oss-cn-beijing.aliyuncs.com/lcsays/gg20210922151051.png'} />
        </div>
      ),
      onOk() {},
    });
  }

  const onRefreshSession = () => {
    refreshSession().then(res => {
      console.log(res);
      window.location.reload();
    });
  }

  return (
    <div className={styles.container}>
      <div className={styles.home} >
        <a href={'/'} style={{color: 'black'}}>
          <HomeOutlined />
          <span className={styles.homeText}>首页</span>
        </a>
      </div>

      <Row>
      {appListState.map((value, index) => {
        return (
          <Col key={value.id} span={4}>
            <Card
              bordered={true}
              hoverable
              className={styles.card}
              cover={<img alt="example" className={styles.cardImage} src={value.image} />}
              onClick={() => setShowAppIndexState(index)}
            >
              <Meta title={`企业:${value.name}`} />
            </Card>
          </Col>
        )
      })}
        <Col span={3}>
          <Card
            hoverable
            className={styles.card}
            cover={<PlusOutlined className={styles.addAppBtn} />}
            onClick={addApp}
          >
            <Meta description={'点击创建新App'} />
          </Card>
        </Col>
      </Row>

      {showAppIndexState > -1
        ?
        <div>
          <Image
            preview={false}
            width={200}
            src={appListState.at(showAppIndexState)?.qrCodePictureUrl}
          />
          <div>扫码关注公众号登录：{appListState.at(showAppIndexState)?.name}</div>
          <div>登录不上或换微信号，<a onClick={onRefreshSession}>点此刷新</a></div>
        </div>
        :""
      }

      <div>本网站仅支持电脑谷歌Chrome浏览器使用</div>

    </div>
  );
};

export default Login;
