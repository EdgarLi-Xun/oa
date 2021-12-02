<template>
    <i-form ref="departmentInfo" :model="departmentInfo" :rules="ruleValidate" :label-width="80">
        <FormItem label="部门编号" prop="code">
            <i-input v-model="departmentInfo.departmentCode" placeholder="请填写部门编号" :readonly="model==='view'"></i-input>
        </FormItem>
        <FormItem label="部门名称" prop="name">
            <i-input v-model="departmentInfo.departmentName" placeholder="请填写部门名称" :readonly="model==='view'"></i-input>
        </FormItem>
        <FormItem label="上级部门">
            <i-input disabled v-model="departmentInfo.parentDepartment.departmentName"></i-input>
        </FormItem>
        <FormItem label="部门介绍">
            <i-input v-model="departmentInfo.departmentIntroduction" type="textarea"
                     :autosize="{minRows: 2,maxRows:200}" :readonly="model==='view'" placeholder="请输入..."></i-input>
        </FormItem>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'

export default {
    name: 'departmentEdit',
    props: [
        'editData',
        'model'
    ],
    data() {
        return {
            model: 'creat',
            departmentInfo: {
                departmentCode: '',
                departmentName: '',
                departmentIntroduction: '',
                parentDepartment: {
                    departmentName: '',
                }
            },
            ruleValidate: {
                name: [
                    {required: true, validator: this.validateName, message: '部门名称不能为空', trigger: 'blur'}
                ],
                code: [
                    {required: true,  validator: this.validateCode,message: '部门编号不能为空', trigger: 'blur'}
                ]
            }
        }
    },
    watch: {
        // 监听数据
        editData(data) {
            this.departmentInfo = data;
        },
        model(data) {
            this.model = data;
        }
    },
    // 方法
    methods: {
        validateName(rule, value, callback) {
            if (this.departmentInfo.departmentName ) {
                callback();
            } else {
                return callback(new Error("部门名称不能为空"));
            }
        },
        validateCode(rule, value, callback) {
            if (this.departmentInfo.departmentName ) {
                callback();
            } else {
                return callback(new Error("部门编号不能为空"));
            }
        },
        handleCancel () {
            this.$refs['departmentInfo'].resetFields()
        },
        // 确认按钮
        handleOk() {
            var flag = true
            this.$refs['departmentInfo'].validate((valid) => {
                if (!valid) {
                    flag = false
                }
            })
            if (!flag) {
                return
            }
            axios.request({
                method: 'post',
                url: '/apis/department/saveOrUpdate',
                data: this.departmentInfo
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
        }
    }
}
</script>
