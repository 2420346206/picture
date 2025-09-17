<template>
  <div id="home">
    <!-- 搜索框 -->
    <div class="search-bar">
      <el-input placeholder="从海量图片中搜索" v-model="input" class="input-with-select">
        <el-button slot="append" icon="el-icon-search"></el-button>
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
      <el-checkbox-group v-model="selectedTagList" @change="doSearch">
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
    <PictureList :dataList="dataList" :loading="loading" style="margin: 8px 30px 0px 30px"/>

  </div>
</template>

<script>
import PictureList from "@/components/PictureList";
export default {
  components: {
    PictureList
  },
  data() {
    return {
      list: null,
      listLoading: true,
      input: '',
      categoryList: ['风景', '人物', '动物'],
      tagList: ['高清', '壁纸', '插画', '艺术'],
      selectedCategory: 'all', // 默认分类
      selectedTagList: [false, false, false, false], // 每个标签的选中状态
      dataList: [
        {
          id: 1,
          name: '风景1',
          url: 'https://picsum.photos/id/1015/300/200',
          category: '风景',
          tags: ['高清', '自然']
        },
        {
          id: 2,
          name: '风景2',
          url: 'https://picsum.photos/id/1020/300/200',
          category: '风景',
          tags: ['壁纸', '艺术']
        },
        {
          id: 3,
          name: '人物1',
          url: 'https://picsum.photos/id/1005/300/200',
          category: '人物',
          tags: ['高清', '人像']
        },
        {
          id: 4,
          name: '动物1',
          url: 'https://picsum.photos/id/1024/300/200',
          category: '动物',
          tags: ['可爱', '宠物']
        },
        {
          id: 5,
          name: '建筑1',
          url: 'https://img.ixintu.com/download/jpg/202110/5da9acfd954f0aad37bb4624b61a86ee_800_550.jpg!con',
          category: '建筑',
          tags: ['艺术', '城市']
        }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList().then(response => {
        this.list = response.data.items
        this.listLoading = false
      })
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
