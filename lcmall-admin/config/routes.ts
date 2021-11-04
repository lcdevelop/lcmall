export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { path: '/user', routes: [{ name: '登录', path: '/user/login', component: './user/Login' }] },
    ],
  },

  { path: '/welcome', name: '概览', icon: 'home', component: './Welcome' },
  {
    path: '/product',
    access: 'ec',
    name: '商品管理',
    icon: 'shop',

    routes: [
      { path: '/product/category', name: '品类管理', component: './Product/category' },
      { path: '/product/product', name: '商品管理', component: './Product/product' },
      { path: '/product/sku', name: '可售卖项管理', component: './Product/sku' },
    ],
  },
  {
    path: '/trade',
    access: 'ec',
    name: '交易管理',
    icon: 'creditCard',
    routes: [
      { path: '/trade/order', name: '订单管理', component: './Trade/order' },
    ],
  },
  {
    path: '/marketing',
    access: 'marketing',
    name: '营销管理',
    icon: 'creditCard',
    routes: [
      { path: '/marketing/stock/list', name: '代金券批次(stock)', component: './Coupon/stockList'},
      { path: '/marketing/stock/data', name: '代金券批次(全部)', component: './Coupon/stock', hideInMenu: true},
      { path: '/marketing/stock/detail/:stockId', name: '代金券批次详情', component: './Coupon/stockDetail', hideInMenu:true},
      { path: '/marketing/activity/list', name: '活动(activity)', component: './Coupon/activity'},
      { path: '/marketing/whitelist', name: '白名单', component: './Coupon/whitelist'},
      { path: '/marketing/statistics/:activityId', name: '数据统计', component: './Coupon/statistics', hideInMenu:true},
      { path: '/marketing/trendstat/:activityId', name: '趋势统计', component: './Coupon/trendStat', hideInMenu:true},
      { path: '/marketing/flowstat/:activityId', name: '日维度统计', component: './Coupon/flowstat', hideInMenu:true},
      { path: '/marketing/system', name: '系统设置', component: './Coupon/system'},
    ],
  },
  { path: '/users', access: 'canAdmin', name: '用户管理(admin)', icon: 'user', component: './user/user' },
  { path: '/', layout: false, component: './Home' },
  // { path: '/', redirect: '/welcome' },
  { component: './404' },

];
