<template>

    <div>

        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90">
                <Row>
                    <!--                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">-->
                    <!--                        <Form-item label="统计维度" clearable>-->
                    <!--                            <Select v-model="model1" @on-change="tjwd(model1)">-->
                    <!--                                <Option :value="0">全部</Option>-->
                    <!--                                <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>-->
                    <!--                            </Select>-->
                    <!--                        </Form-item>-->
                    <!--                    </i-col>-->
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="项目名称" prop="ttProjectId">
                            <Select v-model="condition.projectId" @on-open-change="loadProjectMethod" clearable
                                    filterable>
                                <Option v-for="item in projectList" :value="item.projectId" :key="item.projectId">
                                    {{ item.projectName }}
                                </Option>
                            </Select>
                        </Form-item>
                    </i-col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="员工姓名" prop="ttProjectId">
                            <Select v-model="condition.userId" @on-open-change="loadProjectMethod" clearable filterable>
                                <Option v-for="item in userList" :value="item.userId" :key="item.userId">
                                    {{ item.userName }}
                                </Option>
                            </Select>
                        </Form-item>
                    </i-col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="6" class="col-max-width">
                        <FormItem label="日期" prop="date">
                            <Row>
                                <i-Col span="11">
                                    <FormItem prop="startDate">
                                        <DatePicker type="date" v-model="condition.startDate"
                                                    :editable="false" :options="startTimeOptions"
                                                    @on-change="startTimeChange"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                                <i-Col span="2" style="text-align: center">-</i-Col>
                                <i-Col span="11">
                                    <FormItem prop="dateEnd">
                                        <DatePicker type="date" v-model="condition.endDate"
                                                    :editable="false" :options="endTimeOptions"
                                                    @on-change="endTimeChange" @on-clear="endTimeClear"
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
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>

            <div style="margin-bottom: 20px">
            </div>

        </div>
        <Table border :columns="taskTimeColumns" :data="taskTimeData"></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total" :current="page.current" @on-change="pageChange"
                      @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>
    </div>
</template>
<script>
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import {debuglog} from 'util';
import Modal from '@/view/components/sega/modal'
import taskTimeEdit from '../tasktime/tasktime-edit'
import taskTimeView from '../tasktime/tasktime-view'
import taskTimeSubordinates from '../tasktime/tasktime-Subordinates'

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
            cityList: [
                {
                    value: '1',
                    label: '项目'
                },
                {
                    value: '2',
                    label: '员工'
                }
            ],
            model1: ''
            ,
            startTimeOptions: {},
            endTimeOptions: {},
            //删除弹窗是否显示
            deleteShow: false,
            projectList: [],
            // 查询条件
            condition: {
                projectId: '',
                userId: '',
                projectName: ''
            },
            //项目信息列表
            taskTimeColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            },
                {
                    title: '项目名称',
                    key: 'projectName'
                }
                , {
                    title: '管理工时',
                    key: 'demandTime'
                }, {
                    title: '开发工时',
                    key: 'develTime'
                }, {
                    title: '项目交付工时',
                    key: 'testTime'
                }
            ],
            //用户数据
            taskTimeData: [],
            userList: [],
            //分页信息
            page: {
                total: 100,
                current: 1,
                pageSize: 10,
            }
        }
    },
    methods: {
        tjwd(str) {
            console.log(str)
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
                url: 'apis/taskTime/getTjTask',
                data: this.condition
            }).then((response) => {
                this.taskTimeData = response.data.obj.list;
                for (var i = 0; i < this.taskTimeData.length; i++) {
                    var data = this.taskTimeData[i];
                    if (!data.otherTime) {
                        this.taskTimeData[i].otherTime = '-';
                    }
                    if (!data.demandTime) {
                        this.taskTimeData[i].demandTime = '-';
                    }
                    if (!data.develTime) {
                        this.taskTimeData[i].develTime = '-';
                    }
                    if (!data.testTime) {
                        this.taskTimeData[i].testTime = '-';
                    }
                    if (!data.implementTime) {
                        this.taskTimeData[i].implementTime = '-';
                    }
                }
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
                }),
                    axios.request({
                        method: 'post',
                        url: 'apis/user/queryAllUser',
                    }).then((response) => {
                        this.userList = response.data;
                    })
            }
        }, startTimeChange: function (e) { //设置开始时间
            this.condition.startDate = e;
            this.endTimeOptions = {
                disabledDate: date => {
                    let startTime = this.condition.startDate ? new Date(this.condition.startDate).valueOf() : '';
                    return date && (date.valueOf() < startTime);
                }
            }
        },
        endTimeChange: function (e) { //设置结束时间
            this.condition.endDate = e;
            let endTime = this.condition.endDate ? new Date(this.condition.endDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
            this.startTimeOptions = {
                disabledDate(date) {
                    return date && date.valueOf() > endTime;
                }
            }
        },
        endTimeClear: function (e) {
            this.startTimeOptions = {
                disabledDate(date) {
                    let endTime = this.condition.endDate ? new Date(this.condition.endDate).valueOf() - 1000 * 24 * 60 * 60 * 1000 : '';
                    return date && date.valueOf() > endTime;
                }
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
