<template>
    <div>
        <div slot="content">
            <Form ref="condition" :model="condition" :label-width="90">
                <Row>
                    <i-col :xs="24" :sm="6" :md="6" :lg="6" class="col-max-width">
                        <FormItem label="日期" prop="date">
                            <Row>
                                <i-Col span="11">
                                    <FormItem prop="startDate">
                                        <DatePicker type="date" v-model="condition.startDate"
                                                    :editable="false" :options="startTimeOptions"
                                                    @on-change="startTimeChange"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                                <i-Col span="2" style="text-align: center">-</i-Col>
                                <i-Col span="11">
                                    <FormItem prop="dateEnd">
                                        <DatePicker type="date" v-model="condition.endDate"
                                                    :editable="false" :options="endTimeOptions"
                                                    @on-change="endTimeChange" @on-clear="endTimeClear"
                                                    style="width:100%"></DatePicker>
                                    </FormItem>
                                </i-Col>
                            </Row>
                        </FormItem>
                    </i-col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="发送人">
                                <i-input value="" v-model="condition.dayilUserName" placeholder="请输入填报人" />
                        </Form-item>
                    </i-col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="收件人">
                            <Select v-model="condition.dayilsjrName" clearable>
                                <Option v-for="item in sjrList" :value="item.email" :key="item.userName">
                                    {{ item.userName }}
                                </Option>
                            </Select>
                        </Form-item>
                    </i-col>
                    <i-col :xs="24" :sm="6" :md="6" :lg="5" class="col-max-width">
                        <Form-item label="日报主题">
                                <i-input value="" v-model="condition.dailyTheme" placeholder="请输入主题" />
                        </Form-item>
                    </i-col>
                </Row>
            </Form>
            <!-- 查询重置按钮-->
            <div style="text-align:center;margin-bottom: 10px;">
                <Button type="primary" @click="query()">查询</Button>
                <Button @click="handleReset('condition')" style="margin-left: 8px">重置</Button>
            </div>
        </div>
        <div style="margin-bottom: 10px">
            <i-button type="info" style="margin-left: 2px" @click="openXj()">查看下级日报</i-button>
        </div>
        <Modal :title="subordinateTitle"  :show.sync="subordinateShow"  :width="1300" ok-text="">
            <dailyXs></dailyXs>
            <div slot="footer">
                <Button type="text" size="large" @click="shut()">取消</Button>
            </div>
        </Modal>
        <Table border :columns="daliyColumns" :data="dailyData">
            <template slot-scope="{ row, index }" slot="action">
                <div style="display: flex">
                    <Button type="primary" size="small" @click="edit(index)">编辑</Button>
                    <Button type="primary" size="small" style="margin-left: 5px" @click="show(index)">查看</Button>
                    <Button type="error" size="small" style="margin-left: 5px" @click="deleteData(index)">删除</Button>
                </div>
            </template>

        </Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="page.total" :current="page.current" @on-change="pageChange"
                      @on-page-size-change="pageSizeChange" :page-size="page.pageSize" show-elevator show-sizer></Page>
            </div>
        </div>

        <Modal :title="modalTitle" :show.sync="dailyModalIShow" @on-cancel="editData={}" :width="1200">
            <dailyView :editData="editData" @update-success="editComplete"></dailyView>
            <div slot="footer">
                <Button type="text" size="large" @click="close">取消</Button>
            </div>
        </Modal>
        <Modal :title="modalEditTitle" :show.sync="projectModalIShow" @on-cancel="editDailData={}" :width="1200">
            <dailyEdit :editDailData="editDailData" ref="dailyEdit" @update-success="editDailyComplete"></dailyEdit>
            <div slot="footer">
                <Button type="text" size="large" @click="closeModal">取消</Button>
                <Button type="primary" size="large" @click="save(false)">保存</Button>
                <Button type="primary" size="large" @click="save(true)">发送</Button>
            </div>
        </Modal>
        <!--删除提示-->
        <Modal :title="deleteTitle" :show.sync="deleteShow" @on-cancel="cancelDelete"   @on-ok="comfirmDelete" :width="400" >
            是否删除选择的数据？
        </Modal>
    </div>
</template>
<script>
import dailyEdit from './daily-edit'
import axios from '@/libs/api.request'
import {setToken, getToken} from '@/libs/util'
import {loadavg} from 'os';
import {debuglog} from 'util';
import Modal from '@/view/components/sega/modal'
import dailyView from './daily-view'
import {mapState, mapGetters, mapMutations, mapActions} from 'vuex'
import dailyXs from './daily-xs'

