<template>
  <div class="my-space-page">
    <!-- 搜索与筛选 -->
    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" :model="filters" class="filter-form">
        <div class="left-filters">
          <el-form-item label="图片名称">
            <el-input
              v-model="filters.keyword"
              placeholder="请输入图片名称"
              clearable
              prefix-icon="el-icon-search"
              @input="handleFilterChange"
            />
          </el-form-item>

          <el-form-item label="图片分类">
            <el-select
              v-model="filters.category"
              placeholder="请选择"
              clearable
              @change="handleFilterChange"
            >
              <el-option label="全部" value="" />
              <el-option label="风景" value="风景" />
              <el-option label="人物" value="人物" />
              <el-option label="建筑" value="建筑" />
              <el-option label="美食" value="美食" />
            </el-select>
          </el-form-item>
        </div>

        <div class="right-button">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreatePicture">
            创建图片
          </el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 🖼 图片展示 -->
    <picture-list :data-list="paginatedPictures" />

    <!-- 📄 分页 -->
    <div class="pagination-container">
      <el-pagination
        layout="total, prev, pager, next"
        :page-size="pageSize"
        :current-page.sync="currentPage"
        :total="filteredPictures.length"
      />
    </div>
  </div>
</template>

<script>
import PictureList from "@/components/PictureList"; // ✅ 这里引入你刚才写的组件

export default {
  name: "MySpace",
  components: { PictureList },
  data() {
    return {
      filters: {
        keyword: "",
        category: "",
      },
      pictureList: [
        { id: 1, name: "海边风景", category: "风景", url: "https://picsum.photos/400/250?random=1", tags: ["自然", "蓝色"] },
        { id: 2, name: "城市夜景", category: "建筑", url: "https://picsum.photos/400/250?random=2", tags: ["灯光", "夜晚"] },
        { id: 3, name: "森林小径", category: "风景", url: "https://picsum.photos/400/250?random=3", tags: ["树木", "安静"] },
        { id: 4, name: "美味餐点", category: "美食", url: "https://picsum.photos/400/250?random=4", tags: ["午餐", "美味"] },
        { id: 5, name: "人物肖像", category: "人物", url: "https://picsum.photos/400/250?random=5", tags: ["人物", "艺术"] },
        { id: 6, name: "古典建筑", category: "建筑", url: "https://picsum.photos/400/250?random=6", tags: ["文化", "旅行"] },
        { id: 7, name: "雪山风光", category: "风景", url: "https://picsum.photos/400/250?random=7", tags: ["寒冷", "雪"] },
        { id: 8, name: "甜点下午茶", category: "美食", url: "https://picsum.photos/400/250?random=8", tags: ["甜点", "下午茶"] },
        { id: 9, name: "城市建筑群", category: "建筑", url: "https://picsum.photos/400/250?random=9", tags: ["现代", "高楼"] },
        { id: 10, name: "海滩人物", category: "人物", url: "https://picsum.photos/400/250?random=10", tags: ["夏天", "旅行"] },
      ],
      currentPage: 1,
      pageSize: 8,
    };
  },
  computed: {
    filteredPictures() {
      const { keyword, category } = this.filters;
      let list = [...this.pictureList];

      if (keyword.trim()) {
        list = list.filter(item =>
          item.name.toLowerCase().includes(keyword.toLowerCase())
        );
      }
      if (category) {
        list = list.filter(item => item.category === category);
      }
      return list;
    },
    paginatedPictures() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredPictures.slice(start, start + this.pageSize);
    },
  },
  methods: {
    handleFilterChange() {
      this.currentPage = 1;
    },
  },
};
</script>

<style scoped>
.my-space-page {
  background: linear-gradient(180deg, #f5f7fa 0%, #eef1f5 100%);
  height: calc(100vh - 50px);
  padding: 20px 30px;
}

/* 筛选 */
.filter-card {
  background: #fff;
  border-radius: 8px;
}

.filter-form {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.left-filters {
  display: flex;
  align-items: center;
  gap: 16px;
}

.right-button {
  /* 右边按钮靠右 */
}

/* 分页 */
.pagination-container {
  text-align: center;
  margin-top: 25px;
}

.el-form-item {
  margin-bottom: 0;
}
</style>
