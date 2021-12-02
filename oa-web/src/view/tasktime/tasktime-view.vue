<template>
    <i-form ref="taskTimeInfo" :model="taskTimeInfo" :label-width="80">
        <Row>
            <i-Col span="12">
                <FormItem label="任务类型" prop="ttTaskType">
                    <Select disabled="disabled" v-model="taskTimeInfo.ttTaskType" clearable style="width:200px">
                        <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select>
                </FormItem>
            </i-Col>
            <i-col span="12">
                <Form-item label="计划工时" prop="ttPlanSpend">
                    <i-input disabled="disabled" v-model="taskTimeInfo.ttPlanSpend" type="number"
                             placeholder="请输入计划工时"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-Col span="12">
                <FormItem label="日期" prop="ttDate">
                    <DatePicker disabled="disabled" type="date" v-model="taskTimeInfo.ttDate"
                                :editable="false"
                                style="width:100%"></DatePicker>
                </FormItem>
            </i-Col>
            <i-col span="12">
                <Form-item label="项目名称" prop="ttProjectId">
                    <Select disabled="disabled" v-model="taskTimeInfo.ttProjectId" @on-open-change="loadProjectMethod"
                            filterable>
                        <Option v-for="item in projectList" :value="item.projectId" :key="item.projectId"
                                @click.native="selectProjectMethod(item)">{{ item.projectName }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="工时" prop="ttSpend">
                    <i-input disabled="disabled" v-model="taskTimeInfo.ttSpend" type="number"
                             placeholder="请输入工时"></i-input>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="进度(%)" prop="ttRate">
                    <i-input disabled="disabled" v-model="taskTimeInfo.ttRate" type="number"
                             placeholder="请输入工作进度"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="24">
                <Form-item label="工作内容" prop="ttTypeDetail">
                    <i-input disabled="disabled" type="textarea" v-model="taskTimeInfo.ttTypeDetail"
                             placeholder="请输入工作内容"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="24">
                <Form-item label="备注" prop="ttRemarks">
                    <i-input disabled="disabled" type="textarea" v-model="taskTimeInfo.ttRemarks"
                             placeholder="备注内容"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row v-if="taskTimeInfo.audit">
            <Collapse simple>
                <Panel name="1">
                    审 核
                    <p slot="content">
                        <RadioGroup v-model="taskTimeInfo.ttState" style="margin-bottom: 10px">
                            <Radio label="2" ttState>通过</Radio>
                            <Radio label="3" ttState>驳回</Radio>
                        </RadioGroup>
                    </p>
                    <p slot="content">
                        <Input name="ttReview" v-model="taskTimeInfo.ttReview" type="textarea" placeholder="备注"/>
                    </p>
                </Panel>
            </Collapse>

        </Row>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import TreeSelect from '_c/tree-select'
import {newTreeData} from '@/mock/data/tree-select'
import {getTreeSelectData} from '@/api/data'
import {debuglog} from 'util';
import {getToken} from "@/libs/util";

export default {
    name: 'taskTimeView',
    props: [
        'taskViewData'
    ],
    components: {
        TreeSelect
    },
    data() {
        const validateRate = (rule, value, callback) => {
            var reg = new RegExp(/^(100|[1-9]?\d(\.\d\d?\d?)?)$|0$/);
            if (reg.test(value)) {
                return true;
            }
            return false;
        }

        return {
            projectList: [],
            taskTimeInfo: {
                ttDate: '',
                ttUserId: '',
                ttProjectName: '',
                ttSpend: '',
                ttRate: '',
                ttProjectId: '',
                ttCreateBy: '',
                ttCreateDate: '',
                ttTypeDetail: '',
                ttPercentage: '',
                ttTaskType: '',
                ttPlanSpend: '',
                ttRemarks: '',
                ttState: '',
                ttReview: '',
                audit: false
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
            ]
        }
    },
    watch: {
        // 监听数据
        taskViewData(data) {
            if (data) {
                this.taskTimeInfo = data;
            }
        }
    },
    methods: {
        handleOk() {
            this.$refs['taskTimeInfo'].validate((valid) => {
                if (valid) {
                    axios.request({
                        method: 'post',
                        url: 'apis/taskTime/saveOrUpdate',
                        data: this.taskTimeInfo
                    }).then((response) => {
                        this.$emit('update-success')
                        this.$Message.success(response.data.message)
                        this.$refs['taskTimeInfo'].resetFields()
                    }).catch(() => {
                        this.$Message.error("failMessage")
                    })
                }
            })

        },
        selectProjectMethod(item) {
            this.taskTimeInfo.ttProjectName = item.projectName;
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
        }
    },

    //初始化事件
    mounted() {
        this.loadProjectMethod(true);
    }
}
</script>
