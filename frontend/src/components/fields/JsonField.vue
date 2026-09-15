<template>
  <el-form-item :label="field.name">
    <el-input type="textarea" v-model="text" :rows="6" @change="onChange" />
  </el-form-item>
</template>

<script>
import { ref, watch } from 'vue'
export default {
  props: ['field','value'],
  setup(props, { emit }) {
    const text = ref(JSON.stringify(props.value || {}, null, 2))
    watch(()=> props.value, v => text.value = JSON.stringify(v || {}, null, 2))
    function onChange() {
      try { emit('update', JSON.parse(text.value)) } catch (e) { alert('JSON 格式错误') }
    }
    return { text, onChange }
  }
}
</script>
