<template>
    <Form ref="administrationInfo"  :model="administrationInfo" :rules="ruleValidate" :lable-width="80">
        <Row>

            <i-col span="12">
                <FormItem label="资产名称" prop="faName">
                    <i-input v-model="administrationInfo.faName" placeholder="请输入资产名称"></i-input>
                </FormItem>
            </i-col>
            <i-col span="12">
                <FormItem label="资产编号" prop="faCode">
                    <i-input v-model="administrationInfo.faCode" placeholder="请输入资产编号"></i-input>
                </FormItem>
            </i-col>
        </Row>

         <Row>
            <i-col span="12">
                <FormItem label="责任人" prop="faResponsibleId">
                    <Select v-model="administrationInfo.faResponsibleId" @on-open-change="selectUser">
                      <Option v-for="item in userList" :value="item.userId" :key="item.userId">{{ item.userName}}</Option>
                    </Select>
                </FormItem>
            </i-col>
            <i-col span="12">
                <FormItem label="价格" prop="faPrice">
                    <i-input v-model="administrationInfo.faPrice" placeholder="请输入资产价格"></i-input>
                </FormItem>
            </i-col>
        </Row>
         <Row>
            <i-col span="12">
                <FormItem label="资产状态" prop="faState">
                    <Select v-model="administrationInfo.faState">
                      <Option v-for="item in administrationStateArray" :value="item.id" :key="item.id">{{item.text}}</Option>
                    </Select>
                </FormItem>
            </i-col>
        </Row>
        <Row>
          <FormItem label="备注" prop="faRemark">
            <i-input v-model="administrationInfo.faRemark" placeholder="请输入备注"></i-input>
          </FormItem>
        </Row>
    </Form>
</template>
<script>
    import axios from '@/libs/api.request'
    import TreeSelect from '_c/tree-select'
    import { newTreeData } from '@/mock/data/tree-select'
    import { getTreeSelectData } from '@/api/data'
    import { debuglog } from 'util';
export default {
    name:'administrationEdit',
    props:['editData'],
    components:{
        TreeSelect
    },
    //数据区
    data () {
        return{
            userList: [],//负责人
            administrationStateArray:[{//资产状态
                id:0,
                text:'在用'
            },{
                id:1,
                text:'丢失'
            },{
                id:2,
                text:'停用'
            },{
                id:3,
                text:'维修'
            },{
                id:4,
                text:'外借'
            }],
            administrationInfo: {
                faId: '',
                faName: '',
                faCode: '',
                faResponsibleId: '',
                faPrice: '',
                faRemark: '',
                faState: ''
            },
            ruleValidate: {//定义规则
                faName: [
                    { required: true, message: '项目名称不能为空', trigger: 'blur' }
                ],
                faCode: [
                    { required: true, message: '项目编号不能为空', trigger: 'blur' }
                ],
                faResponsibleId: [
                    { required: true, message: '负责人不能为空',type: 'number', trigger: 'blur' }
                ],
                faPrice: [
                    { required: true, message: '价格不能为空', trigger: 'blur' }
                ],
                faState: [
                    { required: true, message: '资产状态不能为空', type: 'number',trigger: 'blur' }
                ]
            }
        }
    },
    //监听事件
    watch: {
        // 监听数据
        editData (data) {
            this.administrationInfo = data;
        }
    },
    methods:{
        //确认按钮
        handleOk () {

            this.$refs['administrationInfo'].validate((valid)=>{
                console.log("1234234323")
                if(valid){
                    axios.request({
                        method:'post',
                        url:'apis/fixedAssets/saveOrUpdate',
                        data:this.administrationInfo
                    }).then((response)=>{
                        this.$emit('update-success')
                        this.$Message.success(response.data.message)
                    }).catch(()=>{
                        this.$Message.error('failMessage');
                    })
                }
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
        selectUser(){
            axios.request({
                method:'post',
                url:'apis/user/queryAll',
                data:{

                }
            }).then((response)=>{
                this.userList = response.data.obj
            })
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
        }
    },
    // 初始化方法
    mounted () {
       this.loadData();
        axios.request({
            method: 'post',
            url: 'apis/department/queryTreeData'
        }).then((response) => {
            this.treeData = response.data.obj;
        }).catch((response) => {

        })
       this.selectUser()

    }

}
</script>
