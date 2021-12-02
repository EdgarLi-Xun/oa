<template>
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90" @submit.native.prevent>
                <Row>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="员工姓名" prop="userName">
                            <i-input v-model="condition.userName"></i-input>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem prop="userState" label="状态">
                            <Select v-model="condition.userState">
                                <Option v-for="item in userStateArray" :value="item.id"
                                        :key="item.id">{{
                                        item.text
                                    }}
                                </Option>
                            </Select>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <div style="margin-bottom: 10px;">
            <i-button type="primary" style="margin-left: 2px" @click="openModal('add')">新增</i-button>
            <i-button type="primary" style="margin-left: 10px" @click="deleteData">删除</i-button>
        </div>
        <Table border :columns="userColumns" :data="userData" @on-selection-change="selectData">
            <template slot-scope="{ row, index }" slot="action">
                <div style="display: flex;">
                    <Button type="primary" size="small" @click="openModal('edit',index)">修改</Button>
                    <Button type="primary" style="margin-left: 5px;" size="small" @click="reset(index)">重置密码</Button>
                </div>
            </template>
        </Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total" :current="page.current" @on-change="pageChange"
                      @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>

        <!--新增或編輯彈窗-->
        <Modal :title="modalTitle" :show.sync="userModalIShow" @on-cancel="editData={}" :width="900" ok-text="">
            <userEdit :editData="editData" @update-success="editComplete"></userEdit>
        </Modal>

        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete" @on-ok="comfirmDelete"
               :width="400">
            是否删除选择的数据？
        </Modal>
        <Modal
            :show.syn="resetPassModal"
            :title="resetPassTitle"
            @on-ok="ok"
            @on-cancel="cancel">
            <p>确认重置【{{ resetUser.userName }}】用户的密码</p>
        </Modal>
    </div>
</template>
<script>
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import {debuglog} from 'util';
import userEdit from './user-edit'
import Modal from '@/view/components/sega/modal'

export default {
    name: 'userIndex',
    components: {
        axios,
        Modal,
        userEdit
    },
    //数据区域
    data() {
        return {
            //删除弹窗是否显示
            deleteShow: false,
            //重置密码
            resetPassModal: false,
            //删除标识
            deleteTitle: '提示',
            resetPassTitle: '重置密码',
            // 查询条件
            condition: {
                userName: '',
                userState: ''
            },
            resetUser: {
                userName: '',
                userId: ''
            },
            //员工状态
            userStateArray: [{
                id: 0,
                text: '在职'
            }, {
                id: 1,
                text: '离职'
            }],
            //员工信息列表
            userColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '姓名',
                key: 'userName',

            }, {
                title: '登录名',
                key: 'userLoginName'
            }, {
                title: '所属部门',
                key: 'departmentName'
            }, {
                title: '角色',
                key: 'roleName'
            }, {
                title: '日报',
                key: 'daily',
                render: (h, params) => {
                    if (params.row.daily == 1) {
                        return h('div', "不需要");
                    } else {
                        return h('div', "需要");
                    }


                },
            },  {
                title: '性别',
                key: 'userGender',
                render: (h, params) => {
                    if (params.row.userGender == 1) {
                        return h('div', "女");
                    } else {
                        return h('div', "男");
                    }


                },
            }, {
                title: '邮箱',
                key: 'userEmail'
            }, {
                title: '地址',
                key: 'userAddress'
            }, {
                title: '操作',
                slot: 'action',
                width: 150,
                align: 'center'
            }],
            //用户数据
            userData: [],
            //分页信息
            page: {
                total: 100,
                current: 1,
                pageSize: 10,
            },
            //编辑弹窗是否显示
            userModalIShow: false,
            //弹窗标题
            modalTitle: '新增用户',
            //编辑的数据
            editData: {},
            //选中的行数据
            selectArray: [],
        }
    },
    //方法区域
    methods: {
        //打开界面
        openModal(type, index) {
            if (type == 'add') {
                this.modalTitle = '新增用户';
                this.editData = {};
            }
            if (type == 'edit') {
                this.modalTitle = '编辑用户';
                this.editData = this.userData[index];
            }
            this.userModalIShow = true;
        },
        //取消删除
        cancelDelete() {
            this.deleteShow = false;
        },
        //确认删除
        comfirmDelete() {
            var arryIds = [];
            for (var i = 0; i < this.selectArray.length; i++) {
                arryIds.push(this.selectArray[i].userId)
            }
            axios.request({
                method: 'post',
                url: 'apis/user/deleteByIds',
                data: arryIds
            }).then((response) => {
                this.$Message.success(response.data.message);
                this.deleteShow = false;
                this.query()
            }).catch((response) => {

            })
        },
        //删除数据
        deleteData() {
            if (this.selectArray.length == 0) {
                this.$Message.error("请选择需要删除的数据");
                return;
            }
            this.deleteShow = true;

        },
        //编辑完成
        editComplete() {
            this.query();
            this.userModalIShow = false;
        },
        //选择数据变化触发事件
        selectData(selectArray) {
            this.selectArray = selectArray;
            if (selectArray.length == 0) {
                this.editData = selectArray[0];
            } else {
                this.editData = {}
            }
        },
        //分页改变
        pageChange(pageNum) {
            this.page.current = pageNum;
            this.loadData();
        },
        //每页数据改变
        pageSizeChange(pageSize) {
            this.page.current = 1;
            this.page.pageSize = pageSize;
            this.loadData();
        },
        //查询数据
        query() {
            this.page.current = 1;
            this.loadData();
        },
        //重置查询
        handleReset() {
            this.condition = {}
            this.loadData();
        },
        //加载数据
        loadData() {
            var params = this.condition;
            params['pageNum'] = this.page.current;
            params['pageSize'] = this.page.pageSize;
            axios.request({
                method: 'post',
                url: 'apis/user/queryByPage',
                data: this.condition
            }).then((response) => {
                this.userData = response.data.obj.list;
                this.page.current = response.data.obj.pageNum;
                this.page.pageSize = response.data.obj.pageSize;
                this.page.total = response.data.obj.total;
            }).catch((response) => {

            })
        },
        //重置密码
        reset(index) {
            var user = this.userData[index];
            this.resetUser.userId = user.userId;
            this.resetUser.userName = user.userName;
            this.resetPassModal = true;
        },
        ok() {
            axios.request({
                method: 'post',
                url: 'apis/user/resetUserPass',
                data: {
                    userId: this.resetUser.userId
                }
            }).then((response) => {
                this.$Message.success(response.data.message)
                this.resetPassModal = false;
            }).catch(() => {
                this.$Message.error("failMessage")
            })
        },
        cancel() {
            this.resetPassModal = false;
        }
    },
    //初始化事件
    mounted() {
        this.loadData();
        getToken();
    }
}
</script>
