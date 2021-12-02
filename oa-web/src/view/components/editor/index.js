import editor from '@/view/components/editor/editor';
// 这里是重点
const Editor = {
    install: function(Vue){
        Vue.component('Editor',editor)
    }
}

// 导出组件
export default Editor
