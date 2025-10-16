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

      <el-form-item label="空间简介" prop="spaceIntroduction">
        <el-input
          v-model="spaceForm.spaceIntroduction"
          placeholder="请输入空间简介"
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
        {{ spaceLevel.text }}：最大数量 {{ spaceLevel.maxCount }} 张
      </p>
    </el-card>
  </div>
</template>

<script>
import { addSpace } from "@/api/space.js";

export default {
  name: 'AddSpacePage',
  data() {
    return {
      loading: false,
      spaceForm: {
        spaceName: '',
        spaceIntroduction: '',
        spaceLevel: null,
        spaceType: '1'
      },
      SPACE_LEVEL_OPTIONS: [
        { label: '普通版', value: 0 },
        { label: '专业版', value: 1 },
        { label: '企业版', value: 2 },
      ],
      spaceLevelList: [
        { text: '普通版', maxCount: 100 },
        { text: '专业版', maxCount: 1000 },
        { text: '企业版', maxCount: 10000 },
      ],
    }
  },
  methods: {
    handleSubmit() {
      this.loading = true
      addSpace(this.spaceForm).then(res => {
        this.$message.success('创建成功！')
        console.log(res);
        this.loading = false
        // 提交后跳转页面
        this.$router.push('/team/teamSpace')
      }).catch(err => {
          this.$message.error("创建失败：" + err.message);
        });
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
