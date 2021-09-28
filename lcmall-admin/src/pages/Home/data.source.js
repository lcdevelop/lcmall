import React from 'react';
export const Nav20DataSource = {
  isScrollLink: true,
  wrapper: { className: 'header2 home-page-wrapper' },
  page: { className: 'home-page' },
  logo: {
    className: 'header2-logo ktwgdm1ygxq-editor_css',
    children:
      'https://zsenselink.oss-cn-beijing.aliyuncs.com/lcsays/gglogo1.png',
  },
  LinkMenu: {
    className: 'header2-menu',
    children: [
      {
        name: 'linkNav',
        to: 'https://www.lcsays.com',
        children: '博客',
        className: 'menu-item',
      },
      {
        name: 'linkNav~ktwgep3uzu',
        to: '/#welcome',
        children: '免费试用',
        className: 'menu-item',
      },
    ],
  },
  mobileMenu: { className: 'header2-mobile-menu' },
};
export const Banner00DataSource = {
  wrapper: { className: 'banner0 ktwf12exq1i-editor_css' },
  textWrapper: { className: 'banner0-text-wrapper' },
  title: {
    className: 'banner0-title kt9g04lq30j-editor_css',
    children:
      'https://zsenselink.oss-cn-beijing.aliyuncs.com/lcsays/gglogo1.png',
  },
  content: {
    className: 'banner0-content',
    children: (
      <span>
        <span>
          <span>
            <span>
              <p>立创云：量身定制类电商小程序快速创建</p>
            </span>
          </span>
        </span>
      </span>
    ),
  },
  button: {
    className: 'banner0-button',
    children: 'Learn More',
    href: '#welcome',
  },
};
export const Content00DataSource = {
  wrapper: { className: 'home-page-wrapper content0-wrapper' },
  page: { className: 'home-page content0' },
  OverPack: { playScale: 0.3, className: '' },
  titleWrapper: {
    className: 'title-wrapper',
    children: [{ name: 'title', children: '产品与服务' }],
  },
  childWrapper: {
    className: 'content0-block-wrapper',
    children: [
      {
        name: 'block0',
        className: 'content0-block',
        md: 8,
        xs: 24,
        children: {
          className: 'content0-block-item',
          children: [
            {
              name: 'image',
              className: 'content0-block-icon',
              children:
                'https://zos.alipayobjects.com/rmsportal/WBnVOjtIlGWbzyQivuyq.png',
            },
            {
              name: 'title',
              className: 'content0-block-title',
              children: (
                <span>
                  <span>
                    <p>量身定制类电商解决方案</p>
                  </span>
                </span>
              ),
            },
            {
              name: 'content',
              children: (
                <span>
                  <span>
                    <p>集量身检测、定制选购于一体的一站式小程序</p>
                  </span>
                </span>
              ),
            },
          ],
        },
      },
      {
        name: 'block1',
        className: 'content0-block',
        md: 8,
        xs: 24,
        children: {
          className: 'content0-block-item',
          children: [
            {
              name: 'image',
              className: 'content0-block-icon',
              children:
                'https://zos.alipayobjects.com/rmsportal/YPMsLQuCEXtuEkmXTTdk.png',
            },
            {
              name: 'title',
              className: 'content0-block-title',
              children: (
                <span>
                  <span>
                    <span>
                      <p>模板化，快速构建</p>
                    </span>
                  </span>
                </span>
              ),
            },
            {
              name: 'content',
              children: (
                <span>
                  <span>
                    <p>小程序、公众号等模板化开发，灵活扩展</p>
                  </span>
                </span>
              ),
            },
          ],
        },
      },
      {
        name: 'block2',
        className: 'content0-block',
        md: 8,
        xs: 24,
        children: {
          className: 'content0-block-item',
          children: [
            {
              name: 'image',
              className: 'content0-block-icon',
              children:
                'https://zos.alipayobjects.com/rmsportal/EkXWVvAaFJKCzhMmQYiX.png',
            },
            {
              name: 'title',
              className: 'content0-block-title',
              children: (
                <span>
                  <p>集成后台管理系统</p>
                </span>
              ),
            },
            {
              name: 'content',
              children: (
                <span>
                  <span>
                    <p>商品管理、客户管理，一目了然</p>
                  </span>
                </span>
              ),
            },
          ],
        },
      },
    ],
  },
};
export const Content50DataSource = {
  wrapper: { className: 'home-page-wrapper content5-wrapper' },
  page: { className: 'home-page content5' },
  OverPack: { playScale: 0.3, className: '' },
  titleWrapper: {
    className: 'title-wrapper',
    children: [
      { name: 'title', children: '客户案例', className: 'title-h1' },
      {
        name: 'content',
        className: 'title-content',
        children: (
          <span>
            <span>
              <p>以下基于立创云构建</p>
            </span>
          </span>
        ),
      },
    ],
  },
  block: {
    className: 'content5-img-wrapper',
    gutter: 16,
    children: [
      {
        name: 'block0',
        className: 'block',
        md: 6,
        xs: 24,
        children: {
          wrapper: { className: 'content5-block-content' },
          img: {
            children:
              'https://zsenselink.oss-cn-beijing.aliyuncs.com/lcsays/gg20210922164150.png',
          },
          content: {
            children: (
              <span>
                <p>
                  中微万序<br />
                </p>
              </span>
            ),
          },
        },
      },
    ],
  },
};
export const Footer00DataSource = {
  wrapper: { className: 'home-page-wrapper footer0-wrapper' },
  OverPack: { className: 'home-page footer0', playScale: 0.05 },
  copyright: {
    className: 'copyright',
    children: (
      <span>
        <span>
          <span>
            <span>©2021 lcsays.com All Rights Reserved</span>
          </span>
        </span>
      </span>
    ),
  },
};
