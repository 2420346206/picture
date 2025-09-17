<template>
  <div class="url-picture-upload">
    <el-form :inline="true" @submit.native.prevent>
      <el-form-item label="图片地址">
        <el-input v-model="url" placeholder="请输入图片 URL" clearable style="width: 500px;" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">上传</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  name: "UrlPictureUpload",
  props: {
    picture: {
      type: Object,
      default: () => ({}),
    },
    spaceId: {
      type: [String, Number],
      default: null,
    },
  },
  data() {
    return {
      url: "",
    };
  },
  methods: {
    async handleSubmit() {
      if (!this.url) {
        this.$message.warning("请输入图片 URL");
        return;
      }
      try {
        const res = await uploadPictureByUrl({
          url: this.url,
          spaceId: this.spaceId,
        });
        if (res.data.code === 0 && res.data.data) {
          this.$message.success("上传成功");
          this.$emit("success", res.data.data);
        } else {
          this.$message.error("上传失败：" + res.data.message);
        }
      } catch (e) {
        this.$message.error("请求出错");
      }
    },
  },
};
</script>
<style scoped>
.url-picture-upload {
  display: flex;
  justify-content: center;
  margin: 16px 0;
}
</style>
