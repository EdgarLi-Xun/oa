<template>
    <div  style="height: 100%;width: 100%;overflow:hidden" >
        <Card shadow style="height: 100%;width: 100%;overflow:hidden">
            <div class="department-outer">
                <div class="tip-box">

                </div>
                <div class="zoom-box">
                    <zoom-controller v-model="zoom" :min="20" :max="200"></zoom-controller>
                </div>
                <div class="view-box">
                    <org-view
                        :data="data"
                        :zoom-handled="zoomHandled"
                        @on-menu-click="handleMenuClick">
                    </org-view>
                </div>
            </div>
        </Card>
        <!--新增或編輯彈窗-->
        <Modal :title="modalTitle" :show.sync="departmentModalIShow" @on-cancel="editData={}"  :width="900" ok-text="">
            <departmentEdit :editData="editData" :model="model"  @update-success="editComplete"></departmentEdit>
            <div slot="footer" v-if="model==='view'">
                <Button type="primary" size="large"   @click="closeModal">关闭</Button>
            </div> </Modal>
    </div>
</template>
<script>
import axios from '@/libs/api.request'
import OrgView from '@/view/components/org-tree/components/org-view.vue'
import ZoomController from '@/view/components/org-tree/components/zoom-controller.vue'
import { getOrgData } from '@/api/data'
import '@/view/components/org-tree/index.less'
import departmentEdit from './department-edit'
import Modal from '@/view/components/sega/modal'

export default {
    name: 'department',
    components: {
        OrgView,
        ZoomController,
        departmentEdit,
        Modal
    },
    data () {
        return {
            data: null,
            zoom: 100,
            departmentModalIShow: false,
            modalTitle: '编辑部门',
            model: 'view',
            editData: {},
        }
    },
    computed: {
        zoomHandled () {
            return this.zoom / 100
        }
    },
    methods: {
        closeModal() {
            this.departmentModalIShow = false;
        },
        //数据编辑完成
        editComplete (){
            this.getDepartmentData();
            this.departmentModalIShow = false;
        },
        //设置部门数据
        setDepartmentData (data) {
            data.isRoot = true
            return data
        },
        //菜单点击触发事件
        handleMenuClick ({ data, key }) {
            //删除
            if(key == 'delete'){
                this.deleteDepartment(data);
                return ;
            }else{
                 this.departmentModalIShow = true;
            }
            //编辑
            this.editData = {};
            this.editData.departmentParentId = data.departmentId;
            if(key == 'edit'){
                this.modalTitle = '编辑部门';
                this.editData = data;
                this.model= 'edit';
            }
            //详情
            if(key == 'detail'){
                this.modalTitle = '查看部门';
                this.editData = data;
                this.model= 'view';
            }
            //新增子部门
            if(key == 'new'){
                this.modalTitle = '新增子部门';
                this.editData.parentDepartment = data
                this.model= 'creat';
            }


        },
        //获取部门数据
        getDepartmentData () {
            axios.request({
                method: 'post',
                url: 'apis/department/queryTreeData'
            }).then((response) => {
                this.data = response.data.obj[0];
            }).catch((response) => {

            })
        },
        //删除部门
        deleteDepartment(data){
            if(data.children && data.children.length > 0){
                this.$Message.success("请先删除下级部门");
                return
            }
            axios.request({
                method: 'post',
                url: 'apis/department//delete/' + data.departmentId
            }).then((response) => {
                this.$emit('update-success')
                this.getDepartmentData();
                this.$Message.success(response.data.message)
            }).catch((response) => {

            })
        }
  },
  mounted () {
    this.getDepartmentData()
  }
}
</script>
