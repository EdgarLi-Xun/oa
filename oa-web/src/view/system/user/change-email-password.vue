<template>
    <i-form  ref="userInfo" :model="userInfo"  :rules="ruleValidate" :label-width="120">
        <Row>
            <i-col span="24">
                <Form-item label="邮箱密码" prop="emailPassword">
                    <i-input type="password" v-model="userInfo.emailPassword" placeholder="请密码"></i-input>
                </Form-item>
            </i-col>
        </Row>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import {mapState} from "vuex";

export default {
    name: 'changeEmailPassword',
    props: [
        'editData'
    ],
    data () {
        return {
            animal:'',
            i:0,
            userInfo: {
                userId: '',
                emailPassword: '',
            },
            ruleValidate: {
                emailPassword: [
                    { required: true, message: '原密码不能为空', trigger: 'blur' }
                ],


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
            axios.request({
                method: 'post',
                url: 'apis/user/changeEmailPassword',
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
