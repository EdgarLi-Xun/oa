// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ViewUI from 'view-design'
import i18n from '@/locale'
import config from '@/config'
import importDirective from '@/directive'
import { directive as clickOutside } from 'v-click-outside-x'
import { setAuthList, getAuthList } from '@/libs/util'
import axios from '@/libs/api.request'
import installPlugin from '@/plugin'
import './index.less'
import '@/assets/icons/iconfont.css'
import TreeTable from 'tree-table-vue'
import VOrgTree from 'v-org-tree'
import 'v-org-tree/dist/v-org-tree.css'
import Print from 'vue-print-nb'
import 'view-design/dist/styles/iview.css'
import Editor from '@/view/components/editor/editor';
Vue.use(Editor); //富文本编辑器
// 实际打包时应该不引入mock
/* eslint-disable */
 if (process.env.NODE_ENV !== 'production') require('@/mock')
Vue.use(Print); //注册打印控件
Vue.use(ViewUI, {
  i18n: (key, value) => i18n.t(key, value)
})
Vue.use(TreeTable)
Vue.use(VOrgTree)
/**
 * @description 注册admin内置插件
 */
installPlugin(Vue)
/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = false
/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config

/**
 * @description 检测权限
 * @param code 按钮编号
 */
Vue.prototype.hasPermission = function (code){
  let authList = getAuthList();
  if(!authList){
    axios.request({
        method: 'post',
        url: 'apis/user/getMenuAuthCode'
    }).then((response) => {
        setAuthList(JSON.stringify(response.data));
        authList = JSON.parse(getAuthList())
        if(authList[code]){
          return true;
        }else{
          return false;
        }
    }).catch((response) => {

    })
  }else{
    authList = JSON.parse(authList);
    if(authList[code]){
      return true;
    }else{
      return false;
    }
  }
}

/**
 * 注册指令
 */
importDirective(Vue)
Vue.directive('clickOutside', clickOutside)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  store,
  render: h => h(App)
})
