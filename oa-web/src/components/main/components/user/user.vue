<template>
    <div class="user-avatar-dropdown">
        <Dropdown trigger="click" @on-click="handleClick">

            <a href="javascript:void(0)">
                {{ editData.userName }}
                <Icon :size="18" type="md-arrow-dropdown"/>
            </a>
            <DropdownMenu slot="list">
                <!--        <DropdownItem name="message">-->
                <!--          消息中心<Badge style="margin-left: 10px" :count="messageUnreadCount"></Badge>-->
                <!--        </DropdownItem>-->
                <DropdownItem name="changePassword">
                    修改密码
                    <Badge style="margin-left: 10px"></Badge>
                </DropdownItem>
                <DropdownItem name="changeEmailPassword">
                    邮箱密码
                    <Badge style="margin-left: 10px"></Badge>
                </DropdownItem>
                <DropdownItem name="logout">退出登录</DropdownItem>
            </DropdownMenu>
        </Dropdown>

        <Modal :title="modalTitle" :show.sync="dailyModalIShow" @on-cancel="cancel" :width="600" ok-text="">
            <changePassword :editData="editData" @update-success="editComplete"></changePassword>
        </Modal>

        <Modal :title="modalTitle" :show.sync="emailPasswordModalIShow" @on-cancel="cancel" :width="600" ok-text="">
            <changeEmailPassword :editData="editData" @update-success="editComplete"></changeEmailPassword>
        </Modal>

    </div>
</template>

<script>
import './user.less'
import changePassword from '@/view/system/user/change-password'
import changeEmailPassword from '@/view/system/user/change-email-password'
import dailyEdit from '@/view/project/dailypaper/daily-edit'
import axios from '@/libs/api.request'
import Modal from '@/view/components/sega/modal'
import {mapActions, mapState} from 'vuex'

export default {
    name: 'User',
    components: {
        axios,
        Modal,
        changePassword,
        changeEmailPassword,
        dailyEdit
    },
    props: {
        userAvatar: {
            type: String,
            default: ''
        },
        messageUnreadCount: {
            type: Number,
            default: 0
        }
    },
    data() {
        return {
            modalTitle: '修改密码',
            dailyModalIShow: false,
            emailPasswordModalIShow: false,
            editData: {
                userName: ''
            }
        }
    },
    computed: {
        ...mapState({
            loginUser: (state) => state.user
        })
    },
    methods: {
        ...mapActions([
            'handleLogOut'
        ]),
        logout() {
            // axios.request({
            //     method: 'get',
            //     url: '/logout'
            // }).then((response) => {
            //     resolve()
            // }).catch((response) => {
            //
            // })
            this.handleLogOut().then(() => {
                this.$router.push({
                    name: 'login'
                })
            })
        },
        message() {
            this.$router.push({
                name: 'message_page'
            })
        },
        changePassword() {
            this.editData = {userName: this.loginUser.setUserInfo.userName};
            this.dailyModalIShow = true;
        },
        changeEmailPassword() {
            const user = this.loginUser.setUserInfo;
            this.editData = {userId: user.userId, emailPassword: user.emailPassword, userName: user.userName};
            this.emailPasswordModalIShow = true;
        },
        handleClick(name) {
            switch (name) {
                case 'logout':
                    this.logout()
                    break
                case 'message':
                    this.message()
                    break
                case 'changePassword':
                    this.changePassword()
                    break
                case 'changeEmailPassword':
                    this.changeEmailPassword()
                    break
            }
        }, editComplete() {
            this.dailyModalIShow = false;
            this.emailPasswordModalIShow = false;
        }, cancel() {
            this.editData = {userName: this.loginUser.setUserInfo.userName};
        }
    }, mounted() {
        this.editData = {userName: this.loginUser.setUserInfo.userName};
    }
}

</script>
