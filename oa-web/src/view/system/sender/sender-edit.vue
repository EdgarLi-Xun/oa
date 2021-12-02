<template>
    <i-form  ref="sendInfo" :model="sendInfo"  :rules="ruleValidate" :label-width="80">
        <Row>
            <i-col span="12">
                 <Form-item label="发送人" prop="userName">
                    <i-input v-model="sendInfo.dsUserName" placeholder="请输入姓名"></i-input>
                </Form-item>
            </i-col>
            <i-col span="12">
                <Form-item label="接收人" prop="dsRecevicer">
                    <Select v-model="sendInfo.dsRecevicer"  @on-open-change="selectuserMethod" filterable>
                        <Option v-for="item in userList" :value="item.userId" :key="item.userId" >{{ item.userName }}</Option>
                    </Select>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                 <Form-item label="接收类型" prop="userRoleId">
                    <Select v-model="sendInfo.userRoleId" >
                        <Option v-for="item in userRoleArray" :value="item.id"
                                :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                </Form-item>
            </i-col>

            <i-col span="12">
                <Form-item label="接收人邮箱" prop="dsSendEmail">
                    <i-input v-model="sendInfo.dsSendEmail" placeholder="请输入接收人邮箱"></i-input>
                </Form-item>
            </i-col>
        </Row>
    </i-form>
</template>
<script>
import axios from '@/libs/api.request'
import TreeSelect from '_c/tree-select'
import { newTreeData } from '@/mock/data/tree-select'
import { getTreeSelectData } from '@/api/data'
import { debuglog } from 'util';
export default {
    name: 'senderEdit',
    props: [
        'editData'
    ],
    components: {
        TreeSelect
    },
    data () {
        return {
            animal:'',
            treeSelected: [112, 113],
            treeData: [],
            userList: [],
            userRoleArray:  [{
            id: 0,
            text: '发送'
        },{
            id: 1,
            text: '抄送'
        }],
            userStateArray: [],
            sendInfo: {
                dsUserId: '',
                dsRecevicer: '',
                dsType: '',
                dsUserName:'',
                dsSendEmail:''

            },
            ruleValidate: {

            }
        }
    },
    watch: {
        // 监听数据
        editData (data) {
            this.sendInfo = data;
        }
    },
    // 方法
    methods: {
        // 确认按钮
        handleOk () {
            var flag = true
            this.$refs['sendInfo'].validate((valid) => {
                if (!valid) {
                    flag = false
                }
            })
            if (!flag) {
                return
            }
            axios.request({
                method: 'post',
                url: 'apis/daily/dailySender/saveOrUpdate',
                data: this.sendInfo
            }).then((response) => {
                this.$emit('update-success')
                this.$Message.success(response.data.message)
            }).catch(() => {
                this.$Message.error("failMessage")
            })
        },
        changeTreeSelectData () {
            this.treeSelected = [111, 114]
        },
        changeTreeData () {
            this.treeData = newTreeData
            // this.treeSelected = [];
        },
        handleTreeSelectChange (list) {
            // console.log('=-========', list);
        },
        handleTreeSelectExpand (item) {
            // console.log('toggle expand', item);
        },
        handleTreeSelectCheckChange (selectedArray, item) {
            // console.log(selectedArray, item);
        },
        handleTreeSelectClick (selectArray, item) {
            // console.log(selectArray, item);
        },
        loadData (item, callback) {
            let data = [{
                    id: 111,
                    title: '1-1-1'
                },
                {
                    id: 112,
                    title: '1-1-2'
                },
                {
                    id: 113,
                    title: '1-1-3'
                },
                {
                    id: 114,
                    title: '1-1-4'
                }]
        },
        selectuserMethod () {
            axios.request({
                url: 'apis/user/queryAll',
                method: 'post',
                data: {
                }
            }).then(res => {
                this.userList = res.data.obj;
            })
        },
    },
    // 初始化方法
    mounted () {
       this.loadData();
       this.selectuserMethod();
        axios.request({
            method: 'post',
            url: 'apis/department/queryTreeData'
        }).then((response) => {
            this.treeData = response.data.obj;
        }).catch((response) => {

        })
    }
}
</script>
