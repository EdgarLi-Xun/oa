<template>
    <i-form  ref="userInfo" :model="userInfo"  :rules="ruleValidate" :label-width="120">
        <Row>
            <i-col span="24">
                 <Form-item label="姓名" prop="userName">
                    <i-input v-model="userInfo.userName" readonly placeholder="请输入姓名"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="24">
                <Form-item label="原密码" prop="originPassword">
                    <i-input type="password" v-model="userInfo.originPassword" placeholder="请输入原密码"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="24">
                <Form-item label="新密码" prop="userPassword">
                    <i-input type="password" v-model="userInfo.userPassword" placeholder="请输入新密码"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>

            <i-col span="24">
                <Form-item label="新密码确认" prop="confirmPassword">
                    <i-input type="password" v-model="userInfo.confirmPassword" placeholder="新密码确认"></i-input>
                </Form-item>
            </i-col>
        </Row>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import {mapState} from "vuex";

export default {
    name: 'changePassword',
    props: [
        'editData'
    ],
    data () {
        return {
            animal:'',
            i:0,
            userInfo: {
                userId: '',
                userName: '',
                originPassword: '',
                userPassword: '',
                confirmPassword: '',
            },
            ruleValidate: {
                userName: [
                    { required: true, message: '姓名不能为空', trigger: 'blur' }
                ],
                originPassword: [
                    { required: true, message: '原密码不能为空', trigger: 'blur' }
                ],
                userPassword: [
                    { required: true, message: '新密码不能为空', trigger: 'change' }
                ],
                confirmPassword: [
                    { required: true, message: '确认密码不能为空', trigger: 'change' }
                ]
            }
        }
    },
    watch: {
        // 监听数据
        editData (data) {
            this.userInfo = data;
        }
    },

    // 方法
    methods: {
        // 确认按钮
        handleOk () {
            var flag = true
            this.$refs['userInfo'].validate((valid) => {
                if (!valid) {
                    flag = false
                }
            })
            if (!flag) {
                return
            }
            if(this.userInfo.confirmPassword != this.userInfo.userPassword){
                this.$Message.info("新密码与确认密码不一致");
                return;
            }
            axios.request({
                method: 'post',
                url: 'apis/user/changePassword',
                data: this.userInfo
            }).then((response) => {
                this.$emit('update-success')
                this.$Message.success(response.data.message)
            }).catch(() => {
                this.$Message.error("failMessage")
            })
            return;
        }
    },
    // 初始化方法
    mounted () {

    }
}
</script>
