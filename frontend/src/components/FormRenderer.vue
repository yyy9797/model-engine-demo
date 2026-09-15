<template>
  <div>
    <el-card>
      <h3>选择模型</h3>
      <el-select v-model="modelId" placeholder="选择模型" @change="loadModel">
        <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.id" />
      </el-select>
    </el-card>
    <el-card v-if="schema">
      <h3>表单：{{ schema.name }}</h3>
      <el-form :model="formData" label-width="120px">
        <component v-for="f in schema.fields" :is="fieldComponent(f)" :key="f.id" :field="f" :value="formData[f.code]" @update="val => updateField(f.code, val)" :formData="formData" />
      </el-form>
      <div style="margin-top:10px">
        <el-button type="primary" @click="submit">提交</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import TextField from './fields/TextField.vue'
import SelectField from './fields/SelectField.vue'
import MapField from './MapPicker.vue'
import JsonField from './fields/JsonField.vue'
import { createRecord } from '../api'
export default {
  components: { TextField, SelectField, MapField, JsonField },
  setup () {
    const models = ref([{ id: 'm-facility', name: '设施' }])
    const modelId = ref('m-facility')
    const schema = ref(null)
    const formData = reactive({})
    const sampleSchema = {
      id: 'm-facility', name: '设施',
      fields: [
        { id: 'f1', name: '设施名称', code: 'name', data_type: 'string', ui:{widget:'input'} },
        { id: 'f2', name: '设施类型', code: 'facility_type', data_type: 'enum', dict_code: 'facility_type', ui:{widget:'select'} },
        { id: 'f3', name: '机场子类', code: 'airport_sub', data_type: 'enum', dict_code: 'airport_subtype',
          dependencies:{ visible_when: "formData.facility_type === 'airport'", filter_by:{ source_field:'facility_type', relation:'facility->airport_sub' } }, ui:{widget:'select'} },
        { id: 'f4', name: '位置', code: 'location', data_type: 'geo', ui:{widget:'map'} },
        { id: 'f5', name: '扩展', code: 'extra', data_type: 'json', ui:{widget:'json'} }
      ]
    }
    function loadModel() { schema.value = sampleSchema; schema.value.fields.forEach(f => { formData[f.code] = null }) }
    loadModel()
    function fieldComponent(f) {
      if (f.ui && f.ui.widget === 'input') return 'TextField'
      if (f.ui && f.ui.widget === 'select') return 'SelectField'
      if (f.ui && f.ui.widget === 'map') return 'MapField'
      if (f.ui && f.ui.widget === 'json') return 'JsonField'
      return 'TextField'
    }
    function updateField(code, value) { formData[code] = value }
    async function submit() { const rec = JSON.parse(JSON.stringify(formData)); await createRecord(schema.value.id, rec); alert('已提交（mock）') }
    return { models, modelId, schema, formData, loadModel, fieldComponent, updateField, submit }
  }
}
</script>
