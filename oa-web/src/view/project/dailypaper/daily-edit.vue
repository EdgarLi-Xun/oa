<template>
    <div>
        <i-form ref="dailyInfo" :model="dailyInfo" :rules="ruleValidate" :label-width="80">
            <Row>
                <i-col span="6">
                    <Form-item label="发送人" prop="dayilUserName">
                        <i-input disabled="disabled" v-model="dailyInfo.dayilUserName" placeholder="请输入发送人"></i-input>
                    </Form-item>

                </i-col>

                <i-col span="6">
                    <FormItem label="日期" prop="dailyDate">
                        <DatePicker type="date" v-model="dailyInfo.dailyDate"
                                    :editable="false" @on-change="loadTaskTime"
                                    style="width:100%"></DatePicker>
                    </FormItem>
                </i-col>

                <i-col span="6">
                    <Form-item label="收件人" prop="dayilsjrNameArr">
                        <Select v-model="dailyInfo.dayilsjrNameArr" multiple filterable>
                            <Option v-for="item in sjrList" :value="isNullToBlankOrEmail(item)"
                                    :key="isNullToBlankOrEmail(item)">{{ isNullToBlankOrEmail(item) }}
                            </Option>
                        </Select>
                    </Form-item>

                </i-col>
                <i-col span="6">
                    <Form-item label="抄送人" prop="dayilcsrNameArr">
                        <Select v-model="dailyInfo.dayilcsrNameArr" multiple filterable>
                            <Option v-for="item in csrList" :value="isNullToBlankOrEmail(item)"
                                    :key="isNullToBlankOrEmail(item)">{{ isNullToBlankOrEmail(item) }}
                            </Option>
                        </Select>
                    </Form-item>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <Form-item label="日报主题" prop="dailyTheme">
                        <i-input v-model="dailyInfo.dailyTheme" placeholder="请输入日报主题"></i-input>
                    </Form-item>

                </i-col>
                <i-col span="12">
                    <Form-item label="上传附件" prop="dailyFile">
                        <Upload
                            multiple
                            :action="uploadAction"
                            :default-file-list="uploadList"
                            :on-success="handleSuccess"
                            ref="dailyFile"
                            :on-remove="removeFile"
                            :on-error="uploadError"
                            :before-upload="beforeAvatarUpload">
                            <Button icon="ios-cloud-upload-outline">上传附件</Button>
                        </Upload>
                    </Form-item>

                </i-col>

                <i-col span="6">
                    <FormItem label="定时时间">
                        <Time-picker type="datetime" v-model="dailyInfo.dailySendDateTm"></Time-picker>
                    </FormItem>
                </i-col>

                <i-col span="6">
                    <FormItem label="定时时间">
                        <i-input disabled="disabled" v-model="dailyInfo.sendTime" ></i-input>
                    </FormItem>
                </i-col>
            </Row>
            <Row>

                <!--                <i-col span="12">-->
                <!--                    <list>-->
                <!--                        <ListItem v-for="item in uploadList">-->
                <!--                            <ListItemMeta :title="item.dailyfileCreateDate" :description=" item.dailyfileName"/>-->
                <!--                            <template slot="action">-->
                <!--                                <li>-->
                <!--                                    <a :href="item.download">下载</a>-->
                <!--                                </li>-->
                <!--                                <li>-->
                <!--                                    <a href="">删除</a>-->
                <!--                                </li>-->
                <!--                            </template>-->
                <!--                        </ListItem>-->
                <!--                    </list>-->
                <!--                </i-col>-->
            </Row>
        </i-form>

        <div>
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px" @click="addData">新增风险</i-button>
            </div>
            <Table border :columns="dailyHazardColumns" :data="dailyHazard" height="130"></Table>
        </div>
        <div style="margin-top: 20px;">
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px" @click="addDetailData">新增今日工作内容</i-button>
            </div>
            <Table border :columns="dailyDetailColumns" :data="dailyDetail" height="130"></Table>
        </div>

        <div style="margin-top: 20px;">
            <div style="margin-bottom: 10px;">
                <i-button type="primary" style="margin-left: 2px" @click="addPlanData">新增明日计划</i-button>
            </div>
            <Table border :columns="dailyPlanColumns" :data="dailyPlan" height="130"></Table>
        </div>

    </div>


