<template>
  <div class="picture-detail">
    <el-card shadow="hover" class="detail-card">
      <img :src="picture.url" class="detail-img" />
      <div class="detail-info">
        <h2>{{ picture.name }}</h2>
        <p>分类：{{ picture.category }}</p>
        <div class="tags">
          <el-tag v-for="t in picture.tags" :key="t" type="info">{{ t }}</el-tag>
        </div>
      </div>

      <div class="action-buttons">
        <el-button type="danger" @click="handleDelete">删除图片</el-button>
        <el-button @click="$router.back()">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getPictureDetail } from '@/api/picture.js'

export default {
  name: "PictureDetail",
  data() {
    return {
      picture: {},
    };
  },
  created() {
    const id =this.$route.params.id;
    this.fetchData(id)
  },
  methods: {
    fetchData(id) {
      getPictureDetail(id).then(res => {
        console.log('res', res)
        this.picture = res.data;
      })
    },

    async handleDelete() {
      await this.$confirm("确定删除该图片？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      });
      let list = JSON.parse(localStorage.getItem("pictureList") || "[]");
      list = list.filter(p => p.id !== this.picture.id);
      localStorage.setItem("pictureList", JSON.stringify(list));
      this.$message.success("删除成功");
      this.$router.push("/my-space");
    },
  },
};
</script>

<style scoped>
.picture-detail {
  padding: 40px;
}
.detail-card {
  max-width: 700px;
  margin: 0 auto;
}
.detail-img {
  width: 100%;
  border-radius: 10px;
}
.action-buttons {
  margin-top: 20px;
  text-align: right;
}
</style>
