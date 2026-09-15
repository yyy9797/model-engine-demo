<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card>
          <h3>模型编辑（JSON 快速编辑）</h3>
          <el-input type="textarea" :rows="20" v-model="schemaText" />
          <div style="margin-top:10px">
            <el-button type="primary" @click="save">保存草稿</el-button>
            <el-button type="success" @click="publish">发布</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card>
          <h3>实时预览</h3>
          <FormPreview :schema="schemaObj" @createRecord="onCreateRecord"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref } from 'vue'
import FormPreview from './FormRenderer.vue'
import { saveModel, publishModel } from '../api'
const defaultSchema = {
  id: 'm-facility',
  name: '设施',
  code: 'facility',
  fields: [
    { id: 'f1', name: '设施名称', code: 'name', data_type: 'string', constraints: { required: true }, ui: { widget: 'input' } },
    { id: 'f2', name: '设施类型', code: 'facility_type', data_type: 'enum', dict_code: 'facility_type', ui: { widget: 'select' } },
    { id: 'f3', name: '机场子类', code: 'airport_sub', data_type: 'enum', dict_code: 'airport_subtype',
      dependencies: { visible_when: "data.facility_type === 'airport'", filter_by: { source_field: 'facility_type', relation: 'facility->airport_sub' } },
      ui: { widget: 'select' } },
    { id: 'f4', name: '位置', code: 'location', data_type: 'geo', ui: { widget: 'map' } },
    { id: 'f5', name: '扩展', code: 'extra', data_type: 'json', ui: { widget: 'json' } }
  ],
  layout: [{ title: '基本信息', fields: ['name','facility_type','airport_sub','location'] }]
}
export default {
  components: { FormPreview },
  setup () {
    const schemaObj = ref(defaultSchema)
    const schemaText = ref(JSON.stringify(schemaObj.value, null, 2))
    function save () {
      try {
        const parsed = JSON.parse(schemaText.value)
        saveModel(parsed).then(()=> { alert('已保存（mock）') })
      } catch (e) { alert('JSON 格式错误：' + e.message) }
    }
    function publish () { publishModel(schemaObj.value.id).then(()=> alert('已发布（mock）')).catch(()=> alert('mock 未实现 publish 接口')) }
    function onCreateRecord(data) { console.log('create record', data) }
    return { schemaObj, schemaText, save, publish, onCreateRecord }
  }
}
</script>
