<template>
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90">
                <Row>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="日期" prop="date">
                            <Row>
                                <i-Col span="11">
                                    <FormItem prop="startTime">
                                        <DatePicker type="date" v-model="start_time"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                                <i-Col span="2" style="text-align: center">-</i-Col>
                                <i-Col span="11">
                                    <FormItem prop="endTime">
                                        <DatePicker type="date" v-model="end_time"
                                                    :editable="false"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                            </Row>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem prop="spName" label="流程名称">
                            <Select v-model="condition.spName" >
                                <Option v-for="item in spNameArray" :value="item.text"
                                        :key="item.text">{{ item.text
                                    }}
                                </Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem prop="applyName" label="申请人">
                            <i-input v-model="condition.applyName" placeholder="请输入..." style="width: 300px"></i-input>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center; margin-bottom: 10px">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <Table border :columns="projectColumns" :data="spData"></Table>
    </div>
</template>
<script>
    import axios from '@/libs/api.request'
    import { setToken, getToken } from '@/libs/util'
    import { getFormDate } from '@/libs/tools.js'
    import Modal from '@/view/components/sega/modal';
    import expandRow from './wechat-approvalInfo';
    export default {
        name: 'wechat-index',
        components: {
            axios,
            Modal
        },
        //数据区域
        data () {
            return {
                //删除标识
                deleteTitle: '提示',
                // 查询条件
                condition: {
                    startTime: '',
                    endTime: '',
                    spName: '',
                    applyName: ''
                },
                start_time: '',
                end_time: '',
                spNameArray: [{
                    text: '释加请假流程'
                },{
                    text: '释加出差申请'
                },{
                    text: '释加出差加班加班申请'
                },{
                    text: '释加采购'
                }],
                applyNameArray: [],
                //审批流程信息列表
                projectColumns: [{
                    title: '序号',
                    key: 'dexh',
                    width: 80,
                    render: (h, params) => {
                        return h("span", params.index+ 1);
                    },
                },{
                    type: 'expand',
                    width: 50,
                    render: (h, params) => {
                    return h(expandRow, {
                        props: {
                            row: params.row
                        }
                    })
                }
            },{
                title: '流程名称',
                key: 'spname'
            }, {
                title: '申请时间',
                key: 'apply_time',
                render: function(h, params) {
                   return h('div',getFormDate(params.row.apply_time,'y-m-d h:i:s'));
            }
            }, {
                title: '部门名称',
                key: 'apply_org'
            }, {
                title: '申请人',
                key: 'apply_name'
            }, {
                title: '审批人',
                key: 'approval_name',
                render: (h,params) => {
                    let spr = "";
                    params.row.approval_name.forEach(item => {
                        spr += item + ',';
                    })
                    return h('div',spr.substring(0,spr.length - 1));
                }
            }, {
                title: '通知人',
                key: 'notify_name',
                render: (h,params) => {
                    let spr = "";
                    params.row.notify_name.forEach(item => {
                        spr += item + ',';
                    })
                    return h('div',spr.substring(0,spr.length - 1));
                }
            }, {
                title: '审批状态',
                key: 'sp_status',
                render: (h, params) => {
                    let status;
                    switch (params.row.sp_status) {
                        case 1:
                            status = "审批中"
                            break;
                        case 2:
                            status = "已通过"
                            break;
                        case 3:
                            status = "已驳回"
                            break;
                        case 4:
                            status = "已取消"
                            break;
                        default:
                            status = "";
                    }
                    return h('div',status);
                }
            }],
            //用户数据
            spData: [],
            detailData : [],
        }
        },
        //方法区域
        methods: {
            //查询数据
            query (){
                this.loadData();
            },
            //重置查询
            handleReset(){
                this.condition = {}
                var d = new Date();
                this.end_time = new Date(d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate());
                d.setMonth(d.getMonth() - 1);
                this.start_time = new Date(d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate());
                this.loadData();
            },
            //加载数据
            loadData(){
                let startTime = this.start_time;
                let endTime = this.end_time;
                if (typeof startTime != 'undefined' && startTime != '') {
                    startTime = startTime.getTime()/1000;
                    this.condition.startTime = startTime + '';
                } else {
                    this.condition.startTime = '';
                }
                if (typeof endTime != 'undefined' && endTime != '') {
                    endTime = endTime.getTime()/1000;
                    this.condition.endTime = endTime + '';
                } else {
                    this.condition.endTime = '';
                }
                axios.request({
                    method: 'post',
                    url: 'apis/wechat/queryWechatData',
                    data: this.condition
                }).then((response) => {
                    var spdata = response.data.obj;
                    this.spData = spdata;
            }).catch((response) => {

                })
            }
        },
        //初始化事件
        mounted () {
            var d = new Date();
            this.end_time = new Date(d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate());
            d.setMonth(d.getMonth() - 1);
            this.start_time = new Date(d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate());
            this.loadData();
            getToken();
        }
    }
</script>