export default {
    name: 'userIndex',
    components: {
        axios,
        Modal,
        dailyView,
        dailyEdit,
        dailyXs

    },
    //数据区域
    data() {
        return {
            sjrList: [],
            //删除弹窗是否显示
            deleteShow: false,
            //删除提示信息
            deleteMessage: "确认删除该数据？",
            addUserShow: false,
            modalTitle: '查看日报',
            modalEditTitle: '编辑日报',
            //删除标识
            deleteTitle: '提示',
            deleteObj:{},
            // 查询条件
            condition: {
                startDate: '',
                endDate: '',
                dayilsjrName: '',
                dayilUserName:'',
                dailyTheme:''

            },
            startTimeOptions: {},
            endTimeOptions: {},
            //日报信息列表
            daliyColumns: [{
                type: 'selection',
                width: 60,
                align: 'center'
            }, {
                title: '日报日期',
                width: 120,
                key: 'dailyDate'
            }, {
                title: '发送人',
                width: 100,
                key: 'dayilUserName'
            }, {
                title: '日报主题',
                key: 'dailyTheme',

            }, {
                title: '接收人',
                key: 'dayilsjrName'
            }, {
                title: '抄送人',
                key: 'dayilcsrName'
            }, {
                title: '状态',
                width: 80,
                key: 'dailySendTy',
                render: (h, params) => {
                    if (params.row.dailySendTy == 1) {
                        return h('div', "已发送");
                    } else {
                        return h('div', "未发送");
                    }

                }
            }, {
                title: '方式',
                key: 'dailySendBy'
            }, {
                title: '操作',
                slot: 'action',
                width: 200,
                align: 'center'
            }],
            //用户数据
            dailyData: [],
            //分页信息
            page: {
                total: 100,
                current: 1,
                pageSize: 10,
            },
            //编辑弹窗是否显示
            dailyModalIShow: false,
            projectModalIShow: false,
            //弹窗标题
            //编辑的数据
            editData: {},
            editDailData: {},
            //选中的行数据
            selectArray: [],
            subordinateTitle:'下属日报',
            subordinateShow: false,
        }
    },
    computed: {
        ...mapState({
            userInfo: (state) => state.user
        }),
    },
    //方法区域
    methods: {
        selectsjrMethod() {

        },
        editComplete() {
            this.dailyModalIShow = false;
        },
        editDailyComplete() {
            this.projectModalIShow = false;
        },
        close() {
            this.editData = {}
            this.dailyModalIShow = false;
        },
        shut(){
            this.subordinateShow = false;
        }
        ,
        //打开下级工时页面
        openXj(){
            this.subordinateShow = true;
        }
        ,
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
        show(index) {
            this.modalTitle = '日报详情';
            this.editData = this.dailyData[index];
            this.dailyModalIShow = true;

        },
        edit(index) {
            this.modalEditTitle = '重新编辑';
            this.editDailData = this.dailyData[index];
            this.projectModalIShow = true;

        },
        deleteData(index) {
            this.deleteObj = this.dailyData[index];
            if(this.deleteObj.dailySendTy == 1){
                this.$Message.error("日报已发送，不允许删除！");
                return;
            }
            this.deleteMessage = "确认删除该数据吗？";
            this.deleteShow = true;
        },
        //确认删除
        comfirmDelete() {
            var data = [];
            data.push(this.deleteObj.dailyId);
            axios.request({
                method: 'post',
                url: 'apis/daily/daily/deleteByIds',
                data: data
            }).then((response) => {
                this.$Message.success(response.data.message);
                this.deleteShow = false;
                this.loadData();
            }).catch((response) => {
                this.$Message.success("删除失败");
            })
        },
        //取消删除
        cancelDelete() {
            this.deleteShow = false;
        },
        endTimeClear: function (e) {
            this.startTimeOptions = {
                disabledDate(date) {
                    let endTime = this.condition.endDate ? new Date(this.condition.endDate).valueOf() - 1000 * 24 * 60 * 60 * 1000 : '';
                    return date && date.valueOf() > endTime;
                }

            }
        },
        startTimeChange: function (e) { //设置开始时间
            this.condition.startDate = e;
            this.endTimeOptions = {
                disabledDate: date => {
                    let startTime = this.condition.startDate ? new Date(this.condition.startDate).valueOf() : '';
                    return date && (date.valueOf() < startTime);
                }
            }
        },
        endTimeChange: function (e) { //设置结束时间
            this.condition.endDate = e;
            let endTime = this.condition.endDate ? new Date(this.condition.endDate).valueOf() - 1 * 24 * 60 * 60 * 1000 : '';
            this.startTimeOptions = {
                disabledDate(date) {
                    return date && date.valueOf() > endTime;
                }
            }
        },

        //加载数据
        loadData() {
            var params = this.condition;
            params['pageNum'] = this.page.current;
            params['pageSize'] = this.page.pageSize;
            this.condition.dayilUserId = this.userInfo.setUserInfo.userId
            axios.request({
                method: 'post',
                url: 'apis/daily/daily/queryByPage',
                data: this.condition
            }).then((response) => {
                this.dailyData = response.data.obj.list;
                this.page.current = response.data.obj.pageNum;
                this.page.pageSize = response.data.obj.pageSize;
                this.page.total = response.data.obj.total;
            }).catch((response) => {

            });
            axios.request({
                url: 'apis/daily/dailySender/queryjsrEmail',
                method: 'post',
                data: this.userInfo.setUserInfo.userName
            }).then(res => {
                this.sjrList = res.data.obj;
            })
        },
        closeModal() {
            this.editDailData = {}
            this.projectModalIShow = false;
            this.$refs.dailyEdit.handleCancel();
        },
        save(send) {
            this.$refs.dailyEdit.save(send);
            this.projectModalIShow = false;
            this.loadData();

        }
    },
    //初始化事件
    mounted() {
        this.loadData();
        getToken();
    }
}
</script>
