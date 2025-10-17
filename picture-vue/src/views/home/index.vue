<template>
  <div id="home">
    <!-- 搜索框 -->
    <div class="search-bar">
      <el-input placeholder="从海量图片中搜索" v-model="input" class="input-with-select">
        <el-button slot="append" icon="el-icon-search" @click="doSearch"></el-button>
      </el-input>
    </div>

    <!-- 分类 Tabs -->
    <div class="category-bar">
      <el-tabs v-model="selectedCategory" @tab-click="doSearchCategory">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane
          v-for="category in categoryList"
          :key="category"
          :label="category"
          :name="category"
        />
      </el-tabs>
    </div>

    <!-- 标签选择 -->
    <div class="tag-bar">
      <span style="margin-right: 8px;">标签：</span>
      <el-checkbox-group v-model="selectedTagList" @change="doSearchTag">
        <el-checkbox-button
          v-for="tag in tagList"
          :key="tag"
          :label="tag"
        >
          {{ tag }}
        </el-checkbox-button>
      </el-checkbox-group>
    </div>

    <!-- 图片列表 -->
    <PictureList :dataList="dataList" :loading="listLoading" style="margin: 8px 30px 0px 30px"/>

  </div>
</template>

<script>
import PictureList from "@/components/PictureList";
import { getList, getCategoryList, getTagList } from "@/api/picture.js"

export default {
  components: {
    PictureList
  },
  data() {
    return {
      pageQuery: {
        current: 1,
        pageSize: 12,
        searchText: "",  // 搜索关键词
        category: "", // 分类
        tags: [],     // 标签
      },
      listLoading: true,
      input: '',
      categoryList: [],
      tagList: [],
      selectedCategory: 'all', // 默认分类
      selectedTagList: [], // 每个标签的选中状态
      dataList: []
    }
  },
  created() {
    this.loadCategoryAndTag()
    this.fetchData()
  },
  methods: {
    // 加载分类和标签
    loadCategoryAndTag() {
      getCategoryList().then(res => {
        this.categoryList = res.data || []
      })
      getTagList().then(res => {
        this.tagList = res.data || []
      })
    },
    fetchData() {
      this.listLoading = true
      getList(this.pageQuery).then(res => {
        console.log('pictureList', res)
        this.dataList = res.data.records
        this.listLoading = false
      })
    },
    doSearchCategory() {
      this.pageQuery.current = 1; // 切换分类时回到第一页
      this.pageQuery.searchText = '';
      this.pageQuery.category = this.selectedCategory === "all" ? "" : this.selectedCategory;
      this.fetchData();
    },
    doSearchTag() {
      this.pageQuery.current = 1;
      this.pageQuery.searchText = '';
      this.pageQuery.tags = this.selectedTagList;
      this.fetchData();
    },
    doSearch() {
      this.pageQuery.current = 1;
      this.pageQuery.searchText = this.input;
      this.fetchData();
    }
  }
}

</script>

<style scoped>
/* 搜索框整体居中 */
.search-bar {
  margin: 30px auto; /* 顶部留白+居中 */
  max-width: 480px; /* 控制搜索框最大宽度 */
  width: 100%; /* 在小屏幕下自适应 */
}

/* 输入框样式 */
.input-with-select {
  width: 100%;
}

.el-select .el-input {
  width: 130px;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}

.category-bar {
  margin: 30px 30px 0px 30px;
}

.tag-bar {
  margin: 0px 30px 0px 30px;
  display: flex;
  align-items: center;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-bar ::v-deep .el-checkbox-button__inner {
  border: 0 !important;
  box-shadow: none !important;
  padding: 2px 10px;
}
</style>
