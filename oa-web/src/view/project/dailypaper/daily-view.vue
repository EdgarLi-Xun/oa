<template>
    <div>
        <i-form ref="dailyInfo" :model="dailyInfo" :rules="ruleValidate" :label-width="80">
            <Row>
                <i-col span="12">
                    <Form-item label="发送人" prop="dayilUserName">
                        <i-input disabled="disabled" v-model="dailyInfo.dayilUserName" placeholder="请输入发送人"></i-input>
                    </Form-item>

                </i-col>
                <i-col span="12">
                    <Form-item label="收件人" prop="drUserid">
                        <i-input disabled="disabled" v-model="dailyInfo.dayilsjrName" placeholder="请输入收件人"></i-input>
                    </Form-item>

                </i-col>
            </Row>
            <Row>
                <i-col span="24">
                    <Form-item label="日报主题" prop="dailyTheme">
                        <i-input disabled="disabled" v-model="dailyInfo.dailyTheme" placeholder="请输入日报主题"></i-input>
                    </Form-item>

                </i-col>
            </Row>
            <Row>
                <i-col span="24">
                    <Form-item label="上传附件" prop="dailyFile">
                        <list>
                            <ListItem v-for="item in uploadList">
                                <ListItemMeta :title="item.dailyfileCreateDate" :description=" item.dailyfileName"/>
                                <template slot="action">
                                    <li>
                                        <a :href="item.download">下载</a>
                                    </li>
                                </template>
                            </ListItem>
                        </list>
                    </Form-item>


                </i-col>

            </Row>

        </i-form>

        <div>
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px">风险问题</i-button>
            </div>
            <Table border :columns="dailyHazardColumns" :data="dailyHazard"></Table>
        </div>
        <div style="margin-top: 20px;">
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px">工作进展</i-button>
            </div>
            <Table border :columns="dailyDetailColumns" :data="dailyDetail"></Table>
        </div>

        <div style="margin-top: 20px;">
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px">明日计划</i-button>
            </div>
            <Table border :columns="dailyPlanColumns" :data="dailyPlan"></Table>
        </div>
    </div>

