<style>
    .sega-modal .ivu-modal-body > div:first-child,.sega-modal .ivu-modal-body > form:first-child {
        padding-right: 20px;
    }
</style>
<template>
    <Modal class="sega-modal" :loading="loading" :mask-closable="maskClosable" :closable="closable"  :title="title" :width="width" v-model="showed" @on-visible-change="visiableChanged" @on-ok="handleOk" @on-cancel="handleCancel">
        <slot></slot>
        <slot name="footer" slot="footer" v-if="processBtns">
            <Button type="text" @click="handleCancel">{{ $t('cancel') }}</Button>
            <Button type="info" @click="handleSaveSubmit">{{ $t('saveAndSubmit') }}</Button>
            <Button type="primary" @click="handleOk">{{ $t('save') }}</Button>
        </slot>
        <slot name="footer" slot="footer" v-else></slot>
    </Modal>
</template>

<script>
export default {
    props: {
        processBtns: {
            type: Boolean,
            default: false
        },
        show: {
            type: Boolean,
            default: false
        },
        title: {
            type: String
        },
        width: {
            type: Number
        },
        maskClosable: {
            type: Boolean,
            default: false
        },
        closable: {
            type: Boolean,
            default: true
        },
        autoScroll: {
            type: Boolean,
            default: true
        }
    },
    data () {
        return {
            loading: true,
            showed: false
        }
    },
    watch: {
        loading (v) {
            if (!v) {
                setTimeout(() => {
                    this.loading = true
                }, 20)
            }
        },
        show (v) {
            this.showed = v
            if (this.autoScroll) {
                this.$nextTick(() => {
                    // 解决滚动条保留上次位置的问题
                    // 2018年11月9日 修复多个弹出框重置滚动的问题， document -> this.$el
                    let overflowDom = this.$el.querySelectorAll('.sega-modal *')
                    if (overflowDom) {
                        overflowDom.forEach((item) => {
                            if (item.scrollTop !== 0) {
                                item.scrollTop = 0
                            }
                        })
                    }
                })
            }
        },
        showed (v) {
            if (!v) {
                this.$emit('update:show', v)
            }
        }
    },
    methods: {
        visiableChanged (v) {
            if (!v) {
                this.$emit('on-cancel')              
                this.modalVisiableChanged()
            }
        },
        resetLoading () {
            setTimeout(() => {
                this.loading = false
            }, 500)
        },
        getSlotDefault () {
            let slotDefault = {}
            this.$slots.default.forEach(element => {
                if (element.componentInstance) {
                    slotDefault = element.componentInstance
                }
            })
            return slotDefault
        },
        handleOk () { 
            let slotDefault = this.getSlotDefault()
            if (slotDefault.handleOk) {
                if (slotDefault.handleOk.apply(slotDefault)) {
                    this.showed = false
                }
            }
            this.loading = true
            setTimeout(() => {
                    this.loading = false;
                }, 1500);
             this.$emit('on-ok')
        },
        handleSaveSubmit () {
            let slotDefault = this.getSlotDefault()
            if (slotDefault.handleSaveSubmit) {
                if (slotDefault.handleSaveSubmit.apply(slotDefault)) {
                    this.showed = false
                }
            }
            this.loading = false
        },
        handleCancel () {
            let slotDefault = this.getSlotDefault()
            if (slotDefault.handleCancel) {
                slotDefault.handleCancel.apply(slotDefault)
            }
            this.showed = false
        },
        modalVisiableChanged () {
            let slotDefault = this.getSlotDefault()
            if (slotDefault.visiableChanged) {
                slotDefault.visiableChanged.apply(slotDefault)
            }
        }
    }
}
</script>

<style>
</style>
