<template>
    <i-form  ref="projectInfo" :model="projectInfo"  :rules="ruleValidate" :label-width="80">
        <Row>
            <i-col span="12">
                <Form-item label="项目名称" prop="projectName">
                    <i-input disabled="disabled" v-model="projectInfo.projectName" placeholder="请输入项目名称"></i-input>
                </Form-item>

            </i-col>
            <i-col span="12">
                <Form-item label="项目成员" prop="pmUserId">
                    <Select v-model="projectInfo.pmUserId"  @on-open-change="selectuserMethod" >
                        <Option v-for="item in userList" :value="item.userId" :key="item.userId" >{{ item.userName }}</Option>
                    </Select>
                </Form-item>
            </i-col>
        </Row>


    </i-form>
</template>
<script>
    import axios from '@/libs/api.request'
    import TreeSelect from '_c/tree-select'
    import { newTreeData } from '@/mock/data/tree-select'
    import { getTreeSelectData } from '@/api/data'
    import { debuglog } from 'util';
    export default {
        name: 'userEdit',
        props: [
            'editData'
        ],
        components: {
            TreeSelect
        },
        data () {
            return {
                animal:'',
                treeSelected: [112, 113],
                treeData: [],
                userList: [],
                projectStateArray: [{
                    id: 0,
                    text: '未启动'
                },{
                    id: 1,
                    text: '进行中'
                },{
                    id: 2,
                    text: '已结束'
                }],
                projectInfo: {
                    pmId: '',
                    pmUserId: '',
                    pmUserName: '',
                    pmProjectId: ''
                },
                ruleValidate: {
                    pmId: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' }
                    ],
                    pmUserId: [
                        { required: true, message: '用户不能为空', type: 'number',trigger: 'change' },
                    ],

                }
            }
        },
        watch: {
            // 监听数据
            editData (data) {
                this.projectInfo = data;
            }
        },
        // 方法
        methods: {
            // 确认按钮
            handleOk () {
                var flag = true
                this.$refs['projectInfo'].validate((valid) => {
                    if (valid) {
                        axios.request({
                            method: 'post',
                            url: 'apis/projectMember/saveOrUpdate',
                            data: this.projectInfo
                        }).then((response) => {
                            this.$emit('update-success')
                            this.$Message.success(response.data.message)
                        }).catch(() => {
                            this.$Message.error("failMessage")
                        })
                    }
                })



            },
            changeTreeSelectData () {
                this.treeSelected = [111, 114]
            },
            changeTreeData () {
                this.treeData = newTreeData
                // this.treeSelected = [];
            },
            handleTreeSelectChange (list) {
                // console.log('=-========', list);
            },
            handleTreeSelectExpand (item) {
                // console.log('toggle expand', item);
            },
            handleTreeSelectCheckChange (selectedArray, item) {
                // console.log(selectedArray, item);
            },
            handleTreeSelectClick (selectArray, item) {
                // console.log(selectArray, item);
            },
            selectuserMethod () {

                axios.request({
                    url: 'apis/user/queryAll',
                    method: 'post',
                    data: {
                    }
                }).then(res => {
                    this.userList = res.data.obj;
                })
            },
            loadData (item, callback) {

                let data = [{
                    id: 111,
                    title: '1-1-1'
                },
                    {
                        id: 112,
                        title: '1-1-2'
                    },
                    {
                        id: 113,
                        title: '1-1-3'
                    },
                    {
                        id: 114,
                        title: '1-1-4'
                    }]
            }
        },
        // 初始化方法
        mounted () {
            this.loadData();
            axios.request({
                method: 'post',
                url: 'apis/department/queryTreeData'
            }).then((response) => {

                this.treeData = response.data.obj;
            }).catch((response) => {

            })
            this.selectuserMethod()

        }
    }
</script>
