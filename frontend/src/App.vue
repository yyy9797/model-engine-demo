<template>
  <el-container style="height:100vh">
    <el-header>
      <el-menu :default-active="tab" mode="horizontal" @select="tab = $event">
        <el-menu-item index="editor">{{ $t('modelEditor') }}</el-menu-item>
        <el-menu-item index="renderer">{{ $t('formRenderer') }}</el-menu-item>
        <el-menu-item index="dict">{{ $t('dictionaries') }}</el-menu-item>
        <el-menu-item index="approval">{{ $t('approvals') }}</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <component :is="currentTabComponent" />
    </el-main>
  </el-container>
</template>

<script>
import { ref, computed } from 'vue'
import ModelEditor from './components/ModelEditor.vue'
import FormRenderer from './components/FormRenderer.vue'
import DictionaryManager from './components/DictionaryManager.vue'
import ApprovalWorkflow from './components/ApprovalWorkflow.vue'
export default {
  setup () {
    const tab = ref('editor')
    const currentTabComponent = computed(() => {
      if (tab.value === 'editor') return ModelEditor
      if (tab.value === 'renderer') return FormRenderer
      if (tab.value === 'dict') return DictionaryManager
      return ApprovalWorkflow
    })
    return { tab, currentTabComponent }
  }
}
</script>
