<template>
    <div id="printTest">
    <i-form  ref="projectInfo" :model="projectInfo"  :rules="ruleValidate" :label-width="80">
        <Row>
            <i-col span="12">
                <Form-item label="申请人" prop="sqname">
                    <i-input disabled="disabled" v-model="projectInfo.sqname"></i-input>
                </Form-item>

            </i-col>
            <i-col span="12">
                <Form-item label="申请部门" prop="sqbm">
                    <i-input disabled="disabled" v-model="projectInfo.sqbm"></i-input>
                </Form-item>
            </i-col>
        </Row>
        <Row>
            <i-col span="12">
                <Form-item label="申请事由" prop="sqsy">
                    <i-input disabled="disabled" v-model="projectInfo.sqsy"></i-input>
                </Form-item>

            </i-col>
            <i-col span="12">
                <Form-item label="期望交付日期" prop="sqbm">
                    <i-input disabled="disabled" v-model="projectInfo.jfdate"></i-input>
                </Form-item>
            </i-col>
        </Row>

    </i-form>


        <div>
            <i-button type="primary" style="margin-left: 2px">采购明细</i-button>
            <i-button  v-print="'#printTest'"  type="primary" style="margin-left: 10px" >打印</i-button>
        </div>
        <Table border stripe :columns="cglistColumns"  highlight-row :data="cglist" @on-selection-change="selectData"></Table>



    </div>


</template>
<script>
    import axios from '@/libs/api.request'
    import TreeSelect from '_c/tree-select'
    import { newTreeData } from '@/mock/data/tree-select'
    import { getTreeSelectData } from '@/api/data'
    import { debuglog } from 'util';
    export default {
        name: 'wechatPrint',
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
                cglist: [],
                projectStateArray: [{
                    id: 0,
                    text: '未启动'
                },{
                    id: 1,
                    text: '进行中'
                },{
                    id: 2,
                    text: '已结束'
                }],
                projectInfo: {
                    sqname: '',
                    sqbm: '',
                    sqsy: '',
                    jfdate: ''
                },
                cglistColumns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },{
                    title: '序号',
                    key: 'xh',
                    render: (h, params) => {
                        return h("span", params.index+ 1);
                    },
                },{
                    title: '物品名称',
                    key: '物品名称',


                },{
                    title: '型号或规格',
                    key: '型号或规格',
                },{
                    title: '数量',
                    key: '数量',
                },{
                    title: '金额',
                    key: '金额',
                },{
                    title: '备注',
                    key: '备注',
                }
                ],

            }
        },
        watch: {
            // 监听数据
            editData (data) {

                this.projectInfo = data;
                let cgdata = {};
                this.editData.mxlist.forEach(item => {

                    item.forEach(newitem => {
                        let key = newitem.title;
                        let value = newitem.value;
                        cgdata[key] = value;

                    })
                    this.cglist.push(cgdata);
                    cgdata={};

                })

            }
        },
        // 方法
        methods: {
            // 确认按钮
            handleOk () {
                var flag = true

                this.$emit('update-success')


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
            loadData () {

                let cgdata = {};
                this.editData.mxlist.forEach(item => {

                    item.forEach(newitem => {
                        let key = newitem.title;
                        let value = newitem.value;
                        cgdata[key] = value;

                    })
                    this.cglist.push(cgdata);

                })

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
            this.selectuserMethod()

        }
    }
</script>
