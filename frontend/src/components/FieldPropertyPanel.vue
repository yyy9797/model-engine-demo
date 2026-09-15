<template>
  <div>
    <el-card>
      <el-form :model="local" label-width="120px">
        <el-form-item label="字段名称">
          <el-input v-model="local.name" />
        </el-form-item>
        <el-form-item label="字段编码">
          <el-input v-model="local.code" />
        </el-form-item>
        <el-form-item label="数据类型">
          <el-select v-model="local.data_type">
            <el-option label="字符串" value="string" />
            <el-option label="整数" value="integer" />
            <el-option label="数值" value="number" />
            <el-option label="布尔" value="boolean" />
            <el-option label="日期" value="date" />
            <el-option label="枚举" value="enum" />
            <el-option label="JSON" value="json" />
            <el-option label="地理" value="geo" />
          </el-select>
        </el-form-item>
        <el-form-item label="必填">
          <el-switch v-model="local.constraints.required" />
        </el-form-item>
        <el-form-item label="默认值">
          <el-input v-model="local.default" />
        </el-form-item>
        <el-form-item label="迁移旧编码">
          <el-input v-model="local.migration_old_code" placeholder="用于模拟迁移时的重命名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="apply">保存</el-button>
          <el-button @click="$emit('cancel')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { reactive, toRefs, watch } from 'vue'
export default {
  props: ['field'],
  emits: ['save','cancel'],
  setup(props, { emit }) {
    const local = reactive({})
    watch(()=> props.field, v => {
      Object.assign(local, JSON.parse(JSON.stringify(v || {})))
    }, { immediate: true })
    function apply() {
      // copy properties back to original object
      Object.keys(local).forEach(k => { props.field[k] = local[k] })
      emit('save', props.field)
    }
    return { local, apply }
  }
}
</script>
