/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentWxUser?: API.WxUser | undefined }) {
  const { currentWxUser } = initialState || {};
  return {
    canAdmin: currentWxUser && currentWxUser.role === 'admin',
    marketing: currentWxUser && currentWxUser.sessionWxApp.type === 'marketing',
    ec: currentWxUser && currentWxUser.sessionWxApp.type === 'ec',
  };
}
