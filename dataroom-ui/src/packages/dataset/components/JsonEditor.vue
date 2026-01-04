<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { DatasetEntity } from '../api'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  modelValue: DatasetEntity
}>()

const emit = defineEmits<{
  'update:modelValue': [value: DatasetEntity]
}>()

const formRef = ref<FormInstance>()

const formData = reactive<DatasetEntity>({
  name: '',
  datasetType: 'json',
  parentCode: 'root',
  dataset: {
    datasetType: 'json',
    json: ''
  },
  inputList: [],
  outputList: []
})

// 监听外部传入的值变化
watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal) {
      Object.assign(formData, newVal)
      if (!formData.dataset) {
        formData.dataset = {
          datasetType: 'json',
          json: ''
        }
      }
    }
  },
  { immediate: true, deep: true }
)

// 监听内部值变化，触发更新
watch(
  formData,
  (newVal) => {
    emit('update:modelValue', newVal)
  },
  { deep: true }
)

const rules = reactive<FormRules<DatasetEntity>>({
  name: [{ required: true, message: '请输入数据集名称', trigger: 'blur' }]
})

/**
 * 表单验证
 */
const validate = () => {
  return formRef.value?.validate()
}

/**
 * 重置表单
 */
const resetFields = () => {
  formRef.value?.resetFields()
}

/**
 * 获取数据
 */
const getData = (): DatasetEntity => {
  return { ...formData }
}

/**
 * 格式化JSON
 */
const formatJson = () => {
  try {
    if (formData.dataset && 'json' in formData.dataset && formData.dataset.json) {
      const parsed = JSON.parse(formData.dataset.json)
      formData.dataset.json = JSON.stringify(parsed, null, 2)
      ElMessage.success('格式化成功')
    }
  } catch (error) {
    ElMessage.error('JSON格式错误，无法格式化')
  }
}

defineExpose({
  validate,
  resetFields,
  getData
})
</script>

<template>
  <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
    <el-form-item label="数据集名称" prop="name">
      <el-input v-model="formData.name" placeholder="请输入数据集名称" clearable />
    </el-form-item>
    <el-form-item label="JSON数据">
      <div style="width: 100%; position: relative">
        <el-input
          v-if="formData.dataset && 'json' in formData.dataset"
          v-model="formData.dataset.json"
          type="textarea"
          :rows="15"
          placeholder='请输入JSON数据，例如：[{"name": "张三", "age": 20}]'
        />
        <div style="position: absolute; right: 12px; bottom: 16px">
          <el-button size="small" @click="formatJson">格式化</el-button>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="出参配置">
      <div style="width: 100%">
        <el-button size="small" @click="formData.outputList?.push({ name: '', type: 'String' })">
          添加出参
        </el-button>
        <el-table :data="formData.outputList" border style="width: 100%; margin-top: 8px">
          <el-table-column label="参数名" width="150">
            <template #default="{ row }">
              <el-input v-model="row.name" size="small" placeholder="参数名" />
            </template>
          </el-table-column>
          <el-table-column label="类型" width="120">
            <template #default="{ row }">
              <el-select v-model="row.type" size="small" placeholder="类型">
                <el-option label="String" value="String" />
                <el-option label="Number" value="Number" />
                <el-option label="Boolean" value="Boolean" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="描述">
            <template #default="{ row }">
              <el-input v-model="row.desc" size="small" placeholder="描述" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" fixed="right">
            <template #default="{ $index }">
              <el-button
                type="danger"
                size="small"
                link
                @click="formData.outputList?.splice($index, 1)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form-item>
  </el-form>
</template>

<style scoped lang="scss">
:deep(.el-form) {
  padding: 16px;
}
</style>
