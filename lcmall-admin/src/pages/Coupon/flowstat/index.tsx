import React, {useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {Button, DatePicker, Divider} from "antd";
import {downloadXls, flowStatistics} from "@/services/manager/marketing/api";
import {StatisticCard} from "@ant-design/pro-card";
import styles from './index.less';

export type FlowStatProps = {
  match: any;
};

const FlowStat: React.FC<FlowStatProps> = (props: FlowStatProps) => {

  const [statData, setStatData] = useState<API.FlowStatistics>();
  const [dateStr, setDateStr] = useState<string>('');

  const onChange = (value: any, dateStr: string) => {
    setStatData({
      pv: -1,
      uv: -1,
      clickUv: -1,
      clickPv:-1,
      consumeCount: -1,
    });
    setDateStr(dateStr);
    flowStatistics({
      activityId: props.match.params.activityId,
      dateStr: dateStr,
    }).then(res => {
      console.log(res);
      if (res.code === 200) {
        setStatData(res.data!);
      }
    })
  }

  const onDownloadXls = () => {
    downloadXls({
      activityId: props.match.params.activityId,
      dateStr: dateStr,
    }).then();
  }

  return (
    <PageContainer>
      <div>请选择日期</div>
      <DatePicker onChange={onChange} />

      <div style={{margin: "20px"}}></div>

      <StatisticCard.Group title='汇总数据' direction='row'>
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
            title: '当日领取按钮点击次数',
            value: statData?.clickPv === -1 ? 'loading' : statData?.clickPv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日领取按钮点击人数',
            value: statData?.clickUv === -1 ? 'loading' : statData?.clickUv || 'NA',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
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
        {dateStr !== '' ?
          <a className={styles.flowstatBtn} href={'/api/manager/marketing/downloadXls?activityId=' + props.match.params.activityId + '&dateStr=' + dateStr}>下载完整表格</a>
        :
          ""
        }

        <div style={{height: "50px"}} />
      </StatisticCard.Group>
    </PageContainer>
  );
};

export default FlowStat;
