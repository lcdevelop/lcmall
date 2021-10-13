import React, {useEffect, useRef, useState} from 'react';
import {Button, message, Popconfirm, Modal} from 'antd';
import ProCard, {StatisticCard} from '@ant-design/pro-card';
import {
  startStock,
  pauseStock,
  restartStock,
  stockDetail,
  generateMaLink,
  updateWxMarketingStock,
  getWxMarketingStockByStockId
} from "@/services/manager/marketing/api";
import {useModel} from "@@/plugin-model/useModel";
import {ModalForm, ProFormInstance, ProFormText} from "@ant-design/pro-form";
import styles from './index.less';

const { Divider } = ProCard;

export type StockDetailProps = {
  match: any;
};

const StockDetail: React.FC<StockDetailProps> = (props) => {

  const { initialState } = useModel('@@initialState');
  const [stock, setStock] = useState<API.Stock>();
  const [cardId, setCardId] = useState<string>();
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const updateFormRef = useRef<ProFormInstance<API.WxMarketingStock>>();

  useEffect(() => {
    fetchStockDetail();
  }, [])

  const fetchStockDetail = () => {
    stockDetail(
      {
        stockId: props.match.params.stockId
      }).then((res: API.Response<API.Stock>) => {
      if (res.code === 200) {
        setStock(res.data!);
        fetchCardID(res.data!.stockId);
      }
    })
  }

  const fetchCardID = (stockId: string) => {
    getWxMarketingStockByStockId({stockId: stockId}).then((res:API.Response<API.WxMarketingStock>) => {
      if (res.code === 200) {
        setCardId(res.data!.cardId);
      }
    })
  }

  const onStartStock = () => {
    if (undefined !== stock && null !== stock.stockId) {
      startStock({stockId: stock.stockId}).then(res => {
        if (res.code === 200) {
          message.success('成功激活').then();
          fetchStockDetail();
        } else {
          message.error(res.msg).then();
        }
      })
    }
  }

  const onPauseStock = () => {
    if (undefined !== stock && null !== stock.stockId) {
      pauseStock({stockId: stock.stockId}).then(res => {
        if (res.code === 200) {
          message.success('成功暂停').then();
          fetchStockDetail();
        } else {
          message.error(res.msg).then();
        }
      })
    }
  }

  const onRestartStock = () => {
    if (undefined !== stock && null !== stock.stockId) {
      restartStock({stockId: stock.stockId}).then(res => {
        if (res.code === 200) {
          message.success('成功重启').then();
          fetchStockDetail();
        } else {
          message.error(res.msg).then();
        }
      })
    }
  }

  const onGenerateMaLink = () => {
    if (cardId === undefined || cardId === null) {
      message.warning('请先完善卡包ID信息').then();
      return;
    }

    if (undefined !== stock && null !== stock.stockId) {
      generateMaLink({appId: initialState?.currentWxUser?.sessionWxApp.appId!, stockId: stock.stockId}).then(res => {
        if (res.code === 200) {
          Modal.info({
            title: '生成链接成功',
            content: (
              <div>
                <p>如下是此批次专属链接，请保存好，可用于短信发送</p>
                <p>{res.data}</p>
              </div>
            )
          })
        } else {
          message.error(res.msg).then();
        }
      })
    }
  }

  const handleUpdateInfo = (ms: API.WxMarketingStock) => {
    return updateWxMarketingStock(ms);
  }

  const onCancelUpdateInfo = () => {
    setShowUpdateModal(false);
  }

  if (undefined === stock) {
    return (
      <div>loading...</div>
    )
  }

  return (
    <div>
      <Popconfirm
        title="确定要激活?"
        okText="Yes"
        cancelText="No"
        onConfirm={onStartStock}
        disabled={stock?.status==='paused'||stock?.status==='running'}
      >
        <Button type={"primary"} disabled={stock?.status==='paused'||stock?.status==='running'}>激活</Button>
      </Popconfirm>

      <span style={{marginLeft: '10px'}} />

      <Popconfirm
        title="确定要暂停?"
        okText="Yes"
        cancelText="No"
        onConfirm={onPauseStock}
      >
        <Button type={"primary"}>暂停</Button>
      </Popconfirm>

      <span style={{marginLeft: '10px'}} />

      <Popconfirm
        title="确定要重启?"
        okText="Yes"
        cancelText="No"
        onConfirm={onRestartStock}
      >
        <Button type={"primary"}>重启</Button>
      </Popconfirm>

      <span style={{marginLeft: '10px'}} />
      <Button type={"primary"} onClick={onGenerateMaLink}>生成链接</Button>

      <Divider />
      <ProCard.Group>
        <StatisticCard>
          <div><span className={styles.valueLabel}>批次号</span><span>{stock?.stockId}</span></div>
          <div><span className={styles.valueLabel}>创建批次的商户号</span><span>{stock?.stockCreatorMchid}</span></div>
          <div><span className={styles.valueLabel}>批次名称</span><span>{stock?.stockName}</span></div>
          <div><span className={styles.valueLabel}>批次状态</span><span>{stock?.status}</span></div>
          <div><span className={styles.valueLabel}>创建时间</span><span>{stock?.create_time}</span></div>
          <div><span className={styles.valueLabel}>使用说明</span><span>{stock?.description}</span></div>
          <div><span className={styles.valueLabel}>发放总上限</span><span>{stock?.stockUseRule.maxCoupons}</span></div>
          <div><span className={styles.valueLabel}>总预算(单位：分)</span><span>{stock?.stockUseRule.maxAmount}</span></div>
        </StatisticCard>
        <Divider type={'vertical'} />
        <StatisticCard>
          <div><span className={styles.valueLabel}>单天发放上限金额(单位：分)</span><span>{stock?.stockUseRule.maxAmountByDay}</span></div>
          <div><span className={styles.valueLabel}>面额(单位：分)</span><span>{stock?.stockUseRule.fixedNormalCoupon.couponAmount}</span></div>
          <div><span className={styles.valueLabel}>门槛(单位：分)</span><span>{stock?.stockUseRule.fixedNormalCoupon.transactionMinimum}</span></div>
          <div><span className={styles.valueLabel}>单个用户可领个数</span><span>{stock?.stockUseRule.maxCouponsPerUser}</span></div>
          <div><span className={styles.valueLabel}>可用开始时间</span><span>{stock?.availableBeginTime}</span></div>
          <div><span className={styles.valueLabel}>可用结束时间</span><span>{stock?.availableEndTime}</span></div>
          <div><span className={styles.valueLabel}>已发券数量</span><span>{stock?.distributedCoupons}</span></div>
        </StatisticCard>
      </ProCard.Group>

      <Divider />
      <ProCard.Group>
        <StatisticCard>
          <div><span className={styles.valueLabel}>卡包ID</span><span>
            {cardId ?
              cardId
            :
              "未完善"
            }
          </span></div>
          <Divider />
          <Button type={"primary"} onClick={() => {setShowUpdateModal(true)}}>完善信息</Button>
        </StatisticCard>
        <Divider type={'vertical'} />
        <StatisticCard>
        </StatisticCard>
        <Divider type={'vertical'} />
        <StatisticCard>
        </StatisticCard>
      </ProCard.Group>

      <ModalForm
        title='录入信息'
        visible={showUpdateModal}
        onFinish={
          async (values) => {
            const ms = values as API.WxMarketingStock;
            const res = await handleUpdateInfo(ms);
            if (res.code === 200) {
              if (stock) {
                fetchCardID(stock.stockId);
              }
              setShowUpdateModal(false);
              updateFormRef.current?.resetFields();
            } else {
              message.error(res.msg);
            }
          }
        }
        modalProps={{
          onCancel: () => onCancelUpdateInfo(),
        }}
        formRef={updateFormRef}
      >
        <ProFormText
          hidden
          name="stockId"
          initialValue={stock?.stockId}
        />
        <ProFormText
          rules={[
            {
              required: true,
              message: '卡包ID为必填项',
            },
          ]}
          width="md"
          name="cardId"
          placeholder='请输入卡包ID'
        />
      </ModalForm>
    </div>
  );
};

export default StockDetail;
