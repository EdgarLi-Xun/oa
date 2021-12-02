<template>
    <div id="printTest">
        <Row style="height:100%;width:100%">
            <i-col span="10">
                <p slot="title">
                    角色管理
                </p>
                <div style="margin-bottom: 10px;">
                    <i-button v-if="this.hasPermission('0403010000')" type="primary" style="margin-left: 2px"
                              @click="openRoleModal('add')">新增
                    </i-button>

                    <i-button v-if="this.hasPermission('0403030000')" type="primary" style="margin-left: 10px"
                              @click="deleteRoleData">删除
                    </i-button>
                    <!--                    <i-button v-print="'#printTest'" type="primary" style="margin-left: 10px">打印</i-button>-->
                </div>
                <Table border stripe :columns="roleColumns" @on-current-change="roleDataSelect" highlight-row
                       :data="roleData" @on-selection-change="selectData">
                    <template slot-scope="{ row, index }" slot="action">
                        <Button size="small" type="primary"
                                @click="openRoleModal('edit',index)">修改
                        </Button>
                    </template>
                </Table>
            </i-col>
            <i-col span="3"></i-col>
            <i-col span="13">
                <p slot="title">
                    模块管理
                </p>
                <div style="margin-bottom: 10px;">
                    <!--                    <i-button type="primary" v-if="this.hasPermission('0403040000')" style="margin-left: 2px"-->
                    <!--                              @click="openModelModal('add')">新增-->
                    <!--                    </i-button>-->
                    <!--                    <i-button type="primary" v-if="this.hasPermission('0403050000')" style="margin-left: 10px"-->
                    <!--                              @click="openModelModal('edit')">修改-->
                    <!--                    </i-button>-->
                    <!--                    <i-button type="primary" v-if="this.hasPermission('0403060000')" style="margin-left: 10px"-->
                    <!--                              @click="deleteModelData">删除-->
                    <!--                    </i-button>-->
                    <i-button type="primary" v-if="this.hasPermission('0403070000')" style="margin-left: 10px"
                              @click="permissionAssign">权限分配
                    </i-button>
                </div>
                <tree-table
                    ref="modelTable"
                    border
                    expand-key="modelCode"
                    :expand-type="false"
                    :show-row-hover="true"
                    :columns="modelColumns"
                    @checkbox-click="modelDataSelect"
                    :data="modelData">
                </tree-table>
            </i-col>
        </Row>
        <!--新增或編輯 角色 彈窗-->
        <Modal :title="roleModalTitle" :show.sync="roleModalIShow" @on-cancel="editData={}" :width="500" ok-text="">
            <roleEdit :editData="roleSelectData" @update-success="editComplete"></roleEdit>
        </Modal>

        <Modal :title="modelModalTitle" :show.sync="modelModalIShow" @on-cancel="editData={}" :width="500" ok-text="">
            <modelEdit :editData="modelEditData" @update-success="modelEditComplete"></modelEdit>
        </Modal>
        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete" @on-ok="comfirmDelete"
               :width="400">
            {{ deleteMessage }}
        </Modal>
    </div>
</template>
<script>
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import Modal from '@/view/components/sega/modal'
import roleEdit from './role-edit'
import modelEdit from './model-edit'

