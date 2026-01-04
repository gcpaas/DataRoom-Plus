<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { DatasetEntity } from '../api'

const props = defineProps<{
  modelValue: DatasetEntity
}>()

const emit = defineEmits<{
  'update:modelValue': [value: DatasetEntity]
}>()

const formRef = ref<FormInstance>()

const formData = reactive<DatasetEntity>({
  name: '',
  datasetType: 'http',
  parentCode: 'root',
  dataset: {
    datasetType: 'http',
    url: '',
    method: 'GET',
    headerList: [],
    body: '',
    respJsonPath: ''
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
          datasetType: 'http',
          url: '',
          method: 'GET',
          headerList: [],
          body: '',
          respJsonPath: ''
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

defineExpose({
  validate,
  resetFields,
  getData
})
</script>

<template>
  <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
    <el-form-item label="数据集名称" prop="name">
      <el-input v-model="formData.name" placeholder="请输入数据集名称" clearable />
    </el-form-item>
    <el-form-item label="请求地址">
      <el-input
        v-if="formData.dataset && 'url' in formData.dataset"
        v-model="formData.dataset.url"
        placeholder="请输入请求地址"
        clearable
      />
    </el-form-item>
    <el-form-item label="请求方法">
      <el-select
        v-if="formData.dataset && 'method' in formData.dataset"
        v-model="formData.dataset.method"
        placeholder="请选择请求方法"
      >
        <el-option label="GET" value="GET" />
        <el-option label="POST" value="POST" />
        <el-option label="PUT" value="PUT" />
        <el-option label="DELETE" value="DELETE" />
      </el-select>
    </el-form-item>
    <el-form-item label="请求头">
      <div style="width: 100%">
        <el-button
          size="small"
          @click="
            formData.dataset &&
              'headerList' in formData.dataset &&
              formData.dataset.headerList?.push({ key: '', val: '' })
          "
        >
          添加请求头
        </el-button>
        <el-table
          v-if="formData.dataset && 'headerList' in formData.dataset"
          :data="formData.dataset.headerList"
          border
          style="width: 100%; margin-top: 8px"
        >
          <el-table-column label="Key" width="200">
            <template #default="{ row }">
              <el-input v-model="row.key" size="small" placeholder="Key" />
            </template>
          </el-table-column>
          <el-table-column label="Value">
            <template #default="{ row }">
              <el-input v-model="row.val" size="small" placeholder="Value" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" fixed="right">
            <template #default="{ $index }">
              <el-button
                type="danger"
                size="small"
                link
                @click="
                  formData.dataset &&
                    'headerList' in formData.dataset &&
                    formData.dataset.headerList?.splice($index, 1)
                "
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form-item>
    <el-form-item label="请求体">
      <el-input
        v-if="formData.dataset && 'body' in formData.dataset"
        v-model="formData.dataset.body"
        type="textarea"
        :rows="5"
        placeholder="请输入请求体（JSON格式）"
      />
    </el-form-item>
    <el-form-item label="响应路径">
      <el-input
        v-if="formData.dataset && 'respJsonPath' in formData.dataset"
        v-model="formData.dataset.respJsonPath"
        placeholder="请输入响应数据的JSONPath，例如：$.data.list"
        clearable
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
  padding: 20px;
}
</style>