</template>
<script>
import axios from '@/libs/api.request'
import TreeSelect from '_c/tree-select'
import {newTreeData} from '@/mock/data/tree-select'
import {getTreeSelectData} from '@/api/data'
import {mapState, mapGetters, mapMutations, mapActions} from 'vuex'
import {debuglog} from 'util';
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import config from '@/config'
import Modal from '@/view/components/sega/modal'

export default {
    name: 'dailyEdit',
    props: [
        'editDailData'
    ],
    components: {
        TreeSelect,
        axios,
        Modal
    },
    data() {
        return {
            uploadAction: "/apis/daily/daily/importdaily",
            editDailyHazard: -1,
            editdailyPlan: -1,
            editDailata: -1,
            animal: '',
            userList: [],
            sjrList: [],
            csrList: [],
            projectList: [],
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
                dailySendDate: '',
                dayilUserId: '',
                dayilUserName: '',
                dailyTheme: '',
                dailyFile: '',
                dailyState: '',
                dayilsjrNameArr: [],
                dayilcsrNameArr: [],
                flag: '',
                dailySendDateTm: '',
                dailyDate: ''
            },

            ruleValidate: {
                dayilUserName: [
                    {required: true, message: '发送人不能为空', trigger: 'blur'}
                ],
                dailyDate: [
                    {required: true, message: '日期不能为空', type: 'date', trigger: 'change'}
                ],
                dailyTheme: [
                    {required: true, message: '主题不能为空', trigger: 'blur'}
                ],
                /*dayilsjrName: [
                    { required: true, message: '收件人不能为空', type: 'string', trigger: 'change'}
                ]*/

            },
            //风险内容列表
            dailyHazardColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '序号',
                key: 'xh',
                width: 70,
                minWidth: 70,
                render: (h, params) => {
                    return h("span", params.index + 1);
                },
            }, {
                title: '问题描述',
                key: 'dhProblem',
                render: (h, params) => {
                    if (this.editDailyHazard == params.index) {

                        return h('textArea', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
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
                },
                input: 'textarea',
                editable: true

            }, {
                title: '应对方案',
                key: 'dhPlan',
                render: (h, params) => {
                    if (this.editDailyHazard == params.index) {

                        return h('textArea', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
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
                },
                input: 'textarea',
                editable: true
            },
                {
                    title: '操作',
                    key: 'action',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        if (this.editDailyHazard == params.index) {
                                            this.handleHazardSave(params.row)
                                        } else {
                                            this.handleHazard(params.row, params.index)
                                        }
                                    }
                                }
                            }, this.editDailyHazard == params.index ? '保存' : '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        this.removeHazard(params.index)
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                },
            ],
            //工作进展列表
            dailyDetailColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '序号',
                key: 'dexh',
                width: 70,
                minWidth: 70,
                render: (h, params) => {
                    return h("span", params.index + 1);
                },
            }, {
                title: '项目名称',
                key: 'ddProjectName',
                width: 180,
                render: (h, params) => {
                    if (this.editDailata == params.index) {

                        return h('Select', {
                            props: {
                                transfer: true,
                                filterable: true,
                                value: params.row.ddProjectName, // 获取选择的下拉框的值
                                size: 'small'
                            },
                            on: {
                                'on-change': e => {
                                    params.row.ddProjectName = e // 改变下拉框赋值
                                }
                            }
                        }, this.projectList.map((item) => {
                            return h('Option', {
                                props: {
                                    value: item.projectName,
                                    label: item.projectName
                                }
                            });
                        }));
                    } else {
                        return h('div', params.row.ddProjectName);
                    }


                },
                editable: true

            }, {
                title: '工作内容',
                key: 'ddContent',
                render: (h, params) => {
                    if (this.editDailata == params.index) {

                        return h('textArea', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
                            domProps: {
                                value: params.row.ddContent
                            },
                            on: {
                                input: (event) => {
                                    params.row.ddContent = event.target.value
                                }
                            }
                        });
                    } else {

                        return h('div', params.row.ddContent);
                    }
                },
                input: 'textarea',
                editable: true
            }, {
                title: '进度(%)',
                key: 'ddSchedule',
                width: 90,
                minWidth: 90,
                render: (h, params) => {
                    if (this.editDailata == params.index) {

                        return h('input', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
                            domProps: {
                                value: params.row.ddSchedule
                            },
                            on: {
                                input: (event) => {
                                    const data = event.target.value
                                    var reg = new RegExp(/^(100|[1-9]?\d(\.\d\d?\d?)?)$|0$/);
                                    if (reg.test(data)) {
                                        params.row.ddSchedule = event.target.value
                                    } else {
                                        event.target.value = ""
                                        this.$Message.error("请填写数据格式正确数据")
                                    }
                                }
                            }
                        });
                    } else {
                        return h('div', params.row.ddSchedule);
                    }
                },
                input: 'textarea',
                editable: true
            }, {
                title: '耗时',
                key: 'ddTime',
                width: 90,
                minWidth: 90,
                render: (h, params) => {
                    if (this.editDailata == params.index) {
                        return h('input', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
                            domProps: {
                                value: params.row.ddTime
                            },
                            on: {
                                input: (event) => {
                                    const data = event.target.value
                                    if (data && data > 0) {
                                        params.row.ddTime = event.target.value
                                    } else {
                                        event.target.value = ""
                                        this.$Message.error("请填写数据格式正确数据")
                                    }
                                }
                            }
                        });
                    } else {

                        return h('div', params.row.ddTime);
                    }
                },
                input: 'textarea',
                editable: true
            }, {
                title: '备注',
                key: 'ddRemarks',
                render: (h, params) => {
                    if (this.editDailata == params.index) {

                        return h('textArea', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
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
                },
                input: 'textarea',
                editable: true
            },
                {
                    title: '操作',
                    key: 'action',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        if (this.editDailata == params.index) {
                                            this.handleDEtailSave(params.row)
                                        } else {
                                            this.handleDEtailEdit(params.row, params.index)
                                        }
                                    }
                                }
                            }, this.editDailata == params.index ? '保存' : '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        this.remove(params.index)
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                },

            ],
            dailyPlanColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '序号',
                key: 'planxh',
                width: 70,
                minWidth: 70,
                render: (h, params) => {
                    return h("span", params.index + 1);
                },
            }, {
                title: '项目名称',
                key: 'dpProjectName',
                width: 180,
                render: (h, params) => {

                    if (this.editdailyPlan == params.index) {

                        return h('Select', {
                            props: {
                                transfer: true,
                                filterable: true,
                                value: params.row.dpProjectName, // 获取选择的下拉框的值
                                size: 'small'
                            },
                            on: {
                                'on-change': e => {
                                    params.row.dpProjectName = e // 改变下拉框赋值
                                }
                            }
                        }, this.projectList.map((item) => {
                            return h('Option', {
                                props: {
                                    value: item.projectName,
                                    label: item.projectName
                                }
                            });
                        }));
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
                    if (this.editdailyPlan == params.index) {

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
                    if (this.editdailyPlan === params.index) {
                        return h('datePicker', {
                            props: {
                                placement: 'top',
                                transfer: true,
                                value: params.row.dpPlanTime
                            },
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
                            on: {
                                input: function (event) {
                                    if (event) {
                                        params.row.dpPlanTime = event.toLocaleDateString().replace(/\//g, '-')
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
            }, {
                title: '备注',
                key: 'dpPlanRemarks',
                render: (h, params) => {
                    if (this.editdailyPlan == params.index) {

                        return h('textArea', {
                            style: {
                                width: '90%',
                                padding: '4px 2px',
                                borderRadius: '4px',
                                border: '1px solid #e9eaec',
                            },
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
                },
                input: 'textarea',
                editable: true
            },
                {
                    title: '操作',
                    key: 'action',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        if (this.editdailyPlan == params.index) {
                                            this.handlePlanSave(params.row, params.index)
                                        } else {
                                            this.handlePlanEdit(params.row, params.index)
                                        }
                                    }
                                }
                            }, this.editdailyPlan == params.index ? '保存' : '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                on: {
                                    click: () => {
                                        this.removePlan(params.index)
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                },

            ],
            //用户数据
            dailyHazard: [],
            dailyDetail: [],
            dailyPlan: []
        }
    },
    watch: {
        // 监听数据
        editDailData(data) {
            this.uploadList = [];
            if (data.dailyId) {
                axios.request({
                    method: 'get',
                    url: 'apis/daily/daily/get/' + data.dailyId
                }).then((res) => {
                    this.dailyInfo = res.data.obj;
                    this.loadDetail(this.dailyInfo);
                    if (res.data.obj.dayilsjrName) {
                        this.dailyInfo.dayilsjrNameArr = res.data.obj.dayilsjrName.split(',');
                    }
                    if (res.data.obj.dayilcsrName) {
                        this.dailyInfo.dayilcsrNameArr = res.data.obj.dayilcsrName.split(',');

                    }
                    axios.request({
                        method: 'post',
                        url: 'apis/daily/daily/querydailyFileBydailyid',
                        data: data.dailyId
                    }).then((response) => {
                        const datas = response.data.obj
                        for (var i = 0; i < datas.length; i++) {
                            datas[i].download = this.BASEURL + '/apis/daily/daily/downLoadFile/' + datas[i].dailyfileId
                        }
                        for (let i = datas.length - 1; i >= 0; i--) {
                            this.uploadList.push({
                                'id': datas[i].dailyfileId,
                                'name': datas[i].dailyfileName,
                                'url': this.BASEURL + '/apis/daily/daily/downLoadFile/' + datas[i].dailyfileId
                            })
                        }
                    }).catch((response) => {

                    })
                }).catch((response) => {

                });
            } else {
                this.dailyInfo = data;
            }
        }
    },
    computed: {
        ...mapState({
            userInfo: (state) => state.user
        })
    },
    // 方法
    methods: {
        removeFile(e, a) {
            if (this.dailyInfo.dailyFile) {
                var ids = this.dailyInfo.dailyFile.split(',');
                for (var i = 0; i < this.uploadList.length; i++) {
                    if (e.uid == this.uploadList[i].uid) {
                        this.uploadList.splice(i, 1);
                        for (var j = 0; j < ids.length; j++) {
                            if (ids[j] == this.uploadList[i].id) {
                                ids.splice(j, 1);
                            }
                        }
                    }
                }
                this.dailyInfo.dailyFile = ids.toString();
            }
        },
        beforeAvatarUpload(file) {
            let size = file.size;
            if (size / (1024 * 1024) > 10) {
                this.$Message.error("文件上传不能超过10MB");
                return false;
            }
        },
        handleSuccess(res, file, fileList) {
            if (res.success == false) {
                this.$Message.error(res.message);
                var index = fileList.indexOf(file);
                if (index > -1) {
                    fileList.splice(index, 1);
                }
                return;
            }
            var ids = [];
            if (this.dailyInfo.dailyFile) {
                ids = this.dailyInfo.dailyFile.split(',');
            }
            for (var i = 0; i < fileList.length; i++) {
                if (fileList[i].response) {
                    ids.push(fileList[i].response.obj.dailyfileId);
                }
            }
            this.dailyInfo.dailyFile = ids.toString()
            //清除已上传的文件
            // this.$refs.clearFiles.clearFiles();

            axios.request({
                method: 'post',
                url: 'apis/humanEngineering/attendanceRecord/importExcel',
                data: ids
            }).then((response) => {
                this.$Message.success(response.data.message);
            }).catch((response) => {

            })
        },
        uploadError(res, file, fileList) {
            this.$Message.error("文件上传失败");
        },

        // 确认按钮
        save(send) {
            send = send ? send : false;

            var flag = true
            var _this = this;

            //当日工作内容和明日计划不允许为空
            if (this.dailyDetail == undefined || this.dailyDetail.length <= 0) {
                this.$Message.error("今日工作内容不能为空！")
                return;
            }

            if (this.dailyPlan == undefined || this.dailyPlan.length <= 0) {
                this.$Message.error("明日计划不能为空！")
                return;
            }

            if (this.dailyInfo.dayilsjrNameArr) {
                this.dailyInfo.dayilsjrName = this.dailyInfo.dayilsjrNameArr.toString();
            }
            if (this.dailyInfo.dayilcsrNameArr) {
                this.dailyInfo.dayilcsrName = this.dailyInfo.dayilcsrNameArr.toString();
            }
            var params = {
                daily: this.dailyInfo,
                dailyHazard: this.dailyHazard,
                dailyPlan: this.dailyPlan,
                dailyDetail: this.dailyDetail,
                sende: send

            };
            this.$refs['dailyInfo'].validate((valid) => {
                if (valid) {
                    axios.request({
                        method: 'post',
                        async: false,
                        url: 'apis/daily/daily/saveOrUpdateDaily',
                        data: params
                    }).then((response) => {
                        const data = response.data
                        if (data.success) {
                            this.$emit('update-success')
                            this.$Message.success(data.message)
                        } else {
                            this.$Message.error(data.message)
                        }
                        //清除已上传的文件
                        this.$refs['dailyInfo'].resetFields();
                        this.$refs['dailyFile'].clearFiles();
                        this.dailyHazard = [];
                        this.dailyDetail = [];
                        this.dailyPlan = [];
                    }).catch((response) => {
                    })
                }
            })


        },
        handleCancel() {
            this.$refs['dailyInfo'].resetFields()
            this.dailyHazard = [];
            this.dailyDetail = [];
            this.dailyPlan = [];
            //清除已上传的文件
            this.$refs['dailyFile'].clearFiles();
        },
        //如果为null则取空字符串
        isNullToBlankOrEmail(data) {
            return data == null ? "" : data.email
        }, //编辑列表
        handleEdit(row) {
            this.$set(row, '$isEdit', true)
        },
        handleDEtailEdit(row, index) {
            this.editDailata = index;
        },
        handleHazard(row, index) {
            this.editDailyHazard = index;
        },
        handlePlanEdit(row, index) {
            this.editdailyPlan = index;
        },
        //删除一列
        remove(index) {
            this.dailyDetail.splice(index, 1);
            this.dailyDetail.remove(index);
        },
        removeHazard(index) {
            this.dailyHazard.splice(index, 1);
            this.dailyHazard.remove(index);
        },
        removePlan(index) {
            this.dailyPlan.splice(index, 1);
            this.dailyPlan.remove(index);
        },
        //保存编辑内容
        handleDEtailSave(row) {
            if (!row.ddProjectName) {
                this.$Message.info("项目名称描述为空")
            } else if (!row.ddContent) {
                this.$Message.info("工作内容描述为空")
            } else if (!row.ddSchedule) {
                this.$Message.info("进度描述为空")
            } else {
                this.editDailata = -1;

            }
            this.dailyDetail[row._index] = row;
            this.$set(row, '$isEdit', false)
            this.editDailData.flag = false;
        },

        handleHazardSave(row) {
            if (!row.dhProblem) {
                this.$Message.info("问题描述为空")
            } else if (!row.dhPlan) {
                this.$Message.info("应对方案描述为空")
            } else {
                this.editDailyHazard = -1;
            }
            this.dailyHazard[row._index] = row;


        },
        handlePlanSave(row) {
            if (!row.dpProjectName) {
                this.$Message.info("项目名称描述为空")
            } else if (!row.dpPlanContent) {
                this.$Message.info("明日计划描述为空")
            } else if (!row.dpPlanTime) {
                this.$Message.info("预计完成日期描述为空");
            } else {
                this.editdailyPlan = -1;
            }
            this.dailyPlan[row._index] = row;
            this.$set(row, '$isEdit', false)
            this.editDailData.flag = false;

        },
        //查找用户信息
        selectsjrMethod(expand) {
            if (expand) {
                axios.request({
                    url: 'apis/daily/dailySender/queryjsrEmail',
                    method: 'post',
                    data: this.userInfo.setUserInfo.userName
                }).then(res => {
                    this.sjrList = res.data.obj;
                })
            }
        },
        selectcsrMethod(expand) {
            if (expand) {
                axios.request({
                    url: 'apis/daily/dailySender/querycsrEmail',
                    method: 'post',
                    data: this.userInfo.setUserInfo.userName
                }).then(res => {
                    this.csrList = res.data.obj;
                })
            }
        },

        loadData(item, callback) {
            // this.dailyInfo.dayilUserName = this.userInfo.setUserInfo.userEmail;
            // this.dailyInfo.dayilUserId=this.userInfo.setUserInfo.userId;
        },

        //加载数据
        loadprojectData() {
            axios.request({
                method: 'post',
                url: 'apis/projectMember/queryprojectList',
                data: this.userInfo.setUserInfo.userName
            }).then((response) => {
                this.editDailData.flag = true;
                this.dailyDetail = response.data.obj;
            }).catch((response) => {

            })
        },

        //加载数据
        loadprojectplanData() {
            axios.request({
                method: 'post',
                url: 'apis/projectMember/queryprojectplanList',
                data: this.userInfo.setUserInfo.userName
            }).then((response) => {
                this.editDailData.flag = true;
                this.dailyPlan = response.data.obj;
            }).catch((response) => {

            })
        },
        //新增风险
        addData() {
            var obj = {
                dhProblem: '',
                dhPlan: '',
                xh: ''
            }
            this.dailyHazard.push(obj);
            this.editDailyHazard = this.dailyHazard.length - 1;
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
            this.editDailata = this.dailyDetail.length - 1;
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
            this.editdailyPlan = this.dailyPlan.length - 1;
        },
        loadTaskTime(value, type) {
            if (!value) {
                return;
            }
            this.dailyInfo.dailyTheme = " 日报" + " " + value
            axios.request({
                method: 'post',
                url: 'apis/taskTime/queryAll',
                data: {ttDate: value}
            }).then((response) => {
                if (response.data.obj) {
                    this.dailyDetail = [];
                    response.data.obj.forEach(tt => {
                        var obj = {
                            ddProjectName: tt.ttProjectName,
                            ddTime: tt.ttSpend,
                            ddProjectId: tt.ttProjectId,
                            ddContent: tt.ttTypeDetail,
                            ddSchedule: tt.ttRate,
                            ddRemarks: ''
                        }
                        this.dailyDetail.push(obj);
                    })
                }

            }).catch((response) => {

            })
        }, loadSjrAndCsr() {
            axios.request({
                method: 'post',
                url: 'apis/daily/dailySender/loadJsrAndCsrEmail',
                data: {}
            }).then((response) => {
                if (response.data.obj) {
                    this.dailyInfo.dayilsjrNameArr = response.data.obj.dayilsjrNameArr;
                    this.dailyInfo.dayilcsrNameArr = response.data.obj.dayilcsrNameArr;
                }

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
        }
    },
    // 初始化方法
    mounted() {
        const baseUrl = process.env.NODE_ENV === 'development' ? config.baseUrl.dev : config.baseUrl.pro;
        this.uploadAction = baseUrl + this.uploadAction;

        this.selectsjrMethod(true);
        this.selectcsrMethod(true);
        this.loadProjectMethod(true);

        this.loadSjrAndCsr();

    }
}
</script>
