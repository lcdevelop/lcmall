import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // 拂晓蓝
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: '在app.tsx里配',
  pwa: false,
  logo: 'https://zsenselink.oss-cn-beijing.aliyuncs.com/lcsays/gglogo1.png',
  iconfontUrl: '',
};

export default Settings;
