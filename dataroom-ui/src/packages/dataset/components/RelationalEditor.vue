<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { DatasetEntity } from '../api'

const props = defineProps<{
  modelValue: DatasetEntity
  dataSourceList?: any[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: DatasetEntity]
}>()

const formRef = ref<FormInstance>()

const formData = reactive<DatasetEntity>({
  name: '',
  datasetType: 'relational',
  parentCode: 'root',
  dataSourceCode: '',
  dataset: {
    datasetType: 'relational',
    sql: ''
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
          datasetType: 'relational',
          sql: ''
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
  name: [{ required: true, message: '请输入数据集名称', trigger: 'blur' }],
  dataSourceCode: [{ required: true, message: '请选择数据源', trigger: 'change' }]
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
    <el-form-item label="数据源" prop="dataSourceCode">
      <el-select v-model="formData.dataSourceCode" placeholder="请选择数据源" clearable>
        <el-option
          v-for="item in dataSourceList"
          :key="item.code"
          :label="item.name"
          :value="item.code"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="SQL语句">
      <el-input
        v-if="formData.dataset && 'sql' in formData.dataset"
        v-model="formData.dataset.sql"
        type="textarea"
        :rows="10"
        placeholder="请输入SQL语句"
      />
    </el-form-item>
    <el-form-item label="入参配置">
      <div style="width: 100%">
        <el-button size="small" @click="formData.inputList?.push({ name: '', type: 'String', required: false })">
          添加入参
        </el-button>
        <el-table :data="formData.inputList" border style="width: 100%; margin-top: 8px">
          <el-table-column label="参数名" width="120">
            <template #default="{ row }">
              <el-input v-model="row.name" size="small" placeholder="参数名" />
            </template>
          </el-table-column>
          <el-table-column label="类型" width="100">
            <template #default="{ row }">
              <el-select v-model="row.type" size="small" placeholder="类型">
                <el-option label="String" value="String" />
                <el-option label="Number" value="Number" />
                <el-option label="Boolean" value="Boolean" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" width="80">
            <template #default="{ row }">
              <el-checkbox v-model="row.required" />
            </template>
          </el-table-column>
          <el-table-column label="默认值" width="120">
            <template #default="{ row }">
              <el-input v-model="row.defaultVal" size="small" placeholder="默认值" />
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
                @click="formData.inputList?.splice($index, 1)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
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
  padding: 0 20px;
}
</style>
