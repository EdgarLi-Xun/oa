<template>
    <i-form ref="userInfo" :model="userInfo" :rules="ruleValidate" :label-width="80">
        <Row>
            <i-col span="12">
                <Form-item label="姓名" prop="userName">
                    <i-input v-model="userInfo.userName" placeholder="请输入姓名"></i-input>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="登录名" prop="userLoginName">
                    <i-input v-model="userInfo.userLoginName" placeholder="请输入登录名"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="用户角色" prop="userRoleId">
                    <Select v-model="userInfo.userRoleId">
                        <Option v-for="item in userRoleArray" :value="item.roleId"
                                :key="item.roleId">{{ item.roleName }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="员工状态" prop="state">
                    <Select v-model="userInfo.userState">
                        <Option v-for="item in userStateArray" :value="item.id"
                                :key="item.id">{{
                                item.text
                            }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="所属部门" prop="department">
                    <treeselect v-model="userInfo.userDepartmentId" :options="treeData" :default-expand-level="3"
                                placeholder="请选择所属部门"/>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="邮箱" prop="userEmail">
                    <i-input v-model="userInfo.userEmail" placeholder="请输入邮箱" :isDefaultExpanded="true"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="日报">
                    <input type="radio" name="daily" v-model="userInfo.daily" value="0"/>需要
                    <input type="radio" name="daily" v-model="userInfo.daily" value="1"/>不需要
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="性别">
                    <input type="radio" name="sex" v-model="userInfo.userGender" id="s1" value="0"/>男
                    <input type="radio" name="sex" v-model="userInfo.userGender" id="s2" value="1"/>女
                </Form-item>
            </i-col>

        </Row>
        <Row>
<!--            <i-col span="12">-->

<!--            <Form-item label="工时类型" prop="userTaskTime">-->
<!--                    <Select v-model="userInfo.userTaskTime" clearable>-->
<!--                        <Option v-for="item in typeList" :value="item.value" :key="item.value">-->
<!--                            {{ item.label }}-->
<!--                        </Option>-->
<!--                    </Select>-->
<!--                </Form-item>-->
<!--            </i-Col>-->
            <i-col span="12">
                <Form-item label="联系电话" prop="userTelephone">
                    <i-input v-model="userInfo.userTelephone" placeholder="请输入联系电话"></i-input>
                </Form-item>
            </i-col>

        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="年龄" prop="userAge">
                    <i-input v-model="userInfo.userAge" placeholder="请输入年龄"></i-input>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="身份证">
                    <i-input v-model="userInfo.userIdCard" placeholder="请输入身份证"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Form-item label="地址">
            <i-input v-model="userInfo.userAddress" type="textarea" :autosize="{minRows: 2,maxRows:200}"
                     placeholder="请输入地址"></i-input>
        </Form-item>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import selectTree from 'iview-select-tree'
// import the component
import Treeselect from '@riophae/vue-treeselect'
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
    name: 'userEdit',
    props: [
        'editData'
    ],
    components: {
        selectTree,
        Treeselect
    },
    data() {
        return {
            animal: '',
            treeData: [],
            i: 0,
            userRoleArray: [],
            userStateArray: [{
                id: 0,
                text: '在职'
            }, {
                id: 1,
                text: '离职'
            }],
            typeList: [
                {
                    value: 1,
                    label: '管理'
                },
                {
                    value: 2,
                    label: '开发'
                },
                {
                    value: 3,
                    label: '测试'
                },
                {
                    value: 4,
                    label: '实施'
                }
            ],

            userInfo: {
                userId: '',
                userName: '',
                userPassword: '',
                userRoleId: '',
                userState: '',
                userEmail: '',
                userDepartmentId: '',
                userTelephone: '',
                userLoginName: '',
                userTaskTime: '',
                userGender: 0,
                daily: 0,
                userAge: '',
                userIdCard: '',
                userAddress: '',

            },
            ruleValidate: {
                userName: [
                    {required: true, message: '姓名不能为空', trigger: 'blur'}
                ],
                userLoginName: [
                    {required: true, message: '姓名不能为空', trigger: 'blur'}
                ],
                userEmail: [
                    {required: true, message: '邮箱不能为空', trigger: 'blur'},
                    {type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
                ],
                userTelephone: [
                    {required: true, message: '电话不能为空', trigger: 'blur'},
                    {validator: this.validatephone, message: '电话格式不正确', trigger: 'blur'}
                ],
                userTaskTime: [
                    { required: true, message: '内容不能为空', trigger: 'blur' }
                ],
                userRoleId: [
                    {required: true, type: 'date', message: '请选择用户角色', trigger: 'change'}
                ],
                state: [
                    {required: true, validator: this.validateState, trigger: 'change'}
                ],
                department: [
                    {required: true, validator: this.validateDepartment, trigger: 'change'}
                ]
            }
        }
    },
    watch: {
        // 监听数据
        editData(data) {
            this.userInfo = data;
            if (this.userInfo.userId) {
                axios.request({
                    method: 'get',
                    url: '/apis/user/get/' + this.userInfo.userId
                }).then((response) => {
                    this.userInfo = response.data.obj;
                }).catch((response) => {

                })
            }
        }
    },
    // 方法
    methods: {
        validateState(rule, value, callback) {
            if (this.userInfo.userState >= 0) {
                callback();
            } else {
                return callback(new Error("请选择员工状态"));
            }
        },
        validateDepartment(rule, value, callback) {
            if (this.userInfo.userDepartmentId) {
                callback();
            } else {
                return callback(new Error("请选择所属部门"));
            }
        },
        validatephone(rule, value, callback) {
            let reg = /^1[3456789]\d{9}$/;
            if (value != '' && reg.test(value)) {
                callback()
            } else {
                callback(new Error('请输入正确的手机号'))
            }
        },
        // 确认按钮
        handleOk() {
            var flag = true
            this.$refs['userInfo'].validate((valid) => {
                if (!valid) {
                    flag = false
                }
            })

            if (!flag) {
                return
            }
            axios.request({
                method: 'post',
                url: 'apis/user/saveOrUpdate',
                data: this.userInfo
            }).then((response) => {
                if (response.data.success) {
                    this.$emit('update-success')
                    this.$Message.success(response.data.message)
                } else {
                    this.$Message.error(response.data.message);
                }
            }).catch(() => {
                this.$Message.error("failMessage")
            })
        },
        changeTreeSelectData() {
        },
        changeTreeData() {
        },
        handleTreeSelectChange(data, checked, node) {
        },
        handleTreeSelectExpand(item) {
            console.log('toggle expand', item);
        },
        handleTreeSelectCheckChange(selectedArray, item) {
            console.log(selectedArray, item);
        },
        handleTreeSelectClick(selectArray, item) {
        },
        zlTreeData(data) {
            for (var i = 0; i < data.length; i++) {
                if (data[i].children && data[i].children.length > 0) {
                    data[i].expand = true;
                    data[i].disabled = true;
                    data[i].children = this.zlTreeData(data[i].children);
                } else {
                    data[i].checked = data[i].departmentId == this.userInfo.userDepartmentId;
                }
            }
            return data;
        },
        renderContent(h, {root, node, data}) {
            return h('Option', {
                style: {
                    display: 'inline-block',
                    margin: '0'
                },
                disabled: data.disable,
                props: {
                    value: data.departmentId
                }
            }, data.departmentName);
        },
        handleCancel() {
            this.$refs['userInfo'].resetFields()
        }
    },
    // 初始化方法
    created() {
        axios.request({
            method: 'post',
            async: false,
            url: 'apis/department/queryTreeData'
        }).then((response) => {
            this.treeData = this.zlTreeData(response.data.obj);
            // this.treeData = response.data.obj;
        }).catch((response) => {

        });
        axios.request({
            url: 'apis/role/queryAll',
            method: 'post',
            data: {}
        }).then(res => {
            this.userRoleArray = res.data.obj;
        });
    }
}
</script>
