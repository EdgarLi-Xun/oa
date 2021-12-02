<template>
    <i-form ref="roleInfo" :model="roleInfo" :rules="ruleValidate" :label-width="80">
        <FormItem label="角色名称" prop="roleName">
            <i-input v-model="roleInfo.roleName"></i-input>
        </FormItem>
        <FormItem label="数据权限" prop="roleDataType">
            <Select v-model="roleInfo.roleDataType">
                <Option v-for="item in roleDataArray" :value="item.id" :key="item.id">
                    {{ item.text }}
                </Option>
            </Select>
        </FormItem>
        <FormItem label="角色介绍" prop="roleDescribe">
            <i-input v-model="roleInfo.roleDescribe" type="textarea" :autosize="{minRows: 2,maxRows:200}"
                     placeholder="请输入..."></i-input>
        </FormItem>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import {debuglog} from 'util';

export default {
    name: 'roleEdit',
    props: [
        'editData'
    ],
    data() {
        return {
            roleInfo: {
                roleName: "",
                roleDataType: 0,
                roleDescribe: ''
            },
            roleDataArray: [{
                id: 0,
                text: '个人'
            }, {
                id: 1,
                text: '所属部门'
            }, {
                id: 2,
                text: '所属部门及子部门'
            }, {
                id: 3,
                text: '全部'
            }],
            ruleValidate: {
                roleName: [
                    {required: true, message: '角色名称不能为空', trigger: 'blur'}
                ], roleDataType: [
                    {required: true, validator: this.validateRoleType, message: '数据权限不能为空', trigger: 'change'}
                ]
            }
        }
    },
    watch: {
        // 监听数据
        editData(data) {
            this.roleInfo = data;
        }
    },
    // 方法
    methods: {
        validateRoleType(rule, value, callback) {
            if (this.roleInfo.roleDataType) {
                callback();
            } else {
                return callback(new Error("数据权限不能为空"));
            }
        },
        // 确认按钮
        handleOk() {
            var flag = true
            this.$refs['roleInfo'].validate((valid) => {
                if (!valid) {
                    flag = false
                }
            })
            if (!flag) {
                return
            }
            axios.request({
                method: 'post',
                url: '/apis/role/saveOrUpdate',
                data: this.roleInfo
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
        handleCancel() {
            this.$refs['roleInfo'].resetFields()
        }
    },
    // 初始化方法
    mounted() {
        axios.request({
            method: 'post',
            url: 'apis/department/queryTreeData'
        }).then((response) => {
            this.treeData = response.data.obj;
        }).catch((response) => {

        })
    }
}
</script>
