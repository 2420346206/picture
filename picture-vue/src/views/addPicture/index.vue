<template>
  <div id="addPicturePage">
    <h2 style="margin-bottom: 16px"> 创建图片 </h2>

    <!-- 选择上传方式 -->
    <el-tabs v-model="uploadType">
      <el-tab-pane label="文件上传" name="file">
        <PictureUpload
          :picture="picture"
          :spaceId="spaceId"
          @success="onSuccess"
          @changeShow="changeShow"
        />
      </el-tab-pane>
      <el-tab-pane label="URL 上传" name="url">
        <UrlPictureUpload
          :picture="picture"
          :spaceId="spaceId"
          @success="onSuccess"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 图片信息表单 -->
    <el-form
      v-if="isShow"
      ref="pictureFormRef"
      :model="pictureForm"
      label-position="top"
      @submit.native.prevent="handleSubmit"
      >
      <el-form-item label="名称" prop="name">
        <el-input v-model="pictureForm.name" placeholder="请输入名称" clearable />
      </el-form-item>

      <el-form-item label="简介" prop="introduction">
        <el-input
          type="textarea"
          v-model="pictureForm.introduction"
          placeholder="请输入简介"
          :autosize="{ minRows: 2, maxRows: 5 }"
          clearable
        />
      </el-form-item>

      <el-form-item label="分类" prop="category">
        <el-autocomplete
          v-model="pictureForm.category"
          :fetch-suggestions="querySearchCategory"
          placeholder="请输入分类"
          clearable
        />
      </el-form-item>

      <el-form-item label="标签" prop="tags">
        <el-select
          v-model="pictureForm.tags"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="请输入标签"
          clearable
        >
          <el-option
            v-for="item in tagOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" native-type="submit" style="width: 100%">创建</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import PictureUpload from "@/components/PictureUpload";
import UrlPictureUpload from "@/components/UrlPictureUpload";

export default {
  name: "AddPicturePage",
  components: { PictureUpload, UrlPictureUpload },
  data() {
    return {
      uploadType: "file",
      picture: {},
      spaceId: null,
      isShow: true,
      pictureForm: {
        name: "",
        introduction: "",
        category: "",
        tags: []
      },
      categoryOptions: [
        { value: "风景" },
        { value: "人物" },
        { value: "建筑" },
        { value: "美食" },
      ],
      tagOptions: [
        { value: "旅游", label: "旅游" },
        { value: "工作", label: "工作" },
        { value: "学习", label: "学习" },
        { value: "生活", label: "生活" },
        { value: "艺术", label: "艺术" },
      ]
    };
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    onSuccess(data) {
      // 上传成功后的回调，可以刷新页面或显示提示
      console.log("上传成功", data);
    },
    changeShow(isShow) {
      this.isShow = isShow
    },
    // 分类的模糊搜索
    querySearchCategory(queryString, cb) {
      const results = queryString
        ? this.categoryOptions.filter(item =>
            item.value.includes(queryString)
          )
        : this.categoryOptions;
      cb(results);
    },
  },
};
</script>

<style scoped>
#addPicturePage {
  max-width: 720px;
  margin: 0 auto;
}
.el-autocomplete,
.el-select {
  width: 100% !important;
}
</style>
