<style lang="less">
    @import './login.less';
</style>

<template>
    <div class="login">
        <div class="login-con">
            <Card icon="log-in" title="欢迎登录" :bordered="false">
                <div class="form-con">
                    <login-form @on-success-valid="handleSubmit"></login-form>
                    <p class="login-tip">输入任意用户名和密码即可</p>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
    import LoginForm from '_c/login-form'
    import {mapActions} from 'vuex'
    import { setAuthList, getMenuByRouter,getAuthList } from '@/libs/util'
    import axios from '@/libs/api.request'
    import routers from '@/router/routers'
    export default {
        components: {
            LoginForm
        },
        // 该方法会多一次请求
        watch: {
            '$route' (to, from) {
            // 在mounted函数执行的方法，放到该处
                // this.qBankId = globalVariable.questionBankId;
                // this.qBankName = globalVariable.questionBankTitle;  this.searchCharpter();
                }
              },
        methods: {
            ...mapActions([
                'handleLogin',
                'getUserInfo'
            ]),
            handleSubmit({userName, password}) {
                this.handleLogin({userName, password}).then(res => {

                    this.getUserInfo().then(res => {
                        this.$router.push({
                            name: this.$config.homeName
                        })
                    })


                    var storage = window.localStorage;
                    storage.clear();
                    window.localStorage.removeItem('authList');
                    //登录完成设置权限相关信息
                    axios.request({
                        method: 'post',
                        url: 'apis/user/getMenuAuthCode'
                    }).then((response) => {
                        setAuthList(JSON.stringify(response.data));
                        // console.log(routers)
                        // getMenuByRouter(routers, '')
                    }).catch((response) => {

                    })
                }).catch(res => {
                    this.$Notice.warning({
                        title: '提示',
                        desc: res,
                        duration: 1
                    })
                })
            }
        }
    }
</script>

<style>

</style>
