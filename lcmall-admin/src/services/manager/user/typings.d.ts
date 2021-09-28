// @ts-ignore
/* eslint-disable */

declare namespace API {

  type WxApp = {
    id: number;
    name: string;
    appId: string;
    image: string;
    wxaCodeUrl: string;
    qrCodePictureUrl: string;
  }

  type WxUser = {
    id: number;
    wxApp: WxApp;
    sessionWxApp: WxApp;
    nickName: string;
    avatarUrl: string;
    gender: string;
    country: string;
    city: string;
    language: string;
    role: string;
  }
}
