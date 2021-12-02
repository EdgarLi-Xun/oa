<template>
    <i-form ref="projectInfo" :model="projectInfo" :rules="ruleValidate" :label-width="80">
        <Row>

            <i-col span="12">
                <Form-item label="项目名称" prop="projectName">
                    <i-input v-model="projectInfo.projectName" placeholder="请输入项目名称"></i-input>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="客户单位" prop="projectCustomer">
                    <i-input v-model="projectInfo.projectCustomer" placeholder="请输入客户单位"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="项目状态" prop="projectState">
                    <Select v-model="projectInfo.projectState">
                        <Option v-for="item in projectStateArray" :value="item.id"
                                :key="item.id">{{
                                item.text
                            }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
            <i-Col span="12">
                <FormItem label="立项日期" prop="approvalDate">
                    <DatePicker type="date" v-model="projectInfo.approvalDate"
                                :editable="false" :options="endDateApproval"
                                @on-change="approvalChange"
                                style="width:100%"></DatePicker>
                </FormItem>
            </i-Col>

        </Row>
        <Row>
            <i-Col span="12">
                <FormItem label="上线日期" prop="launchDate">
                    <DatePicker type="date" v-model="projectInfo.launchDate"
                                :editable="false" :options="startAndEndDateLaunch"
                                @on-change="LaunchChange"
                                style="width:100%"></DatePicker>
                </FormItem>
            </i-Col>
            <i-Col span="12">
                <FormItem label="验收日期" prop="acceptanceDate">
                    <DatePicker type="date" v-model="projectInfo.acceptanceDate"
                                :editable="false" :options="startDateAcceptance"
                                @on-change="acceptanceChange"
                                style="width:100%"></DatePicker>
                </FormItem>
            </i-Col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="项目经理" prop="projectManagerId">
                    <Select v-model="projectInfo.projectManagerId" @on-open-change="loadUserMethod" filterable>
                        <Option v-for="item in userList" :value="item.userId" :key="item.userId">{{
                                item.userName
                            }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="项目成员" prop="projectMembers">
                    <Select v-model="projectInfo.projectMembers" @on-open-change="loadUserMethod" multiple filterable>
                        <Option v-for="item in pmList" :value="item.userId" :key="item.userId">{{
                                item.userName
                            }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <Form-item label="备注" prop="projectRemark">
                <i-input type="textarea" v-model="projectInfo.projectRemark" placeholder="请输入备注"></i-input>
            </Form-item>
        </Row>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import TreeSelect from '_c/tree-select'
import {newTreeData} from '@/mock/data/tree-select'
import {getTreeSelectData} from '@/api/data'
import {debuglog} from 'util';

export default {
    name: 'userEdit',
    props: [
        'editData'
    ],
    components: {
        TreeSelect
    },
    data() {
        return {
            endDateApproval: {},
            startAndEndDateLaunch: {},
            startDateAcceptance: {},
            animal: '',
            treeSelected: [112, 113],
            treeData: [],
            userList: [],
            pmList: [],
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
            projectInfo: {
                projectId: '',
                projectName: '',
                approvalDate: '',
                launchDate: '',
                acceptanceDate: '',
                projectManagerId: '',
                prjectManagerName: '',
                projectCustomer: '',
                projectBudget: '',
                projectRemark: '',
                projectState: '',
                pmId: '',
                projectMembers: []
            },
            ruleValidate: {
                projectName: [
                    {required: true, message: '项目名称不能为空', trigger: 'blur'}
                ],
                projectManagerId: [
                    {required: true, message: '项目经理不能为空', type: 'number', trigger: 'change'},
                ],
                projectState: [
                    {required: true, message: '项目状态不能为空', type: 'number', trigger: 'change'}
                ],
                projectCustomer: [
                    {required: true, message: '客户单位不能为空', trigger: 'blur'}
                ]
            }
        }
    },
    watch: {
        // 监听数据
        editData(data) {
            this.projectInfo = data;
        }
    },
    // 方法
    methods: {
        approvalChange(e) {
            this.projectInfo.approvalDate = e;
            this.startAndEndDateLaunch = {
                disabledDate: date => {
                    let startTime = this.projectInfo.approvalDate ? new Date(this.projectInfo.approvalDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
                    return date && date.valueOf() < startTime;
                    let endTime = "";
                    if (this.projectInfo.acceptanceDate) {
                        endTime = this.projectInfo.acceptanceDate ? new Date(this.projectInfo.acceptanceDate).valueOf() : ''
                    }
                    //对区间进行校验
                    return !endTime ? date && date.valueOf() > endTime : !(date && date.valueOf() < endTime && date.valueOf() > startTime);
                }
            }
            if (!this.projectInfo.launchDate) {
                this.startDateAcceptance = {
                    disabledDate: date => {
                        let startTime = this.projectInfo.approvalDate ? new Date(this.projectInfo.approvalDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
                        return date && date.valueOf() < startTime;
                    }
                }
            }
        },
        LaunchChange(e) {
            this.projectInfo.launchDate = e;
            this.endDateApproval = {  //设置结束时间
                disabledDate: date => {
                    let endTime = this.projectInfo.launchDate ? new Date(this.projectInfo.launchDate).valueOf() : '';
                    return date && date.valueOf() > endTime;
                }
            }
            this.startDateAcceptance = { //设置开始时间
                disabledDate: date => {
                    let startTime = this.projectInfo.launchDate ? new Date(this.projectInfo.launchDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
                    return date && date.valueOf() < startTime;
                }
            }
        },
        acceptanceChange(e) {
            this.projectInfo.acceptanceDate = e;
            if (!this.projectInfo.launchDate) {
                this.endDateApproval = {
                    disabledDate: date => {
                        let endTime = this.projectInfo.acceptanceDate ? new Date(this.projectInfo.acceptanceDate).valueOf() : '';
                        return date && date.valueOf() > endTime;
                    }
                }
            }
            this.startAndEndDateLaunch = {
                disabledDate: date => {
                    let endTime = this.projectInfo.acceptanceDate ? new Date(this.projectInfo.acceptanceDate).valueOf() : '';
                    let startTime = "";
                    if (this.projectInfo.approvalDate) {
                        startTime = this.projectInfo.approvalDate ? new Date(this.projectInfo.approvalDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
                    }
                    return !startTime ? date && date.valueOf() > endTime : !(date && date.valueOf() < endTime && date.valueOf() > startTime);
                }
            }
        },
        // 确认按钮
        handleOk() {
            this.$refs['projectInfo'].validate((valid) => {
                if (valid) {
                    axios.request({
                        method: 'post',
                        url: 'apis/project/saveOrUpdate',
                        data: this.projectInfo
                    }).then((response) => {
                        this.$emit('update-success')
                        this.$refs['projectInfo'].resetFields()
                        this.$Message.success(response.data.message)
                    }).catch(() => {
                        this.$Message.error("failMessage")
                    })
                }
            })

        }, // 确认按钮
        handleCancel() {
            this.$refs['projectInfo'].resetFields()
        },
        loadUserMethod(expand) {
            if (expand) {
                axios.request({
                    url: 'apis/user/queryAll',
                    method: 'post',
                    data: {}
                }).then(res => {
                    this.pmList = res.data.obj;
                    this.userList = res.data.obj;
                    this.pList = res.data.obj;
                })
            }
        }
    },
    mounted() {
        this.loadUserMethod(true);
    }
}
</script>
