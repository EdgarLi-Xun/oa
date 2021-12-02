<template>
    <div style="padding:32px 64px">
        <h1>可编辑行</h1> <Button
        type="primary"
        @click="addRow"
    >添加一行</Button>
        <Table
            :data="data"
            :columns="tbColumns"
            border
        ></Table>
        <div class="main-page">
            <Page
                :total="totals"
                :page-size="pageSize"
                @on-change="change"
                show-elevator
                :current.sync="current"
                :page-size-opts="page_size_array"
                show-sizer
                show-total
                @on-page-size-change="handleChangeSize"
            ></Page>
        </div>
        {{data}}
    </div>
</template>
<script>
export default {
    data () {
        return {
            editTableIndex: -1,
            editName: '',
            editCode: '',
            editRemark: '',
            tbColumns:
                [
                    {
                        title: '编号',
                        width: 80,
                        align: 'center',
                        key: 'id'
                    },
                    {
                        title: '产品名称',
                        key: 'name',
                        render: (h, { row, index }) => {
                            return this.renderTableColumn('editName', 'name', h, { row, index })
                        }
                    },
                    {
                        title: '产品代码',
                        key: 'code',
                        render: (h, { row, index }) => {
                            return this.renderTableColumn('editCode', 'code', h, { row, index })
                        }
                    },
                    {
                        title: '产品描述',
                        key: 'remark',
                        render: (h, { row, index }) => {
                            return this.renderTableColumn('editRemark', 'remark', h, { row, index })
                        }
                    },
                    {
                        title: '操作',
                        render: (h, { row, index }) => {
                            if (this.editTableIndex === index) {
                                return [
                                    h('Button', {
                                        props: {
                                            type: 'success'
                                        },
                                        on: {
                                            click: () => {
                                                this.data[index].name = this.editName
                                                this.data[index].code = this.editCode
                                                this.data[index].remark = this.editRemark
                                                this.editTableIndex = -1
                                                // 可在此处配置异步提交（缺省）
                                            }
                                        }
                                    }, '保存'),
                                    h('Button', {
                                        props: {
                                            type: 'error'
                                        },
                                        style: {
                                            marginLeft: '6px'
                                        },
                                        on: {
                                            click: () => {
                                                this.editTableIndex = -1
                                            }
                                        }
                                    }, '取消')
                                ]
                            } else {
                                return [h('Button', {
                                    props: {
                                        type: 'info'
                                    },
                                    on: {
                                        click: () => {
                                            // this.editName = row.name
                                            // this.editCode = row.code
                                            // this.editRemark = row.remark
                                            this.editTableIndex = index
                                        }
                                    }
                                }, '修改'),
                                    h('Button', {
                                        props: {
                                            type: 'warning'
                                        },
                                        on: {
                                            click: () => {
                                                this.$Modal.confirm({
                                                    title: '提示',
                                                    content: '确认要删除该行吗？',
                                                    onOk: () => {
                                                        this.data.splice(index, 1)
                                                    },
                                                    onCancel: () => {
                                                        this.$Message.info('您已取消删除.')
                                                    }
                                                })
                                            }
                                        }
                                    }, '删除'), h('Button', {
                                        props: {
                                            type: 'primary'
                                        },
                                        on: {
                                            click: () => {
                                                this.data.push({ id: '5', name: '', code: '', remark: '' })
                                                this.editTableIndex = index + 1// 触发新增行的行编辑
                                            }
                                        }
                                    }, ' + ')]
                            }
                        }
                    }
                ],
            data: [],
            loading: false,
            current: 1,
            page_size_array: [10, 20, 30, 40, 60, 100],
            totals: 0, // 数据行数
            pageSize: 10, // 每页显示条数
            pageIndex: 1// 当前页
        }
    },
    methods: {
        getList () {
            if (this.loading) return
            this.loading = true
            // 以下方法应根据实际应用场景来写
            let filter = {}
            this.$axios
                .post('/api/v1/product/list', { filter: filter, sort: { id: 'ASC', name: 'DESC' }, page: this.pageIndex, limit: this.pageSize })
                .then(response => {
                    console.log(response)
                    this.data = response.data.data.docs
                    this.totals = response.data.data.totals
                    // console.log(this.totals)
                    this.loading = false
                })
                .catch(function (error) { // 请求失败处理
                    console.log('请求失败：' + error)
                })
        },
        // 分页触发
        change (page) {
            // console.log(page)
            this.pageIndex = page
            this.getList()
        },
        // 调整页面大小后加载
        handleChangeSize (val) {
            this.pageSize = val
            this.$nextTick(() => {
                this.getList()
            })
        },
        // 对列的render处理：编辑和未编辑
        renderTableColumn (editName, columnName, h, { row, index }) {
            let edit
            if (this.editTableIndex === index) {
                this[editName] = row[columnName]
                edit = [h('Input', {
                    props: {
                        value: row[columnName]
                    },
                    on: {
                        input: (val) => {
                            this[editName] = val
                        }
                    }
                })]
            } else {
                edit = row[columnName]
            }
            return h('div', [edit])
        },
        // 添加一行
        addRow () {
            this.data.push({ id: '5', name: '', code: '', remark: '' })
            this.editTableIndex = this.data.length + 1// 触发新增行的行编辑
        }
    },
    mounted () {
        this.getList()
    }
}
</script>
