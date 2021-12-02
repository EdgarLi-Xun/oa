<style scoped>
    .expand-row{
        margin-bottom: 16px;
    }
</style>
<template>
    <div>
    <Table border :columns="projectuserColumns" :data="projectuserData" @on-selection-change="selectData">
        <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 5px" @click="openUserModal(index)">修改</Button>
            <Button type="error" size="small" @click="deleteuserData(index)">删除</Button>
        </template>
    </Table>
    <Modal :title="modaluserTiltle" :show.sync="projectModalIShow" @on-cancel="editData={}"  :width="900" ok-text="">
        <projectEdit :editData="editData"  @update-success="edituserComplete"  ></projectEdit>
    </Modal>
        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteuserShow" @on-cancel="cancelDelete"   @on-ok="comfirmDelete(index)" :width="400" >
            是否删除选择的数据？
        </Modal>
    </div>
</template>
<script>
    import axios from '@/libs/api.request'
    import projectEdit from './projectuser-edit'
    import Modal from '@/view/components/sega/modal'
    export default {
        props: {
            row: Object
        },
        components: {
            axios,
            Modal,
            projectEdit
        },
        data () {
            return {

                //员工信息列表
                projectuserColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },{
                    title: '用户名称',
                    key: 'userName'
                },{
                    title: '所属部门',
                    key: 'userDepartmentName'
                },{
                    title: '角色',
                    key: 'userRole'
                },{
                    title: '性别',
                    key: 'userGender'
                },{
                    title: '邮箱',
                    key: 'userEmail'
                },{
                    title: '地址',
                    key: 'userddress'
                },
                    {
                        title: '操作',
                        slot: 'action',
                        width: 150,
                        align: 'center'
                    }],
                projectuserData:[],
                //选中的行数据
                selectArray: [],
                projectModalIShow:false,
                deleteuserShow: false,

            }
        },
        methods: {

            //加载数据
            loadData(){
                var params =this._props.row;

                axios.request({
                    method: 'post',
                    url: 'apis/project/queryuserByprojectId',
                    data: this._props.row
                }).then((response) => {
                    this.projectuserData = response.data.obj;

                }).catch((response) => {

                })
            },
            openUserModal(index){
                    this.modaluserTiltle = '修改项目成员';
                    this.editData = this.projectuserData[index];
                    this.editData.pmProjectId=this.editData.projectId;
                    this.editData.pmUserId=this.editData.userId


                this.projectModalIShow = true;
            },
            //删除数据
            deleteuserData(index){

                this.deleteuserShow = true;
                this.editData =this.projectuserData[index]


            },
            cancelDelete(){
                this.deleteuserShow = false;
            },
            comfirmDelete(index){
                var arryIds = [];
                    arryIds.push(this.editData.pmId)


                axios.request({
                    method: 'post',
                    url: 'apis/projectMember/deleteByIds',
                    data: arryIds
                }).then((response) => {
                    this.$Message.success(response.data.message);
                    this.deleteuserShow = false;
                    this.loadData();
                }).catch((response) => {

                })
            },
            edituserComplete() {
                this.projectModalIShow = false;

            },

        },
        mounted () {
            this.loadData();
        }
    };

</script>
