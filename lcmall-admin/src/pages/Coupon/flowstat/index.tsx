import React, {useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import {DatePicker, Divider} from "antd";
import {flowStatistics} from "@/services/manager/marketing/api";
import {StatisticCard} from "@ant-design/pro-card";

export type FlowStatProps = {
  match: any;
};

const FlowStat: React.FC<FlowStatProps> = (props: FlowStatProps) => {

  const [statData, setStatData] = useState<API.FlowStatistics>();

  const onChange = (value: any, dateStr: string) => {
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

  return (
    <PageContainer>
      <DatePicker onChange={onChange} />

      <StatisticCard.Group title='核心指标' direction='row'>
        <StatisticCard
          statistic={{
            title: '当日pv',
            value: statData?.pv || '无',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日uv',
            value: statData?.uv || '无',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日领取按钮点击次数',
            value: statData?.clickPv || '无',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
        <StatisticCard
          statistic={{
            title: '当日领取按钮点击人数',
            value: statData?.clickUv || '无',
            precision: 0,
          }}
        />
        <Divider type={'vertical'} />
      </StatisticCard.Group>
    </PageContainer>
  );
};

export default FlowStat;
