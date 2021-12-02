<template>
    <i-form  ref="modelInfo" :model="modelInfo"  :rules="ruleValidate" :label-width="80">
        <FormItem label="模块名称" prop="modelName">
            <i-input v-model="modelInfo.modelName"></i-input>
        </FormItem>
        <FormItem label="模块编号" prop="modelCode">
            <i-input v-model="modelInfo.modelCode"></i-input>
        </FormItem>
        <FormItem label="上级菜单" >
            <i-input disabled v-model="modelInfo.modleParentName"></i-input>
        </FormItem>
<!--        <FormItem label="模块链接">-->
<!--            <i-input disabled v-model="modelInfo.modelUrl"></i-input>-->
<!--        </FormItem>-->
        <FormItem label="模块描述">
            <i-input v-model="modelInfo.modelDescribe" type="textarea" :autosize="{minRows: 2,maxRows:200}" placeholder="请输入..." ></i-input>
        </FormItem>
    </i-form>
</template>
<script>
    import axios from '@/libs/api.request'
    import { debuglog } from 'util';
    export default {
        name: 'modelEdit',
        props: [
            'editData'
        ],
        components: {
            axios
        },
        data () {
            return {
                modelInfo: {

                },
                ruleValidate: {
                    modelName: [
                        {required: true, message: '模块名称不能为空', trigger: 'blur'}
                    ],
                    modelCode: [
                        { required: true, message: '模块编号不能为空', trigger: 'blur' }
                    ]
                }
            }
        },
        watch: {
            // 监听数据
            editData (data) {
                this.modelInfo = data;
                if(data.modelParentId){
                    this.getparentData(data.modelParentId);
                }
            }
        },
        // 方法
        methods: {
            // 确认按钮
            handleOk () {
                var flag = true
                this.$refs['modelInfo'].validate((valid) => {
                    if (!valid) {
                        flag = false
                    }
                })
                if (!flag) {
                    return
                }
                axios.request({
                    method: 'post',
                    url: '/apis/model/saveOrUpdate',
                    data: this.modelInfo
                }).then((response) => {
                    if (response.data.success) {
                        this.$emit('update-success')
                        this.$Message.success(response.data.message)
                    } else {
                        this.$Message.error(response.data.message);
                    }
                }).catch(() => {
                    this.$Message.error("failMessage")
                })
            },
            //获取父节点ID
            getparentData(parentId){
                let that = this
                axios.request({
                    method: 'get',
                    url: 'apis/model/get/' + parentId,
                }).then((response) => {
                    that.modelInfo["modleParentName"] = response.data.obj.modelName;
                    that.$forceUpdate();
                }).catch((response) => {

                })

            },
            handleCancel() {
                this.$refs['modelInfo'].resetFields()
            }
        },
        // 初始化方法
        mounted () {

        }
    }
</script>