export default {
    components: {
        axios,
        Modal,
        roleEdit,
        modelEdit
    },
    data() {
        return {
            //角色弹窗标题
            roleModalTitle: '新增角色',
            //角色弹窗是否显示
            roleModalIShow: false,
            //删除弹窗是否显示
            deleteShow: false,
            //删除提示信息
            deleteMessage: "确认删除该角色数据？",
            //模块弹窗标题
            modelModalTitle: '新增模块',
            //模块弹窗是否显示
            modelModalIShow: false,
            //提示
            deleteTitle: '提示',
            // 删除类型 role or model
            deleteType: "role",
            //选择的角色数据
            roleSelectData: {},
            //角色数据
            roleData: [],
            //数据权限的数据
            roleDataType: {
                0: '个人',
                1: '所属部门',
                2: '所属部门及子部门',
                3: '全部'
            },
            //员工信息列表
            roleColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '角色名称',
                key: 'roleName'
            }, {
                title: '角色数据类型',
                key: 'roleDataType',
                render: (h, params) => {
                    return h('span', {
                        domProps: {
                            innerHTML: this.roleDataType[params.row.roleDataType]
                        }
                    })
                }
            }, {
                title: '角色描述',
                key: 'roleDescribe'
            }, {
                title: '操作',
                slot: 'action',
                width: 150,
                align: 'center'
            }],
            //模块列信息
            modelColumns: [{
                title: '模块编号',
                key: 'modelCode',
                resizable: true
            }, {
                title: '模块名称',
                key: 'modelName'
            },
                //     {
                //     title: '模块链接',
                //     key: 'modelUrl'
                // },
                {
                    title: '模块描述',
                    key: 'modelDescribe'
                }],
            //角色已选择的数据
            roleSelectArray: [],
            //模块数据
            modelData: [],
            //角色当前选择事件
            roleCurrentData: {},
            //模块选择的数据
            modelSelectData: [],
            //选择的第一条数据
            modelEditData: {},
            //选择的模块数据
            modelCheckIds: [],
        }
    },
    methods: {
        //权限分配
        permissionAssign() {
            var checkedModelIds = this.$refs.modelTable.getCheckedProp(["modelId"]);
            if (checkedModelIds.length == 0) {
                this.$Message.success("请选择需要分配的权限");
                return;
            }
            if (!this.roleSelectData.roleId) {
                this.$Message.success("请选择需要分配的角色");
                return;
            }
            var params = {
                modelIds: JSON.stringify(checkedModelIds).replace("[", "").replace("]", ""),
                roleId: this.roleSelectData.roleId
            }
            axios.request({
                method: 'post',
                url: 'apis/role/permissionAssign',
                params: params
            }).then((response) => {
                this.$Message.success(response.data.message);
                this.loadModelData(this.roleSelectData.roleId)
            }).catch((response) => {
                this.$Message.success("分配失败");
            })
        },
        //角色选中事件
        roleDataSelect(roleData) {
            this.roleSelectArray = [];
            this.roleSelectArray.push(roleData);
            this.roleSelectData = roleData;
            this.loadModelData(roleData.roleId);
        },
        //选择数据变化触发事件
        selectData(selectArray) {
            this.roleSelectArray = selectArray;
            if (selectArray.length == 0) {
                this.roleSelectData = selectArray[0];
            } else {
                this.roleSelectData = {}
            }
        },
        //编辑完成事件
        editComplete() {
            this.loadRoleData();
            this.roleModalIShow = false;
        },
        //打开角色弹窗
        openRoleModal(params, index) {
            if (params == "add") {
                this.roleSelectData = {}
                this.roleModalTitle = '新增角色';
            }
            if (params == "edit") {
                // if (this.roleSelectArray.length == 0) {
                //     this.$Message.success("请选择一条数据");
                //     return;
                // }
                this.roleSelectData = this.roleData[index];
                this.roleModalTitle = '编辑角色';
            }
            this.roleModalIShow = true;
        },
        //打开模块弹窗
        openModelModal(params) {
            if (params == "add") {
                var beforeData = this.modelEditData;
                this.modelEditData = {};
                this.modelEditData.modelParentId = beforeData.modelId;
                this.modelModalTitle = '新增模块';
            }
            if (params == "edit") {
                if (this.modelEditData.modelId == null) {
                    this.$Message.success("请选择一条数据");
                    return;
                }
                this.modelModalTitle = '修改模块';
            }
            this.modelModalIShow = true;
        },
        //模块选择事件
        modelDataSelect(data, arr, event) {
            this.modelEditData = data;
        },
        //点击一条数据时
        modelDataSelectSingle(data, arr, event) {
            this.modelEditData = data.row;
        },
        //删除角色数据
        deleteRoleData() {
            if (this.roleSelectArray.length == 0) {
                this.$Message.error("请选择需要删除的数据");
                return;
            }
            this.deleteMessage = "确认删除该角色数据吗？";
            this.deleteType = "role";
            this.deleteShow = true;
        },
        //查询角色数据
        loadRoleData() {
            axios.request({
                method: 'post',
                url: 'apis/role/queryAll',
                data: {}
            }).then((response) => {
                this.roleData = response.data.obj;
            }).catch((response) => {

            })
        },
        //确认删除
        comfirmDelete() {
            var arryIds = [];
            if (this.deleteType == "role") {
                for (var i = 0; i < this.roleSelectArray.length; i++) {
                    arryIds.push(this.roleSelectArray[i].roleId)
                }
            } else {
                arryIds.push(this.modelEditData.modelId);
            }
            axios.request({
                method: 'post',
                url: 'apis/' + this.deleteType + '/deleteByIds',
                data: arryIds
            }).then((response) => {
                this.$Message.success(response.data.message);
                this.deleteShow = false;
                if (this.deleteType == "role") {
                    this.loadRoleData()
                } else {
                    this.loadModelData()
                }
            }).catch((response) => {
                this.$Message.success("删除失败");
            })
        },
        //取消删除
        cancelDelete() {
            this.roleModalIShow = false;
        },
        //加载模块数据
        loadModelData(roleId) {
            axios.request({
                method: 'post',
                url: 'apis/model/queryTree',
                data: {roleId: roleId}
            }).then((response) => {
                this.modelData = response.data.obj;
            }).catch((response) => {

            })
        },
        //模块编辑完成事件
        modelEditComplete() {
            this.loadModelData();
            this.modelModalIShow = false;
        },
        //删除模块数据
        deleteModelData() {
            if (JSON.stringify(this.modelEditData).indexOf("{}") != -1) {
                this.$Message.success("请选择需要删除的模块数据");
                return;
            }
            this.deleteMessage = "确认删除该模块数据吗？";
            this.deleteType = "model";
            this.deleteShow = true;
        }
    },
    //初始化事件
    mounted() {
        this.loadRoleData();
        this.loadModelData();

    }
}
</script>
