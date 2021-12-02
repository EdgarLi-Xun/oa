<style scoped>
    .expand-row{
        margin-bottom: 16px;
    }
</style>
<template>
    <div>
        <Table border :columns="detailInfoColumns" :data="detailInfoData">
            <template slot-scope="{ row, index }" slot="action">
                <Button type="primary" size="small" style="margin-right: 5px"  @click="printModal(index)">打印</Button>
            </template>
        </Table>
        <Modal :title="modaluserTiltle" :show.sync="projectModalIShow" @on-cancel="editData={}"  :width="900" ok-text="">
            <wechatPrint :editData="editData"  @update-success="edituserComplete"  ></wechatPrint>
        </Modal>
    </div>


</template>
<script>
    import { getFormDate } from '@/libs/tools.js'
    import wechatPrint from './wechat-print'
    import axios from '@/libs/api.request'
    import Modal from '@/view/components/sega/modal'
    export default {
        props: {
            row: Object
        },
        components: {
            wechatPrint,
            axios,
            Modal
        },
        data () {
            return {
                //审批流程信息
                detailInfoColumns: [],
                detailInfoData:[],
                cglist:[],
                projectModalIShow:false,
                cgcountlist:[]
        }
        },
        methods: {

            //加载数据
            loadData(){
                let params =this._props.row;
                //获取审批信息的详细信息，并遍历动态生成column和数据
                let detailInfo = params.comm.apply_data;
                let data = JSON.parse(detailInfo);
                let detaildata = {};
                data.forEach(item => {
                    this.detailInfoColumns.push({'title':item.title,'key':item.title});
                    let key = item.title;
                    let value = item.value;
                    if (key.indexOf('时间') > -1) {
                        value = getFormDate(value,'y-m-d h:i:s');
                    }
                    detaildata[key] = value;

                })
                this.detailInfoData.push(detaildata);
                if(params.spname=='释加采购'){
                    this.detailInfoColumns.push({'title':'操作','slot':'action','align':'center'})
                }

            },
            printModal(index){
                this.modaluserTiltle = '打印采购申请';
                this.editData = this.detailInfoData[index];
                this.editData.sqname=this.editData.申请人[0].name;
                this.editData.sqbm=this.editData.申请部门[0].name;
                this.editData.sqsy=this.editData.申请事由;
                this.editData.jfdate=this.editData.期望交付日期;
                this.editData.mxlist=this.editData.采购明细;





                this.projectModalIShow = true;
            },
            edituserComplete() {
                this.projectModalIShow = false;

            },
        },
        mounted () {
            this.loadData();
        }
    };

</script>
