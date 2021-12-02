<template>
    <div>
        <Dropdown trigger="click" @on-click="selectLang">
            <a href="javascript:void(0)">
                {{ title }}
                <Icon :size="18" type="md-arrow-dropdown"/>
            </a>
            <DropdownMenu slot="list">
                <DropdownItem v-for="(value, key) in localList" :name="key" :key="`lang-${key}`">{{
                        value
                    }}
                </DropdownItem>
            </DropdownMenu>
        </Dropdown>

        <Modal :title="modalTitle" :show.sync="dailyModalIShow" @on-cancel="editDailData={}" :width="1600"
        >
            <dailyEdit :editDailData="editDailData" ref="dailyEdit" @update-success="editComplete"></dailyEdit>
            <div slot="footer">
                <Button type="text" size="large" @click="closeModal">取消</Button>
                <Button type="primary" size="large" @click="save(false)">保存</Button>
                <Button type="primary" size="large" @click="save(true)">保存并发送</Button>
            </div>
        </Modal>


    </div>
</template>

<script>
import dailyEdit from './daily-edit'
import axios from '@/libs/api.request'
import Modal from '@/view/components/sega/modal'
import {mapState, mapGetters, mapMutations, mapActions} from 'vuex'

export default {
    name: 'dailyMain',
    props: {
        lang: String
    },
    components: {
        axios,
        Modal,
        dailyEdit

    },
    data() {
        return {
            localList: {
                'addDaily': '新增日报',
            },
            modalTitle: '新增日报',
            dailyModalIShow: false,
            editDailData: {},

        }
    },
    watch: {
        lang(lang) {
            this.$i18n.locale = lang
        }
    },
    computed: {
        title() {
            return '日报'
        },
        ...mapState({
            loginUser: (state) => state.user
        })
    },

    methods: {
        closeModal() {
            this.dailyModalIShow = false;
            this.$refs.dailyEdit.handleCancel();
        },
        selectLang(name) {
            this.modalTitle = '新增日报';
            this.editDailData = {
                dayilUserName: this.loginUser.setUserInfo.userName,
                dayilUserId: this.loginUser.setUserInfo.userId
            };
            this.dailyModalIShow = true;
        },
        editComplete() {
            this.dailyModalIShow = false;
        },
        save(send) {
            console.log(this.$refs.dailyEdit);
            this.$refs.dailyEdit.save(send);
        }
    }
}
</script>
