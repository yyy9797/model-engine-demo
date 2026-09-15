<template>
  <el-dialog title="模型差异预览" :visible.sync="visible" width="70%">
    <div style="display:flex;gap:16px">
      <div style="flex:1;overflow:auto;max-height:60vh" v-html="leftHtml"></div>
      <div style="flex:1;overflow:auto;max-height:60vh" v-html="rightHtml"></div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="confirm">确认并发布</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { ref, watch } from 'vue'
import jsondiffpatch from 'jsondiffpatch'
import 'jsondiffpatch/public/formatters-styles/html.css'

export default {
  props: ['oldSchema','newSchema'],
  emits: ['close','publishConfirmed'],
  setup(props, { emit }) {
    const visible = ref(true)
    const leftHtml = ref('')
    const rightHtml = ref('')
    const jdp = jsondiffpatch.create({
      objectHash: function(obj){ return obj.id || obj.code || JSON.stringify(obj) }
    })

    watch(()=> [props.oldSchema, props.newSchema], () => {
      try {
        const delta = jdp.diff(props.oldSchema || {}, props.newSchema || {}) || {}
        leftHtml.value = jdp.formatters.html.format(props.oldSchema || {})
        rightHtml.value = jdp.formatters.html.format(props.newSchema || {})
        // we could also render delta
      } catch (e) {
        leftHtml.value = '<pre>' + JSON.stringify(props.oldSchema, null, 2) + '</pre>'
        rightHtml.value = '<pre>' + JSON.stringify(props.newSchema, null, 2) + '</pre>'
      }
    }, { immediate: true })

    function confirm() { emit('publishConfirmed') }
    return { visible, leftHtml, rightHtml, confirm }
  }
}
</script>
