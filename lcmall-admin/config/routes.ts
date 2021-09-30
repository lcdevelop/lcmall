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
      { path: '/product/category', name: '品类管理', icon: 'smile', component: './Product/category' },
      { path: '/product/product', name: '商品管理', icon: 'smile', component: './Product/product' },
      { path: '/product/sku', name: '可售卖项管理', icon: 'smile', component: './Product/sku' },
    ],
  },
  {
    path: '/trade',
    access: 'ec',
    name: '交易管理',
    icon: 'creditCard',
    routes: [
      { path: '/trade/order', name: '订单管理', icon: 'smile', component: './Trade/order' },
    ],
  },
  {
    path: '/marketing',
    access: 'marketing',
    name: '营销管理',
    icon: 'creditCard',
    routes: [
      { path: '/marketing/stock/all', name: '代金券批次(全部)', icon: 'smile', component: './Coupon/stock'},
      // { path: '/marketing/stock/running', name: '代金券批次(运营中)', icon: 'smile', component: './Coupon/stock'},
      // { path: '/marketing/stock/unactivated', name: '代金券批次(草稿箱)', icon: 'smile', component: './Coupon/stock'},
      { path: '/marketing/stock/detail/:stockId', name: '代金券批次详情', icon: 'smile', component: './Coupon/stockDetail', hideInMenu:true},
    ],
  },
  { path: '/users', access: 'canAdmin', name: '用户管理(admin)', icon: 'user', component: './user/user' },
  { path: '/', layout: false, component: './Home' },
  // { path: '/', redirect: '/welcome' },
  { component: './404' },

];
