import Cookies from "js-cookie";

const TokenKey = "token";

export function getToken() {
  return Cookies.get(TokenKey);
}

export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  return Cookies.remove(TokenKey);
}
/**
 * 处理子菜单的递归方法
 */
export function subMenuList(subMenu) {
  const arr = [];
  if (subMenu && subMenu.length > 0) {
    for (let i = 0; i < subMenu.length; i++) {
      const obj = {};
      obj.path = subMenu[i].path;
      obj.name = subMenu[i].name;
      obj.redirect = subMenu[i].redirect;
      obj.meta = JSON.parse(subMenu[i].meta);
      const component = subMenu[i].component;
      // 使用模板字符串
      obj.component = (resolve) => require([`@/views/${component}`], resolve);   
      //子菜单
      obj.children = subMenuList(subMenu[i].children);
      arr.push(obj);
    }
    return arr;
  }
}
