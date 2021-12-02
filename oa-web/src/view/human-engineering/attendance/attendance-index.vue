<template>
    <div>
        <div>
            <Form ref="attendance" :model="attendance" :label-width="90">
                <Row>
                  <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="姓名" prop="name">
                            <i-input v-model="attendance.name"></i-input>
                        </FormItem>
                  </i-col>
                  <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="日期" prop="date">
                            <Row>
                                <i-Col span="11">
                                    <FormItem prop="dateStart">
                                        <DatePicker type="date" v-model="attendance.dateStart"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                                <i-Col span="2" style="text-align: center">-</i-Col>
                                <i-Col span="11">
                                    <FormItem prop="dateEnd">
                                        <DatePicker type="date" v-model="attendance.dateEnd"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                            </Row>
                        </FormItem>
                  </i-col>
                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('attendance')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <div>
            <Row >
                <i-Col span="1" >
                    <i-button type="primary" style="margin-left: 2px" @click="openModal('add')">新增</i-button>
                </i-Col>
                <i-Col span="1" >
                    <i-button type="primary" style="margin-left: 10px" @click="openModal('edit')">修改</i-button>
                </i-Col>
                <i-Col span="1" style="margin-left: 7px">
                    <i-button type="primary" style="margin-left: 10px" @click="deleteData">删除</i-button>
                </i-Col>
                <i-Col span="2" style="margin-left: 16px">
                    <Upload
                        multiple
                        :headers="headers"
                        :action="uploadAction"
                        :on-success="handleSuccess"
                        ref="clearFiles"
                        >
                        <Button icon="ios-cloud-upload-outline">上传文件</Button>
                    </Upload>
                </i-Col>
            </Row>
            <!-- <i-button type="primary" style="margin-left: 2px" @click="openModal('add')">新增</i-button>
            <i-button type="primary" style="margin-left: 10px" @click="openModal('edit')">修改</i-button> -->
            <!-- <i-button type="primary" style="margin-left: 10px" @click="adduser">新增项目成员</i-button> -->
            <!-- <i-button type="primary" style="margin-left: 10px" @click="deleteData">删除</i-button>
            <span style="margin-left: 10px">
                <Upload action="//jsonplaceholder.typicode.com/posts/">
                    <Button icon="ios-cloud-upload-outline">Upload files</Button>
                </Upload>
            </span> -->
        </div>
        <Table border :columns="projectColumns" :data="projectData" @on-selection-change="selectData"></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total"  :current="page.current" @on-change="pageChange" @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>

        <!-- 新增或編輯彈窗 -->
        <!-- <Modal :title="modalTitle" :show.sync="userModalIShow" @on-cancel="editData={}"  :width="900" :loading="loading" ok-text="">
            <administrationEdit :editData="editData"  @update-success="editComplete"  ></administrationEdit>
        </Modal> -->

        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete"   @on-ok="comfirmDelete" :width="400" >
            是否删除选择的数据？
        </Modal>
        <!-- <Modal :title="deleteTitle" :show.sync="addUserShow" @on-cancel="canceladdUser"   @on-ok="openUserModal('adduser')" :width="400" >
            该项目是否需要添加成员？
        </Modal> -->
    </div>
</template>
<script>
    import axios from '@/libs/api.request'
    import { setToken, getToken } from '@/libs/util'
    import { loadavg } from 'os';
    import { debuglog } from 'util';
    import Modal from '@/view/components/sega/modal'
    import config from '@/config'
