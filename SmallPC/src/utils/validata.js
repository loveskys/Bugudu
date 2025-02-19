import { ElMessage } from 'element-plus'

//校验id列表长度
export function IdsLength(ids) {
  let b = ids.length>0
  if(!b){
    Message({message: '请选择',type: 'error',duration: 1000});
    return false;
  }
  return true;
}

/*合法url检查*/
export function URL(urltext) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(urltext)
}

/*小写字母*/
export function LowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/*大写字母*/
export function UpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/*大小写字母*/
export function Alphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/*对比两个对象的值是否相等, 相等返回false */
export function TwoObj(obja,objb) {
  let a = JSON.stringify(obja);
  let b = JSON.stringify(objb);
  if(a == b) {
      ElMessage.error({message: '未修改',duration: 1000});
      return false;
  }else{
    return true;
  }
}

//检查对象每个字段是否完整, 完整返回true
export function Obj(params) {
  for(var key in params){
    if(params[key] != '0' && !params[key]){
      console.log(key)
      ElMessage.error({message: '信息未填写完整',duration: 1000});
      return false;
    }
  }
  return true;
}

//对list数据分组
export function groupBy(array,f) {
  const groups = {};
  array.forEach(function (o) {
    const group = JSON.stringify(f(o));
    groups[group] = groups[group] || [];
    groups[group].push(o);
  });
  return Object.keys(groups).map(function (group) {
    return groups[group];
  });
}