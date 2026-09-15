<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card>
          <div style="display:flex;justify-content:space-between;align-items:center">
            <h3>字段列表（拖拽改变顺序）</h3>
            <div>
              <el-button size="small" type="primary" @click="addField">新增字段</el-button>
              <el-button size="small" @click="resetToOrigin">恢复为原始</el-button>
            </div>
          </div>

          <draggable v-model="schema.fields" item-key="id" animation="200" class="draggable-list">
            <template #item="{element, index}">
              <el-card style="margin-bottom:8px" :body-style="{padding: '8px'}">
                <div style="display:flex;justify-content:space-between;align-items:center">
                  <div style="display:flex;align-items:center;gap:8px">
                    <i class="el-icon-menu"></i>
                    <div>
                      <div style="font-weight:600">{{ element.name || element.code }}</div>
                      <div style="font-size:12px;color:#888">{{ element.data_type }}</div>
                    </div>
                  </div>
                  <div>
                    <el-button type="text" size="mini" @click="selectField(element)">编辑</el-button>
                    <el-button type="text" size="mini" @click="removeField(index)">删除</el-button>
                  </div>
                </div>
              </el-card>
            </template>
          </draggable>

          <div style="margin-top:10px">
            <el-button type="warning" @click="showDiff">预览差异并发布</el-button>
            <el-button type="info" @click="showMigration">模拟迁移</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="14">
        <el-card>
          <h3>字段属性面板</h3>
          <FieldPropertyPanel v-if="editingField" :field="editingField" @save="saveField" @cancel="editingField=null" />
          <div v-else style="color:#888">请选择字段以编辑其属性，或点击“新增字段”。</div>

          <el-divider />
          <h4>发布与迁移配置</h4>
          <el-form label-width="120px">
            <el-form-item label="迁移映射 (JSON)">
              <el-input type="textarea" v-model="migrationMappingText" :rows="6" placeholder='{"old_code":"new_code"} - 用于模拟简单的字段重命名' />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSchema">保存草稿</el-button>
              <el-button type="success" @click="publish">发布（mock）</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <PublishDiff v-if="diffVisible" :oldSchema="originSchema" :newSchema="schema" @close="diffVisible=false" @publishConfirmed="onPublishConfirmed" />
    <MigrationSimulator v-if="migrationVisible" :oldSchema="originSchema" :newSchema="schema" :mapping="migrationMapping" @close="migrationVisible=false" />
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import draggable from 'vue-draggable-next'
import FieldPropertyPanel from './FieldPropertyPanel.vue'
import PublishDiff from './PublishDiff.vue'
import MigrationSimulator from './MigrationSimulator.vue'
import { fetchModels, saveModel, publishModel } from '../api'

export default {
  components: { draggable, FieldPropertyPanel, PublishDiff, MigrationSimulator },
  setup () {
    const originSchema = ref(null)
    const schema = reactive({ id: 'm-facility', name: '设施', code: 'facility', fields: [] })
    const editingField = ref(null)
    const diffVisible = ref(false)
    const migrationVisible = ref(false)
    const migrationMappingText = ref('{}')

    const migrationMapping = computed(() => {
      try { return JSON.parse(migrationMappingText.value || '{}') } catch (e) { return {} }
    })

    function loadOrigin() {
      // try to load from backend; fallback to sample
      fetchModels().then(resp => {
        const list = resp.data || []
        const m = list.find(x => x.code === 'facility') || list[0]
        if (m && m.schema && m.schema.fields) {
          originSchema.value = JSON.parse(JSON.stringify(m.schema))
          Object.assign(schema, { id: m.id || schema.id, name: m.name || schema.name, code: m.code || schema.code, fields: JSON.parse(JSON.stringify(m.schema.fields)) })
        } else {
          originSchema.value = { fields: [ {id:'f1',name:'设施名称',code:'name',data_type:'string'}, {id:'f2',name:'设施类型',code:'facility_type',data_type:'enum'} ] }
          schema.fields = JSON.parse(JSON.stringify(originSchema.value.fields))
        }
      }).catch(()=>{
        originSchema.value = { fields: [ {id:'f1',name:'设施名称',code:'name',data_type:'string'}, {id:'f2',name:'设施类型',code:'facility_type',data_type:'enum'} ] }
        schema.fields = JSON.parse(JSON.stringify(originSchema.value.fields))
      })
    }

    onMounted(loadOrigin)

    function addField() {
      const newField = { id: 'f' + Date.now(), name: '新字段', code: 'field_' + Date.now(), data_type: 'string', constraints: {} }
      schema.fields.push(newField)
      selectField(newField)
    }
    function selectField(field) { editingField.value = field }
    function removeField(index) { schema.fields.splice(index,1); editingField.value = null }
    function saveField(updated) {
      // fields are reactive; updated is reference
      editingField.value = null
    }
    function resetToOrigin() {
      if (originSchema.value) schema.fields = JSON.parse(JSON.stringify(originSchema.value.fields))
    }
    function showDiff() { diffVisible.value = true }
    function showMigration() { migrationVisible.value = true }
    function saveSchema() {
      const payload = { code: schema.code, name: schema.name, schema: { fields: schema.fields } }
      saveModel(payload).then(()=>{ alert('已保存（mock）') }).catch(()=>{ alert('保存失败（mock）') })
    }
    function publish() {
      publishModel(schema.id).then(()=> alert('已发布（mock）')).catch(()=> alert('发布（mock）'))
    }
    function onPublishConfirmed() {
      // If user confirmed in PublishDiff, proceed to publish
      publish()
      diffVisible.value = false
    }

    return { originSchema, schema, editingField, addField, selectField, removeField, saveField, resetToOrigin, diffVisible, showDiff, migrationVisible, showMigration, migrationMappingText, migrationMapping, saveSchema, publish, onPublishConfirmed }
  }
}
</script>

<style>
.draggable-list { max-height: 60vh; overflow:auto }
</style>
