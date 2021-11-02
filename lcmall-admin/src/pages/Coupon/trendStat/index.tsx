import React, {useEffect, useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import ReactEcharts from 'echarts-for-react';
import {trendStat} from "@/services/manager/marketing/api";

export type TrendStatProps = {
  match: any;
};

const TrendStat: React.FC<TrendStatProps> = (props: TrendStatProps) => {

  const [date, setDate] = useState<string[]>();
  const [data, setData] = useState<number[]>();

  useEffect(() => {
    trendStat({activityId: props.match.params.activityId}).then(res => {
      if (res.code === 200) {
        setDate(res.data!.keys);
        setData(res.data!.values);
      }
    })
  }, [])

  const getOption = () => {
    return {
      tooltip: {
        trigger: 'axis',
        position: function (pt: any) {
          return [pt[0], '10%'];
        }
      },
      title: {
        left: 'center',
        text: '活动ID:' + props.match.params.activityId + ' 领券趋势统计'
      },
      toolbox: {
        feature: {
          dataZoom: {
            yAxisIndex: 'none'
          },
          restore: {},
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: date
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%']
      },
      dataZoom: [
        {
          type: 'inside',
          start: 0,
          end: 100
        },
        {
          start: 0,
          end: 100
        }
      ],
      series: [
        {
          name: '领券数',
          type: 'line',
          symbol: 'circle',
          sampling: 'lttb',
          itemStyle: {
            color: 'rgb(255, 70, 131)'
          },
          data: data
        }
      ]
    };
  }

  const onEvents = () => {

  }

  return (
    <PageContainer>
      <ReactEcharts
        option={getOption()}
        notMerge={true}
        lazyUpdate={true}
        onEvents={{
          'click': onEvents,
        }}
        style={{width: '100%',height:'30vw'}}
      />
    </PageContainer>
  );
};

export default TrendStat;
