import storejs from 'storejs'

const TokenKey = 'Authorization';
const AdminInfoKey = 'AdminUserInfo';
export function removeAll() {
  storejs.clear();
}

//token
export function getToken() {
  var token = storejs.get(TokenKey);
  if(token === undefined || token ==  null|| token === ''){
    return false;
  }
  return token;
}



export function setToken(token) {
  return storejs.set(TokenKey,token);
}
export function removeToken() {
  return storejs.remove(TokenKey);
}



//用户信息
export function setAdminInfo(data) {
  return storejs.set(AdminInfoKey,data);
}
export function getAdminInfo() {
  return storejs.get(AdminInfoKey);
}
export function removeAdminInfo() {
  return storejs.remove(AdminInfoKey);
}
