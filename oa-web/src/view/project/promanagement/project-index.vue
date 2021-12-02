<template>
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90" @submit.native.prevent>
                <Row>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem label="项目名称" prop="projectName">
                            <i-input v-model="condition.projectName"></i-input>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="12" :md="8" :lg="7" class="col-max-width">
                        <FormItem prop="projectState" label="项目状态">
                            <Select v-model="condition.projectState" clearable>
                                <Option v-for="item in projectStateArray" :value="item.id"
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
            <i-button type="primary" style="margin-left: 10px" @click="openModal('edit')">修改</i-button>
            <i-button type="error" style="margin-left: 10px" @click="deleteData">删除</i-button>
        </div>
        <Table border :columns="projectColumns" :data="projectData" @on-selection-change="selectData"></Table>
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
    </div>
</template>
<script>
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import {debuglog} from 'util';
import userEdit from './project-edit'
import Modal from '@/view/components/sega/modal'
import projectEdit from './projectuser-edit'
import expandRow from './projectuser.vue';

export default {
    name: 'userIndex',
    components: {
        axios,
        Modal,
        userEdit,
        projectEdit
    },
    //数据区域
    data() {
        return {
            //删除弹窗是否显示
            deleteShow: false,
            addUserShow: false,
            //删除标识
            deleteTitle: '提示',
            // 查询条件
            condition: {
                projectName: '',
                projectState: ''
            },
            //员工状态
            projectStateArray: [{
                id: 0,
                text: '未启动'
            }, {
                id: 1,
                text: '进行中'
            }, {
                id: 2,
                text: '已结束'
            }],
            //项目信息列表
            projectColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }
                /*, {
                    type: 'expand',
                    width: 50,
                    render: (h, params) => {
                        return h(expandRow, {
                            props: {
                                row: params.row
                            }
                        })
                    }
                }*/
                , {
                    title: '项目名称',
                    key: 'projectName'
                }, {
                    title: '客户单位',
                    key: 'projectCustomer'
                }, {
                    title: '项目状态',
                    width: 120,
                    key: 'projectStateName'
                }, {
                    title: '立项日期',
                    width: 120,
                    key: 'approvalDate'
                }, {
                    title: '上线日期',
                    width: 120,
                    key: 'launchDate'
                }, {
                    title: '验收日期',
                    width: 120,
                    key: 'acceptanceDate'
                }, {
                    title: '项目经理',
                    width: 120,
                    key: 'prjectManagerName'
                }, {
                    title: '项目成员',
                    key: 'projectMemberNames'
                }, {
                    title: '备注',
                    key: 'projectRemark'
                }],
            //用户数据
            projectData: [],
            //分页信息
            page: {
                total: 100,
                current: 1,
                pageSize: 10,
            },
            //编辑弹窗是否显示
            userModalIShow: false,
            projectModalIShow: false,
            //弹窗标题
            modalTitle: '新增项目',
            //编辑的数据
            editData: {},
            //选中的行数据
            selectArray: [],
        }
    },
    //方法区域
    methods: {
        //打开界面
        openModal(type) {
            if (type == 'add') {
                this.modalTitle = '新增项目';
                this.editData = {};
            }
            if (type == 'edit') {
                this.modalTitle = '编辑项目';
                if (this.selectArray.length > 1) {
                    this.$Message.info("请选择一条数据 ");
                    return;
                } else if (this.selectArray.length === 1) {
                    this.editData = this.selectArray[0];
                } else {
                    this.$Message.info("请选择需要编辑的数据");
                    return;
                }
            }
            this.userModalIShow = true;
        },
        openUserModal(type) {
            this.projectModalIShow = true;
        },
        //取消删除
        cancelDelete() {
            this.deleteShow = false;
        },
        canceladdUser() {
            this.addUserShow = false;
        },
        //确认删除
        comfirmDelete() {
            var arryIds = [];
            for (var i = 0; i < this.selectArray.length; i++) {
                arryIds.push(this.selectArray[i].projectId)
            }
            axios.request({
                method: 'post',
                url: 'apis/project/deleteByIds',
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
        adduser() {
            if (this.selectArray.length == 0) {
                this.$Message.error("请选择需要添加成员的部门");
                return;
            }
            if (this.selectArray.length > 1) {
                this.$Message.error("请选择一条数据");
                return;
            }
            this.addUserShow = true;

        },
        //编辑完成
        editComplete() {
            this.query();
            this.userModalIShow = false;
        },
        edituserComplete() {
            this.query();
            this.projectModalIShow = false;
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
                url: 'apis/project/queryByPage',
                data: this.condition
            }).then((response) => {
                this.projectData = response.data.obj.list;
                //翻译项目状态
                this.projectData.forEach((pd) => {
                    this.projectStateArray.forEach(psa => {
                        if (psa.id == pd.projectState) {
                            pd.projectStateName = psa.text;
                        }
                    })
                })
                this.page.current = response.data.obj.pageNum;
                this.page.pageSize = response.data.obj.pageSize;
                this.page.total = response.data.obj.total;
            }).catch((response) => {

            })
        }
    },
    //初始化事件
    mounted() {
        this.loadData();
        getToken();
    }
}
</script>