export default {
    name: 'attendanceIndex',
    components: {
            axios,
            Modal
        },
        //数据区域
    data() {
        return {
            //文件上传的请求头
            headers: {

            },
            uploadAction : "/apis/daily/file/import",
            //删除弹窗是否显示
            deleteShow: false,
            addUserShow:false,
            //删除标识
            deleteTitle: '提示',
            // 查询条件
            attendance: {
                name: '',
                date: ''
            },
            //上班时间是否异常
            SymbolArray:[{
                id:0,
                text:'无异常'
            },{
                id:1,
                text:'异常'
            }],
            //加班状态
            OverstateArray:[{
                id:0,
                text:'工作日加班'
            },{
                id:1,
                text:'周末半天班'
            },{
                id:2,
                text:'周末全天班'
            },{
                id:3,
                text:'未加班'
            }],
            LieupayState:[{
                id:0,
                text:'无调休无工资'
            },{
                id:1,
                text:'调休日'
            },{
                id:2,
                text:'工资日'
            }],
            WeekArray:[{
                id:0,
                text:'星期六'
            },{
                id:1,
                text:'星期日'
            },{
                id:2,
                text:'星期一'
            },{
                id:3,
                text:'星期二'
            },{
                id:4,
                text:'星期三'
            },{
                id:5,
                text:'星期四'
            },{
                id:6,
                text:'星期五'
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
                title: '日期',
                key: 'arDate'
            },{
                title: '姓名',
                key: 'arUsername'
            },{
                title: '上班时间',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: params.row.arBasicdata.substring(0,5)
                            }
                        })
                    }
            },{
                title: '下班时间',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: params.row.arBasicdata.substring(params.row.arBasicdata.length-5,params.row.arBasicdata.length)
                            }
                        })
                    }
            },{
                title: '上班时长',
                key: 'arTime',
            },{
                title: '星期',
                key: 'arWeek',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: this.WeekArray[params.row.arWeek].text
                            }
                        })
                    }
            },{
                title: '上班时间是否异常',
                key: 'arSymbol',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: this.SymbolArray[params.row.arSymbol].text
                            }
                        })
                    }
            },{
                title: '异常状态',
                key: 'arDebuff'
            },{
                title: '加班状态',
                key: 'arOverstate',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: this.OverstateArray[params.row.arOverstate].text
                            }
                        })
                    }
            },{
                title: '工资调休日',
                key: 'arLieupayState',
                render: (h, params) => {
                        return h('span',{
                            domProps: {
                                innerHTML: this.LieupayState[params.row.arLieupayState].text
                            }
                        })
                    }
            },{
                title: '获得工资调休日',
                key: 'arGetLieupay'
            },{
                title: '金额',
                key: 'arMoney'
            },{
                title: '原始数据',
                key: 'arBasicdata'
            }
            // ,{
            //     title: '审核人',
            //     key: 'arCheckerName'
            // }
            ,{
                title: '备注',
                key: 'arRemark'
            }],
            //数据
            projectData: [],
            //分页信息
            page:{
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
                this.modalTitle = '新增考勤';
                this.editData = {};
            }
            if(type == 'edit'){
                this.modalTitle = '编辑考勤';
                if(this.selectArray.length != 0){
                    this.editData = this.selectArray[0];
                }
            }
            this.userModalIShow = true;
        },
        // openUserModal(type){
        //     if(type == 'adduser'){
        //         this.modaluserTiltle = '新增项目成员';
        //         if(this.selectArray.length != 0){
        //             this.editData = this.selectArray[0];
        //             this.editData.pmProjectId=this.editData.projectId;
        //         }
        //         this.addUserShow=false;
        //     }
        //     this.projectModalIShow = true;
        // },
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
                url: 'apis/humanEngineering/attendanceRecord/deleteByIds',
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
        //文件上传成功事件
        handleSuccess (res, file, fileList) {
            var ids = [];
            for(var i = 0 ; i < fileList.length ; i++){
                if(fileList[i].response){
                      ids.push(fileList[i].response.obj.fileId);
                }else{
                    return ;
                }
            }
            //清除已上传的文件
            this.$refs.clearFiles.clearFiles();

            axios.request({
                method: 'post',
                url: 'apis/humanEngineering/attendanceRecord/importExcel',
                data:ids
            }).then((response) => {
                this.$Message.success(response.data.message);
            }).catch((response) => {

            })
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
        //查询数据
        query (){
            this.page.current = 1;
            this.loadData();
        },
        //重置查询
        handleReset(){
            this.attendance = {}
            this.loadData();
        },
        //加载数据
        loadData(){
            var params = this.attendance;
            params['pageNum'] = this.page.current;
            params['pageSize'] = this.page.pageSize;
            axios.request({
                method: 'post',
                url: 'apis/humanEngineering/attendanceRecord/queryByPage',
                data: this.attendance
            }).then((response) => {
                this.projectData = response.data.obj.list;
                this.page.current = response.data.obj.pageNum;
                this.page.pageSize = response.data.obj.pageSize;
                this.page.total = response.data.obj.total;
        }).catch((response) => {

            });
        }
    },
    //初始化事件
    mounted () {
        const baseUrl = process.env.NODE_ENV === 'development' ? config.baseUrl.dev : config.baseUrl.pro
        this.loadData();
        this.uploadAction = baseUrl + this.uploadAction;
        this.headers = {
            'Authorization': getToken()
        }
        console.log(this.headers);
    }
}
</script>
