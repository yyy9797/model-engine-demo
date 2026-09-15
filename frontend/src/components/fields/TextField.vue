<template>
  <el-form-item :label="field.name">
    <el-input v-model="localValue" @input="emitUpdate"/>
  </el-form-item>
</template>

<script>
import { ref, watch } from 'vue'
export default {
  props: ['field','value'],
  setup(props, { emit }) {
    const localValue = ref(props.value)
    watch(()=> props.value, v => localValue.value = v)
    function emitUpdate() { emit('update', localValue.value) }
    return { localValue, emitUpdate }
  }
}
</script>
