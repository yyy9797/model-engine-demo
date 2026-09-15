<template>
  <el-card>
    <h3>审批流程演示</h3>
    <el-input v-model="modelId" placeholder="输入 model id，示例：m-facility" />
    <div style="margin-top:10px">
      <el-button type="primary" @click="submitForApproval">提交审批</el-button>
    </div>
    <el-divider />
    <div>
      <h4>审批列表（mock）</h4>
      <el-table :data="approvals">
        <el-table-column prop="model_id" label="Model"></el-table-column>
        <el-table-column prop="status" label="Status"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="mini" type="success" @click="action(row, 'approved')">通过</el-button>
            <el-button size="mini" type="danger" @click="action(row, 'rejected')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</template>

<script>
import { ref } from 'vue'
import { submitApproval } from '../api'
export default {
  setup () {
    const modelId = ref('m-facility')
    const approvals = ref([{ id:1, model_id:'m-facility', status:'pending' }])
    function submitForApproval() {
      submitApproval(modelId.value, { submitted_by: 'demo_user' }).then(()=> {
        approvals.value.push({ id: Date.now(), model_id: modelId.value, status: 'pending' })
        alert('已提交（mock）')
      }).catch(()=> { approvals.value.push({ id: Date.now(), model_id: modelId.value, status: 'pending' }) })
    }
    function action(row, status) { row.status = status }
    return { modelId, approvals, submitForApproval, action }
  }
}
</script>
