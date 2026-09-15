<template>
  <div>
    <el-card>
      <h3>字典管理（示例）</h3>
      <el-table :data="dicts" style="width:100%">
        <el-table-column prop="code" label="Code"></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column label="Values" :formatter="row => row.values?.map(v=>v.label).join(', ')"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { fetchDictionaries } from '../api'
export default {
  setup () {
    const dicts = ref([])
    onMounted(async () => {
      try {
        const r = await fetchDictionaries()
        dicts.value = r.data
      } catch (e) {
        dicts.value = [
          { code: 'facility_type', name: '设施类型', values: [{label:'机场',value:'airport'},{label:'港口',value:'port'}] },
          { code: 'airport_subtype', name: '机场子类', values: [{label:'民用机场',value:'civil'},{label:'军用机场',value:'military'}] }
        ]
      }
    })
    return { dicts }
  }
}
</script>
