// @ts-ignore
/* eslint-disable */

declare namespace API {
  type Category = {
    id: number;
    name: string;
    wxAppId: number;
  }

  type Product = {
    id: number;
    name: string;
    description: string;
    category: Category;
    categoryId?: number; // 辅助
  }

  // type SkuSpec = {
  //   id: number;
  //   field: string;
  //   value: string;
  // }

  type Price = {
    id: number;
    policy: number;
    price: number;
  }

  type Sku = {
    id: number;
    name: string;
    image: string;
    product: Product;
    product_id?: number; // 辅助
    price: Price;
    price_value: number; // 辅助
    price_id?: number; // 辅助
    specs: string;
    specList: string[];
  }
}
