import React, {useEffect, useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {DatePicker, Divider, message} from "antd";
import {flowStatistics} from "@/services/manager/marketing/api";
import {StatisticCard} from "@ant-design/pro-card";
import styles from './index.less';
import moment from "moment";

export type FlowStatProps = {
  match: any;
};

const FlowStat: React.FC<FlowStatProps> = (props: FlowStatProps) => {

  const [statData, setStatData] = useState<API.FlowStatistics>();

  useEffect(() => {
    const hide = message.loading('loading...');
    setStatData({
      totalClickPv: -1,
      totalClickUv: -1,
      totalConsumeCount: -1,
      totalCouponCount: -1,
      totalPv: -1,
      totalUv: -1,
      pv: -1,
      uv: -1,
      clickUv: -1,
      clickPv:-1,
      couponCount: -1,
      consumeCount: -1
    });
    const now = new Date();
    const str = now.getFullYear() + '-' + (now.getMonth()+1) + '-' + now.getDate();
    flowStatistics({
      activityId: props.match.params.activityId,
      dateStr: str,
    }).then(res => {
      console.log(res);
      if (res.code === 200) {
        setStatData(res.data!);
        hide();
      } else if (res.code === 700) {
        message.error(res.msg).then();
      }
    })
  }, [])

  const onChange = (value: any, str: string) => {
    const hide = message.loading('loading...');
    setStatData({
      totalClickPv: -1,
      totalClickUv: -1,
      totalConsumeCount: -1,
      totalCouponCount: -1,
      totalPv: -1,
      totalUv: -1,
      pv: -1,
      uv: -1,
      clickUv: -1,
      clickPv:-1,
      couponCount: -1,
      consumeCount: -1
    });
    flowStatistics({
      activityId: props.match.params.activityId,
      dateStr: str,
    }).then(res => {
      console.log(res);
      if (res.code === 200) {
        setStatData(res.data!);
        hide();
      } else if (res.code === 700) {
        message.error(res.msg).then();
      }
    })
  }

  return (
    <PageContainer>
      <StatisticCard.Group title='累计数据' direction='row'>
        <StatisticCard
          statistic={{
            title: 'pv',
            value: statData?.totalPv === -1 ? 'loading' : statData?.totalPv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: 'uv',
            value: statData?.totalUv === -1 ? 'loading' : statData?.totalUv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '按钮点击次数',
            value: statData?.totalClickPv === -1 ? 'loading' : statData?.totalClickPv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '按钮点击人数',
            value: statData?.totalClickUv === -1 ? 'loading' : statData?.totalClickUv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '领券数',
            value: statData?.totalCouponCount === -1 ? 'loading' : statData?.totalCouponCount || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '核销券数',
            value: statData?.totalConsumeCount === -1 ? 'loading' : statData?.totalConsumeCount || 'NA',
            precision: 0,
          }}
        />
      </StatisticCard.Group>

      <Divider />

      <DatePicker onChange={onChange} defaultValue={moment()}/>

      <div style={{margin: "20px"}}></div>

      <StatisticCard.Group title='当日数据' direction='row'>
        <StatisticCard
          statistic={{
            title: '当日pv',
            value: statData?.pv === -1 ? 'loading' : statData?.pv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日uv',
            value: statData?.uv === -1 ? 'loading' : statData?.uv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '按钮点击次数',
            value: statData?.clickPv === -1 ? 'loading' : statData?.clickPv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '按钮点击人数',
            value: statData?.clickUv === -1 ? 'loading' : statData?.clickUv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日领券数',
            value: statData?.couponCount === -1 ? 'loading' : statData?.couponCount || 'NA',
            precision: 0,
          }}
        />
        <StatisticCard
          statistic={{
            title: '当日核销券数',
            value: statData?.consumeCount === -1 ? 'loading' : statData?.consumeCount || 'NA',
            precision: 0,
          }}
        />
      </StatisticCard.Group>

      <Divider />

      <StatisticCard.Group title='明细数据' direction='row'>
        <a className={styles.flowstatBtn} href={'/api/manager/marketing/downloadXls?activityId=' + props.match.params.activityId}>下载完整表格</a>
        <div style={{height: "50px"}} />
      </StatisticCard.Group>
    </PageContainer>
  );
};

export default FlowStat;
