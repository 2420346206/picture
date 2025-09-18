<template>
  <div class="picture-upload">
    <el-upload
      class="upload-demo"
      list-type="picture-card"
      action="#"
      :file-list="fileList"
      :class="{hide:hideUpload}"
      :http-request="uploadFile"
      :on-change="handleChange"
    >
    <i slot="default" class="el-icon-plus"></i>
    <div slot="file" slot-scope="{file}">
      <img
        class="el-upload-list__item-thumbnail"
        :src="file.url" alt="图片加载失败"
      >
    </div>
    </el-upload>
  </div>
</template>

<script>
import {upload} from '@/api/picture.js'

export default {
  name: "PictureUpload",
  data() {
    return {
      fileList: [],
      pictureUrl: '',
      hideUpload: false
    };
  },
  methods: {
    uploadFile({ file, onSuccess, onError }) {
      let formData = new FormData()
      formData.append('file', file)

      upload(formData).then(res => {
          // 上传成功
          this.pictureUrl = res.data
          onSuccess(res)

          this.$emit("success", {
            isShow: true,
            url: this.pictureUrl
          })
        })
        .catch(err => {
          onError(err);
          console.error(err)
        })
    },
    handleChange(file, fileList) {
      this.fileList = fileList
      console.log(this.fileList.length)
      this.hideUpload = this.fileList.length >= 1 ? true : false
      console.log(this.hideUpload)
    },
  }
}
</script>

<style scoped>
.picture-upload {
  width: 100%;
  margin: 16px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.upload-demo .el-upload {
  width: 100%;
}
.upload-placeholder {
  text-align: center;
  color: #909399;
  font-size: 14px;
}
.upload-placeholder em {
  color: #409eff;
}
.uploaded-picture {
  display: flex;
  align-items: center;
}


</style>

<style>
.hide .el-upload--picture-card {
  display: none;
}

/* 上传框：自适应父容器宽度，高度随内容自动 */
.el-upload--picture-card {
  width: 100%;
  height: auto;
  min-width: 500px;
  min-height: 200px;
  line-height: 200px;
}

/* 上传后的预览框：跟上传框保持一致 */
.el-upload-list__item {
  width: 100% !important;
  height: auto !important;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  overflow: hidden;
}

/* 图片完整显示 */
.el-upload-list__item-thumbnail {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

</style>
