<template>
  <div id="addSpacePage">
    <h2 style="margin-bottom: 16px">
      创建 团队空间
    </h2>

    <!-- 空间信息表单 -->
    <el-form
      ref="spaceFormRef"
      :model="spaceForm"
      label-position="top"
      @submit.native.prevent="handleSubmit"
    >
      <el-form-item label="空间名称" prop="spaceName">
        <el-input
          v-model="spaceForm.spaceName"
          placeholder="请输入空间名称"
          clearable
        />
      </el-form-item>

      <el-form-item label="空间级别" prop="spaceLevel">
        <el-select
          v-model="spaceForm.spaceLevel"
          placeholder="请选择空间级别"
          clearable
          style="min-width: 180px"
        >
          <el-option
            v-for="item in SPACE_LEVEL_OPTIONS"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          native-type="submit"
          :loading="loading"
          style="width: 100%"
        >
          提交
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 空间级别介绍 -->
    <el-card>
      <div slot="header">
        <span>空间级别介绍</span>
      </div>
      <p v-for="spaceLevel in spaceLevelList" :key="spaceLevel.value">
        {{ spaceLevel.text }}：大小 {{ formatSize(spaceLevel.maxSize) }}，
        数量 {{ spaceLevel.maxCount }}
      </p>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'AddSpacePage',
  data() {
    return {
      loading: false,
      spaceForm: {
        spaceName: '',
        spaceLevel: null,
      },
      spaceType: 'personal',
      SPACE_TYPE_MAP: {
        personal: '个人空间',
        team: '团队空间',
      },
      SPACE_LEVEL_OPTIONS: [
        { label: '普通版', value: 1 },
        { label: '专业版', value: 2 },
        { label: '企业版', value: 3 },
      ],
      spaceLevelList: [
        { text: '普通版', maxSize: 1024 * 1024 * 10, maxCount: 100 },
        { text: '专业版', maxSize: 1024 * 1024 * 100, maxCount: 1000 },
        { text: '企业版', maxSize: 1024 * 1024 * 1000, maxCount: 10000 },
      ],
    }
  },
  methods: {
    formatSize(size) {
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
      if (size < 1024 * 1024 * 1024)
        return (size / 1024 / 1024).toFixed(2) + ' MB'
      return (size / 1024 / 1024 / 1024).toFixed(2) + ' GB'
    },
    handleSubmit() {
      this.loading = true
      setTimeout(() => {
        this.$message.success('提交成功')
        this.loading = false
      }, 1000)
    },
  },
}
</script>

<style scoped>

#addSpacePage {
  max-width: 720px;
  margin: 0 auto;
}
</style>
