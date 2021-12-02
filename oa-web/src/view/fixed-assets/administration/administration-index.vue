<template>
<Tabs type="card">
  <TabPane label="行政管理">
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90">
                <Row>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="资产名称" prop="faName">
                            <i-input v-model="condition.projectName"></i-input>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem prop="faState" label="资产状态">
                            <Select v-model="condition.faState" >
                                <Option v-for="item in administrationStateArray" :value="item.id"
                                        :key="item.id">{{ item.text
                                    }}
                                </Option>
                            </Select>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <div>
            <i-button type="primary" style="margin-left: 2px" @click="openModal('add')">新增</i-button>
            <i-button type="primary" style="margin-left: 10px" @click="openModal('edit')">修改</i-button>
            <!-- <i-button type="primary" style="margin-left: 10px" @click="adduser">新增项目成员</i-button> -->
            <i-button type="primary" style="margin-left: 10px" @click="deleteData">删除</i-button>
        </div>
        <Table border :columns="projectColumns" :data="projectData" @on-selection-change="selectData"></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total"  :current="page.current" @on-change="pageChange" @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>

        <!--新增或編輯彈窗-->
        <Modal :title="modalTitle" v-model="modal6" :show.sync="userModalIShow" @on-cancel="editData={}"  :width="900" :loading="loading" ok-text="">
            <administrationEdit :editData="editData"  @update-success="editComplete"  ></administrationEdit>
        </Modal>

        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete"   @on-ok="comfirmDelete" :width="400" >
            是否删除选择的数据？
        </Modal>
        <Modal :title="deleteTitle" :show.sync="addUserShow" @on-cancel="canceladdUser"   @on-ok="openUserModal('adduser')" :width="400" >
            该项目是否需要添加成员？
        </Modal>
        <!-- <Modal :title="modaluserTiltle" :show.sync="projectModalIShow" @on-cancel="editData={}"  :width="900" ok-text="">
            <projectEdit :editData="editData"  @update-success="edituserComplete"  ></projectEdit>
        </Modal> -->
    </div>
  </TabPane>
  <TabPane label="同名统计">
    <Table border :columns="statisticsColumns" :data="statisticsData"></Table>
    <div style="margin: 10px;overflow: hidden">
        <div style="float:right;">
            <Page :total="TJpage.total" :current="TJpage.current" @on-change="pageTJChange" @on-page-size-change="pageTJSizeChange" :page-size="TJpage.pageSize" show-elevator show-sizer></Page>
        </div>
    </div>
  </TabPane>
</Tabs>


