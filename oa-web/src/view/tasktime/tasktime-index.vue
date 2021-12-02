<template>
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90">
                <Row>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="项目名称" prop="ttProjectId">
                            <Select v-model="condition.ttProjectId" @on-open-change="loadProjectMethod" clearable filterable>
                                <Option v-for="item in projectList" :value="item.projectId" :key="item.projectId">{{ item.projectName }}
                                </Option>
                            </Select>
                        </Form-item>
                    </i-col>
                    <i-Col :xs="24" :sm="6" :md="6" :lg="7">
                        <FormItem label="日期" prop="occurDate">
                            <Row>
                                <i-Col span="11">
                                    <FormItem prop="ttDateStart">
                                        <DatePicker type="date" v-model="condition.ttDateStart"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                                <i-Col span="2" style="text-align: center">-</i-Col>
                                <i-Col span="11">
                                    <FormItem prop="ttDateEnd">
                                        <DatePicker type="date" v-model="condition.ttDateEnd"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                            </Row>
                        </FormItem>
                    </i-Col>
                     <i-Col :xs="24" :sm="6" :md="6" :lg="5">
                         <Form-item label="任务类型" prop="ttTaskType">
                             <Select v-model="condition.ttTaskType"  clearable>
                                 <Option v-for="item in typeList" :value="item.value" :key="item.value">
                                     {{ item.label }}
                                 </Option>
                             </Select>
                         </Form-item>
                    </i-Col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="填报人" prop="ttProjectId">
                            <Select v-model="condition.ttUserId" @on-open-change="loadUserMethod" clearable filterable>
                                <Option v-for="item in userList" :value="item.userId" :key="item.userId">{{ item.userName }}
                                </Option>
                            </Select>
                        </Form-item>
                    </i-col>

                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <div style="margin-bottom: 10px">
            <i-button type="info" style="margin-left: 2px" @click="openXj()">查看项目工时</i-button>
            <i-button type="primary" style="margin-left: 10px" @click="openModal('add')">新增</i-button>
            <i-button type="primary" style="margin-left: 10px" @click="openModal('edit')">修改</i-button>
            <i-button type="error" style="margin-left: 10px" @click="deleteData">删除</i-button>
        </div>
        <Table border :columns="taskTimeColumns" :data="taskTimeData" @on-selection-change="selectData"></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total" :current="page.current" @on-change="pageChange"
                      @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>


        <!--新增或编辑弹窗-->
        <Modal :title="modalTitle" :show.sync="userModalIShow" :width="900" ok-text="">
            <taskTimeEdit :editData="editData"  @update-success="editComplete"/>
        </Modal>
        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete" @on-ok="comfirmDelete"
               :width="400">
            是否删除选择的数据？
        </Modal>
        <Modal :title="taskViewTitle" :show.sync="taskModalIShow"  :width="900" ok-text="">
            <taskTimeView :taskViewData="taskViewData" @update-success="saveComplete" />
        </Modal>
        <Modal :title="subordinateTitle"  :show.sync="subordinateShow"  :width="1300" ok-text="">
            <taskTimeSubordinates></taskTimeSubordinates>
        </Modal>
    </div>
</template>

<script>
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import {debuglog} from 'util';
import Modal from '@/view/components/sega/modal'
import taskTimeEdit from './tasktime-edit'
import taskTimeView from './tasktime-view'
import taskTimeSubordinates from './tasktime-Subordinates'

