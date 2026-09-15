import { createI18n } from 'vue-i18n'
const messages = {
  en: {
    modelEditor: 'Model Editor',
    formRenderer: 'Form Renderer',
    dictionaries: 'Dictionaries',
    approvals: 'Approvals',
    save: 'Save',
    publish: 'Publish'
  },
  zh: {
    modelEditor: '模型编辑器',
    formRenderer: '表单渲染',
    dictionaries: '字典管理',
    approvals: '审批流程',
    save: '保存',
    publish: '发布'
  }
}
export const i18n = createI18n({ legacy: false, locale: 'zh', fallbackLocale: 'en', messages })
