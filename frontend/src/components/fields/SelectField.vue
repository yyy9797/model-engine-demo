<template>
  <el-form-item :label="field.name">
    <el-select v-model="localValue" placeholder="请选择" @change="emitUpdate" filterable>
      <el-option v-for="opt in options" :key="opt.value" :label="opt.label" :value="opt.value" />
    </el-select>
  </el-form-item>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
export default {
  props: ['field','value','formData'],
  setup(props, { emit }) {
    const localValue = ref(props.value)
    const options = ref([])
    watch(()=> props.value, v => localValue.value = v)
    onMounted(async () => {
      if (props.field.dict_code === 'facility_type') {
        options.value = [{ value: 'airport', label: '机场' }, { value: 'port', label: '港口'}]
      } else if (props.field.dict_code === 'airport_subtype') {
        options.value = [{ value: 'civil', label: '民用机场' }, { value: 'military', label: '军用机场'}]
      }
    })
    function emitUpdate() { emit('update', localValue.value) }
    return { localValue, options, emitUpdate }
  }
}
</script>