</template>
<script>
    import axios from '@/libs/api.request'
    import { setToken, getToken } from '@/libs/util'
    import { loadavg } from 'os';
    import { debuglog } from 'util';
    import Modal from '@/view/components/sega/modal'
    import administrationEdit from './administration-edit'
    export default {
        name: 'administrationIndex',
        components: {
            axios,
            Modal,
            administrationEdit
        },
        //数据区域
        data () {
            return {
                //删除弹窗是否显示
                deleteShow: false,
                addUserShow:false,
                //删除标识
                deleteTitle: '提示',
                // 查询条件
                condition: {
                    administrationName: '',
                    administrationPersonName: ''
                },
                administrationStateArray:[{//资产状态
                    id:0,
                    text:'在用'
                },{
                    id:1,
                    text:'丢失'
                },{
                    id:2,
                    text:'停用'
                },{
                    id:3,
                    text:'维修'
                },{
                    id:4,
                    text:'外借'
                }],
                //员工信息列表
                projectColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                }
                // , {
                //     type: 'expand',
                //     width: 50,
                //     render: (h, params) => {
                //         return h(expandRow, {
                //             props: {
                //                 row: params.row
                //             }
                //         })
                //     }
                // }
                ,{
                    title: '资产名称',
                    key: 'faName'
                },{
                    title: '责任人',
                    key: 'faResponsibleName'
                },{
                    title: '资产编号',
                    key: 'faCode',
                },{
                    title: '创建时间',
                    key: 'faCreateDate'
                },{
                    title: '价格',
                    key: 'faPrice'
                },{
                    title: '资产状态',
                    key: 'faState',
                    render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: this.administrationStateArray[params.row.faState].text
                            }
                        })
                    }
                },{
                    title: '备注',
                    key: 'faRemark'
                }],
                //统计信息列表
                statisticsColumns:[{
                    type:'selection',
                    width: 60,
                    align: 'center'
                },{
                    title: '资产名称',
                    key: 'faName'
                },{
                    title: '总价格',
                    key: 'price'
                },{
                    title: '个数',
                    key: 'num'
                }],
                //用户数据
                projectData: [],
                //统计数据
                statisticsData: [],
                //分页信息
                page:	{
                    total: 100,
                    current: 1,
                    pageSize: 10,
                },
                //统计分页信息
                TJpage:	{
                    total: 100,
                    current: 1,
                    pageSize: 10,
                },
                //编辑弹窗是否显示
                userModalIShow: false,
                projectModalIShow:false,
                //弹窗标题
                modalTitle: '新增项目',
                modaluserTiltle:'新增项目成员',
                //编辑的数据
                editData: {},
                //选中的行数据
                selectArray: [],
            }
        },
        //方法区域
        methods: {
            //打开界面
            openModal(type){
                if(type == 'add'){
                    this.modalTitle = '新增资产';
                    this.editData = {};
                }
                if(type == 'edit'){
                    this.modalTitle = '编辑资产';
                    if(this.selectArray.length != 0){
                        this.editData = this.selectArray[0];
                    }
                }
                this.userModalIShow = true;
            },
            openUserModal(type){
                if(type == 'adduser'){
                    this.modaluserTiltle = '新增项目成员';
                    if(this.selectArray.length != 0){
                        this.editData = this.selectArray[0];
                        this.editData.pmProjectId=this.editData.projectId;
                    }
                    this.addUserShow=false;
                }
                this.projectModalIShow = true;
            },
            //取消删除
            cancelDelete(){
                this.deleteShow = false;
            },
            canceladdUser(){
                this.addUserShow = false;
            },
            //确认删除
            comfirmDelete(){
                var arryIds = [];
                for(var i = 0 ; i <  this.selectArray.length ; i++){
                    arryIds.push(this.selectArray[i].faId)
                }
                axios.request({
                    method: 'post',
                    url: 'apis/fixedAssets/deleteByIds',
                    data:arryIds
                }).then((response) => {
                    this.$Message.success(response.data.message);
                this.deleteShow = false;
                this.query()
            }).catch((response) => {

                })
            },
            //删除数据
            deleteData(){
                if(this.selectArray.length == 0){
                    this.$Message.error("请选择需要删除的数据");
                    return ;
                }
                this.deleteShow = true;

            },

            //编辑完成
            editComplete() {
                this.query();

                this.userModalIShow = false;
            },
            edituserComplete() {
                this.query();
                this.projectModalIShow = false;
            },
            //选择数据变化触发事件
            selectData(selectArray){
                this.selectArray = selectArray;
                if(selectArray.length == 0){
                    this.editData = selectArray[0];
                }else{
                    this.editData = {}
                }
            },
            //分页改变
            pageChange (pageNum) {
                this.page.current = pageNum;
                this.loadData();
            },
            //每页数据改变
            pageSizeChange (pageSize) {
                this.page.current = 1;
                this.page.pageSize = pageSize;
                this.loadData();
            },
            //统计分页改变
            pageTJChange (pageNum) {
                this.TJpage.current = pageNum;
                this.loadTJData();
            },
            //统计每页数据改变
            pageTJSizeChange (pageSize) {
                this.TJpage.current = 1;
                this.TJpage.pageSize = pageSize;
                this.loadTJData();
            },
            //查询数据
            query (){
                this.page.current = 1;
                this.loadData();
            },
            //重置查询
            handleReset(){
                this.condition = {}
                this.loadData();
            },
            //加载数据
            loadData(){
                var params = this.condition;
                params['pageNum'] = this.page.current;
                params['pageSize'] = this.page.pageSize;
                axios.request({
                    method: 'post',
                    url: 'apis/fixedAssets/queryByPage',
                    data: this.condition
                }).then((response) => {
                    this.projectData = response.data.obj.list;
                    this.page.current = response.data.obj.pageNum;
                    this.page.pageSize = response.data.obj.pageSize;
                    this.page.total = response.data.obj.total;
            }).catch((response) => {

                });
            },
            loadTJData(){
                var page = {
                    pageNum:this.TJpage.current,
                    pageSize:this.TJpage.pageSize
                }
                axios.request({
                    method:'post',
                    url:'apis/fixedAssets/queryStatistics',
                    data:page
                }).then((response) => {
                    this.statisticsData = response.data.obj.list;
                     this.TJpage.current = response.data.obj.pageNum;
                    this.TJpage.pageSize = response.data.obj.pageSize;
                    this.TJpage.total = response.data.obj.total;
                }).catch((response)=>{

                });
            }
        },
        //初始化事件
        mounted () {
            this.loadData();
            this.loadTJData();
            getToken();
        }
    }
</script>