</template>
<script>
    import axios from '@/libs/api.request'
    import TreeSelect from '_c/tree-select'
    import {mapState, mapGetters, mapMutations, mapActions} from 'vuex'
    import {debuglog} from 'util';
    import config from '@/config'
    export default {
        name: 'dailyView',
        props: [
            'editData'
        ],
        components: {
            TreeSelect
        },
        data() {
            return {
                BASEURL: '',
                animal: '',
                treeSelected: [112, 113],
                userList: [],
                modalTitle: '风险问题',
                projectStateArray: [{
                    id: 0,
                    text: '未启动'
                }, {
                    id: 1,
                    text: '进行中'
                }, {
                    id: 2,
                    text: '已结束'
                }],
                uploadList: [],
                dailyInfo: {
                    dailyId: '',
                    drUserid: '',
                    dailySendDate: '',
                    dayilUserId: '',
                    dayilUserName: '',
                    dailyTheme: '',
                    dailyFile: '',
                    dailyState: '',
                },
                ruleValidate: {
                    pmId: [
                        {required: true, message: '项目名称不能为空', trigger: 'blur'}
                    ],
                    pmUserId: [
                        {required: true, message: '用户不能为空', type: 'number', trigger: 'change'},
                    ],

                },
                //风险内容列表
                dailyHazardColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                }, {
                    title: '序号',
                    width: 70,
                    key: 'xh',
                    render: (h, params) => {
                        return h("span", params.index + 1);
                    },
                }, {
                    title: '问题描述',
                    key: 'dhProblem',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.dhProblem
                                },
                                on: {
                                    input: function (event) {
                                        params.row.dhProblem = event.target.value
                                    }
                                }
                            });
                        } else {

                        return h('div', params.row.dhProblem);
                    }
                }

                }, {
                    title: '应对方案',
                    key: 'dhPlan',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.dhPlan
                                },
                                on: {
                                    input: function (event) {
                                        params.row.dhPlan = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.dhPlan);
                        }
                    }
                }
                ],
                //工作进展列表
                dailyDetailColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                }, {
                    title: '序号',
                    width: 70,
                    key: 'dexh',
                    render: (h, params) => {
                        return h("span", params.index + 1);
                    },
                }, {
                    title: '项目名称',
                    key: 'ddProjectName',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.ddProjectName
                                },
                                on: {
                                    input: function (event) {
                                        params.row.ddProjectName = event.target.value
                                    }
                                }
                            });
                        } else {

                        return h('div', params.row.ddProjectName);
                    }
                }

                }, {
                    title: '工作内容',
                    key: 'ddContent',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.ddContent
                                },
                                on: {
                                    input: function (event) {
                                        params.row.ddContent = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.ddContent);
                        }
                    }
                }, {
                    title: '进度(%)',
                    width: 120,
                    key: 'ddSchedule',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.ddSchedule
                                },
                                on: {
                                    input: function (event) {
                                        params.row.ddSchedule = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.ddSchedule);
                        }
                    }
                }, {
                    title: '耗时',
                    width: 100,
                    key: 'ddTime',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.ddTime
                                },
                                on: {
                                    input: function (event) {
                                        params.row.ddTime = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.ddTime);
                        }
                    }
                }, {
                    title: '备注',
                    key: 'ddRemarks',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.ddRemarks
                                },
                                on: {
                                    input: function (event) {
                                        params.row.ddRemarks = event.target.value
                                    }
                                }
                            });
                        } else {

                        return h('div', params.row.ddRemarks);
                    }
                }
            }

                ],
                dailyPlanColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                }, {
                    title: '序号',
                    key: 'planxh',
                    width: 70,
                    render: (h, params) => {
                        return h("span", params.index + 1);
                    },
                }, {
                    title: '项目名称',
                    key: 'dpProjectName',
                    width: 180,
                    render: (h, params) => {
                        if (this.editData.flag) {
                            this.$set(params.row, '$isEdit', true)
                        }
                        if (params.row.$isEdit) {

                            return h('input', {
                                style: {
                                    width: '90%',
                                    padding: '4px 2px',
                                    borderRadius: '4px',
                                    border: '1px solid #e9eaec',
                                },
                                domProps: {
                                    value: params.row.dpProjectName
                                },
                                on: {
                                    input: function (event) {
                                        params.row.dpProjectName = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.dpProjectName);
                        }
                    },
                    input: 'textarea',
                    editable: true

                }, {
                    title: '明日计划',
                    key: 'dpPlanContent',
                    render: (h, params) => {
                        if (this.editData.flag) {
                            this.$set(params.row, '$isEdit', true)
                        }
                        if (params.row.$isEdit) {

                            return h('textArea', {
                                style: {
                                    width: '90%',
                                    padding: '4px 2px',
                                    borderRadius: '4px',
                                    border: '1px solid #e9eaec',
                                },
                                domProps: {
                                    value: params.row.dpPlanContent
                                },
                                on: {
                                    input: function (event) {
                                        params.row.dpPlanContent = event.target.value
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.dpPlanContent);
                        }
                    },
                    input: 'textarea',
                    editable: true
                }, {
                    title: '预计完成日期',
                    key: 'dpPlanTime',
                    width: 200,
                    render: (h, params) => {
                        if (this.editData.flag) {
                            this.$set(params.row, '$isEdit', true)
                        }
                        if (params.row.$isEdit) {
                            return h('datePicker', {
                                props: {
                                    placement: 'top',
                                    transfer: true,
                                },
                                style: {
                                    width: '90%',
                                    padding: '4px 2px',
                                    borderRadius: '4px',
                                    border: '1px solid #e9eaec',
                                },
                                domProps: {
                                    value: params.row.dpPlanTime
                                },
                                on: {
                                    input: function (event) {
                                        if(event){
                                            params.row.dpPlanTime =  event.toLocaleDateString().replace(/\//g, '-')
                                        }
                                    }
                                }
                            });
                        } else {

                            return h('div', params.row.dpPlanTime);
                        }
                    },
                    input: 'date',
                    editable: true
                },  {
                    title: '备注',
                    key: 'dpPlanRemarks',
                    render: (h, params) => {

                    if (params.row.$isEdit) {

                        return h('input', {

                                domProps: {
                                    value: params.row.dpPlanRemarks
                                },
                                on: {
                                    input: function (event) {
                                        params.row.dpPlanRemarks = event.target.value
                                    }
                                }
                            });
                        } else {

                        return h('div', params.row.dpPlanRemarks);
                    }
                }
            }

                ],
                //用户数据
                dailyHazard: [],
                dailyDetail: [],
                dailyPlan: []
            }
        },
        watch: {
            // 监听数据
            editData(data) {
                this.dailyInfo = data;
                this.loadDetail(data);
            }
        },
        computed: {
            ...mapState({
                userInfo: (state) => state.user
            })
        },
        // 方法
        methods: {
            // 确认按钮
            handleOk() {
                this.$emit('update-success')
            },
            //查找用户信息
            selectuserMethod() {
                axios.request({
                    url: 'apis/user/queryAll',
                    method: 'post',
                    data: {}
                }).then(res => {
                    this.userList = res.data.obj;
                })
            },
            loadData(item, callback) {
                this.dailyHazard.dhDailyId = this.editData.dailyId;
            },
            //新增风险
            addData() {
                var obj = {
                    dhProblem: '',
                    dhPlan: '',
                    xh: ''
                }
                this.dailyHazard.push(obj);
            },
            //新增工作进度
            addDetailData() {
                var obj = {
                    ddProjectName: '',
                    ddTime: '',
                    ddProjectId: '',
                    ddContent: '',
                    ddSchedule: '',
                    ddRemarks: ''

                }
                this.dailyDetail.push(obj);
            },
            //新增明日计划
            addPlanData() {
                var obj = {
                    dpProjectId: '',
                    dpDailyId: '',
                    dpPlanContent: '',
                    dpPlanTime: '',
                    dpPlanRemarks: '',

                }
                this.dailyPlan.push(obj);
            },
            exportExcel(url) {
                /* if(this.data.length == 0){
                     this.$Message.info('当前列表暂无数据！')
                     return;
                 }
                 let url1 = this.GLOBAL.BASE_URL + url,
                     sessionId = Cookies.get('status'),
                     form = $('<form></form>').attr('action',url1).attr('method','post');
                 form.append($('<input></input>').attr('type','hidden').attr('name','Authorization').attr('value',sessionId));
                 form.appendTo('body').submit().remove();*/
            },
            loadDetail(data) {
                axios.request({
                    method: 'post',
                    url: 'apis/daily/dailyHazard/querydailyhazardBydailyid',
                    data: data.dailyId
                }).then((response) => {
                    this.dailyHazard = response.data.obj;

            }).catch((response) => {

            });
            axios.request({
                method: 'post',
                url: 'apis/daily/dailyDetail/querydailydetailBydailyid',
                data: data.dailyId
            }).then((response) => {
                this.dailyDetail = response.data.obj;

            }).catch((response) => {

            });
            axios.request({
                method: 'post',
                url: 'apis/daily/dailyPlan/querydailyplanBydailyid',
                data: data.dailyId
            }).then((response) => {
                this.dailyPlan = response.data.obj;

            }).catch((response) => {

                })
                axios.request({
                    method: 'post',
                    url: 'apis/daily/daily/querydailyFileBydailyid',
                    data: data.dailyId
                }).then((response) => {
                   const datas = response.data.obj
                    for(var i=0;i<datas.length;i++){
                        datas[i].download = this.BASEURL +'/apis/daily/daily/downLoadFile/' + datas[i].dailyfileId
                    }
                    this.uploadList = datas
                }).catch((response) => {

                })
            }
        },
        // 初始化方法
        mounted() {
            this.loadData();
            this.selectuserMethod()
            const baseUrl = process.env.NODE_ENV === 'development' ? config.baseUrl.dev : config.baseUrl.pro
            this.BASEURL = baseUrl ;
        }
    }
</script>
