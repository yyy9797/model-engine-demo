<template>
  <el-dialog title="模拟迁移" :visible.sync="visible" width="80%">
    <div style="display:flex;gap:16px">
      <div style="flex:1">
        <h4>原始记录（示例）</h4>
        <el-table :data="sampleRecords" style="max-height:60vh;overflow:auto">
          <el-table-column prop="id" label="ID" width="120" />
          <el-table-column label="数据">
            <template #default="{row}">
              <pre style="max-height:120px;overflow:auto">{{ JSON.stringify(row.data, null, 2) }}</pre>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="flex:1">
        <h4>迁移后预览</h4>
        <el-table :data="migrated" style="max-height:60vh;overflow:auto">
          <el-table-column prop="id" label="ID" width="120" />
          <el-table-column label="数据">
            <template #default="{row}">
              <pre style="max-height:120px;overflow:auto">{{ JSON.stringify(row.data, null, 2) }}</pre>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div style="margin-top:12px">
      <h4>问题与建议</h4>
      <el-alert v-for="(p,i) in problems" :key="i" :title="p" type="warning" show-icon />
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="$emit('close')">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { ref, watch, onMounted } from 'vue'
import { fetchRecords } from '../api'

export default {
  props: ['oldSchema','newSchema','mapping'],
  emits: ['close'],
  setup(props) {
    const visible = ref(true)
    const sampleRecords = ref([])
    const migrated = ref([])
    const problems = ref([])

    async function loadSamples() {
      try {
        // try fetching real records for the model (mock will return empty)
        const r = await fetchRecords(props.oldSchema && props.oldSchema.id ? props.oldSchema.id : 'm-facility')
        sampleRecords.value = r.data || []
      } catch (e) {
        // fallback demo records
        sampleRecords.value = [ { id: 'rec1', data: { name: '机场A', facility_type: 'airport', location: { type: 'Point', coordinates: [121.5,31.2] } } }, { id: 'rec2', data: { name: '港口B', facility_type: 'port' } } ]
      }
      runMigration()
    }

    function runMigration() {
      problems.value = []
      migrated.value = sampleRecords.value.map(r => {
        const newData = {}
        // for each new field, try to map from old using mapping or same code
        (props.newSchema.fields || []).forEach(f => {
          const oldCode = (f.migration_old_code) ? f.migration_old_code : (Object.keys(props.mapping || {}).find(k=> props.mapping[k] === f.code) || f.code)
          const val = r.data ? r.data[oldCode] : undefined
          if (val !== undefined) {
            newData[f.code] = val
            // attempt simple type conversion
            if (f.data_type === 'string' && typeof val !== 'string') newData[f.code] = String(val)
          } else {
            // if required, warn
            if (f.constraints && f.constraints.required) problems.value.push(`记录 ${r.id}: 字段 ${f.code} 未找到匹配来源，且为必填`) 
            if (f.default) newData[f.code] = f.default
          }
        })
        return { id: r.id, data: newData }
      })
    }

    watch(()=> [props.oldSchema, props.newSchema, props.mapping], () => { loadSamples() }, { immediate: true })

    onMounted(loadSamples)
    return { visible, sampleRecords, migrated, problems }
  }
}
</script>