export default {
    name: 'userIndex',
    components: {
        axios,
        Modal,
        taskTimeEdit,
        taskTimeView,
        taskTimeSubordinates
    },
    data() {
        return {
            //删除弹窗是否显示
            deleteShow: false,
            projectList: [],
            userList: [],
            // 查询条件
            condition: {
                ttCreateBy: '',
                ttId: '',
                ttProjectId: '',
                ttDateStart: '',
                ttDateEnd: '',
                ttTaskType: '',
                ttUserName:''
            },
            typeList: [
                {
                    value: '1',
                    label: '需求'
                },
                {
                    value: '2',
                    label: '开发'
                },
                {
                    value: '3',
                    label: '测试'
                },
                {
                    value: '4',
                    label: '实施'
                },
                {
                    value: '5',
                    label: '其他'
                }
            ],
            //项目信息列表
            taskTimeColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            },
            {
                title: '日期',
                width: 120,
                key: 'ttDate',
            },{
                title: '任务类型',
                width: 100,
                key: 'ttTaskType',
                render:(h,params)=>{
                    let ttTaskType = params.row.ttTaskType
                    if(ttTaskType=='1'){
                        ttTaskType = '需求'
                    }else if(ttTaskType=='2'){
                        ttTaskType = '开发'
                    }else if(ttTaskType =='3'){
                        ttTaskType = '测试'
                    }else if(ttTaskType =='4'){
                        ttTaskType = '实施'
                    }else if (ttTaskType=='5'){
                        ttTaskType = '其他'
                    }
                    return h('span',ttTaskType)
                }
            }
            , {
                title: '项目名称',
                key: 'ttProjectName'
            }, {
                title: '工时',
                width: 70,
                key: 'ttSpend'
            }, {
                title: '工作内容',
                key: 'ttTypeDetail'
            }, {
                title: '进度(%)',
                width: 100,
                key: 'ttRate'
            }, {
                title: '填报人',
                width: 120,
                key: 'ttUserName'
            },
            {
                title: '审核状态',
                width: 120,
                key: 'ttState',
                render:(h,params)=>{
                    let ttState = params.row.ttState
                    if(ttState=='1'){
                        ttState = '待审核'
                    }else if(ttState=='2'){
                        ttState = '已通过'
                    }else if(ttState =='3'){
                        ttState = '已驳回'
                    }
                    return h('span',ttState)
                }
            },
            {
                title: '操作',
                key: 'action',
                width: 150,
                render: (h, params) => {
                    return h('div', [
                        h('Button', {
                            props: {
                                type: 'primary',
                                size: 'small'
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                    this.openView(params.row)
                                }
                            }
                        }, '查看')
                    ]);
                }
            },
            // {
            //     title: '计划工时',
            //     width: 120,
            //     key: 'ttPlanSpend'
            // },
            // {
            //     title: '备注',
            //     width: 120,
            //     key: 'ttRemarks'
            // }
            ],
            //用户数据
            taskTimeData: [],
            //分页信息
            page: {
                total: 100,
                current: 1,
                pageSize: 10,
            },
            modalTitle: '新增工时信息',
            deleteTitle: '提示',
            editData: {},
            //选中的行数据
            selectArray: [],
            userModalIShow: false,
            taskModalIShow: false,
            taskViewData:{},
            taskViewTitle: '查看工时信息',

            subordinateTitle:'下属工时',
            subordinateShow: false,

        }
    },
    methods: {
        edit(data){
            console.log(data)
        },
        openView(row){
            this.query()
            this.taskViewData = row;
            this.taskModalIShow = true;
        },
        close(str) {
            if (str=="taskTimeSub"){
                this.subordinateShow = false;
            }else if (str=="taskTimeView"){
                this.query()
                this.taskModalIShow = false;
            }
        },
        //打开下级工时页面
        openXj(){
            this.subordinateShow = true;
        },

        //打开界面
        openModal(type) {
            if (type == 'add') {
                this.modalTitle = '新增工时信息';
                this.editData = {ttDate: new Date()};
            }
            if (type == 'edit') {
                this.modalTitle = '编辑工时信息';
                if (this.selectArray.length > 1) {
                    this.$Message.info("请选择一条数据 ");
                    return;
                } else if (this.selectArray.length === 1) {
                    this.editData = this.selectArray[0];
                } else {
                    this.$Message.info("请选择需要编辑的数据");
                    return;
                }
            }
            this.userModalIShow = true;
        },
        //删除数据
        deleteData() {
            if (this.selectArray.length == 0) {
                this.$Message.error("请选择需要删除的数据");
                return;
            }
            this.deleteShow = true;
        },
        //取消删除
        cancelDelete() {
            this.deleteShow = false;
        },
        //确认删除
        comfirmDelete() {
            var arryIds = [];
            for (var i = 0; i < this.selectArray.length; i++) {
                arryIds.push(this.selectArray[i].ttId)
            }
            axios.request({
                method: 'post',
                url: 'apis/taskTime/deleteByIds',
                data: arryIds
            }).then((response) => {
                this.$Message.success(response.data.message);
                this.deleteShow = false;
                this.query()
            }).catch((response) => {
            })
        },
        //编辑完成
        editComplete() {
            this.query();
            this.userModalIShow = false;
        },
        saveComplete() {
            this.query();
            this.taskModalIShow = false;
        },
        //选择数据变化触发事件
        selectData(selectArray) {
            this.selectArray = selectArray;
            if (selectArray.length == 0) {
                this.editData = selectArray[0];
            } else {
                this.editData = {}
            }
        },


        //分页改变
        pageChange(pageNum) {
            this.page.current = pageNum;
            this.loadData();
        },
        //每页数据改变
        pageSizeChange(pageSize) {
            this.page.current = 1;
            this.page.pageSize = pageSize;
            this.loadData();
        },
        //查询数据
        query() {
            this.page.current = 1;
            this.loadData();
        },
        //重置查询
        handleReset() {
            this.condition = {}
            this.loadData();
        },
        //加载数据
        loadData() {
            var params = this.condition;
            params['pageNum'] = this.page.current;
            params['pageSize'] = this.page.pageSize;
            axios.request({
                method: 'post',
                url: 'apis/taskTime/queryByPage',
                data: this.condition
            }).then((response) => {
                this.taskTimeData = response.data.obj.list;
                this.page.current = response.data.obj.pageNum;
                this.page.pageSize = response.data.obj.pageSize;
                this.page.total = response.data.obj.total;
            }).catch((response) => {
            })
    },
        loadProjectMethod(expand) {
            if (expand) {
                axios.request({
                    url: 'apis/project/queryAll',
                    method: 'post',
                    data: {}
                }).then(res => {
                    this.projectList = res.data.obj;
                })
            }
        },

        loadUserMethod(expand) {
            if (expand) {
                axios.request({
                    url: 'apis/project/queryUserAll',
                    method: 'post',
                    data: {}
                }).then(res => {
                    this.userList = res.data.obj;
                })
            }
        }
    },
    //初始化事件
    mounted() {
        this.loadProjectMethod(true);
        this.loadData();
        getToken();
    }
}
</script>




