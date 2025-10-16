<template>
  <div class="my-space-page">
    <!-- 空间统计栏 -->
    <div class="stat-bar">
      <el-row :gutter="20">
        <el-col :span="6" v-for="stat in stats" :key="stat.label">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-title">{{ stat.label }}</div>
            <div class="stat-value">{{ stat.value }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选区域 -->
    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" :model="pageQuery" label-width="80px">
        <el-form-item label="空间名称">
          <el-input
            v-model="pageQuery.spaceName"
            placeholder="请输入空间名称"
            clearable
            prefix-icon="el-icon-search"
            @input="handleFilterChange"
          />
        </el-form-item>

        <el-form-item label="空间类型">
          <el-select
            v-model="pageQuery.spaceLevel"
            placeholder="请选择"
            clearable
            @change="handleFilterChange"
          >
            <el-option label="全部" value="" />
            <el-option label="普通版" value="1" />
            <el-option label="专业版" value="2" />
            <el-option label="企业版" value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="排序">
          <el-select v-model="pageQuery.order" placeholder="请选择" @change="handleFilterChange">
            <el-option label="创建时间最新" value="desc" />
            <el-option label="创建时间最早" value="asc" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 空间卡片展示 -->
    <el-row :gutter="20" class="space-list">
      <el-col
        :span="6"
        v-for="item in dataList"
        :key="item.id"
      >
        <el-card shadow="hover" class="space-card">
          <div class="card-header">
            <span class="space-name">{{ item.spaceName }}</span>
            <el-tag size="mini" type="info">{{ item.spaceLevel }}</el-tag>
          </div>
          <div class="card-body">
            <p>创建时间：{{ item.createTime }}</p>
            <p class="desc">{{ item.spaceIntroduction }}</p>
          </div>
          <div class="card-footer">
            <el-button type="primary" size="mini" plain>进入空间</el-button>
            <el-button type="danger" size="mini" plain>删除</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        layout="total, prev, pager, next"
        :page-size="pageSize"
        :current-page.sync="currentPage"
        :total="dataList.length"
      />
    </div>
  </div>
</template>

<script>
import { getSpaceList } from "@/api/space.js";

export default {
  name: "MySpace",
  data() {
    return {
      pageQuery: {
        spaceName: '',
        spaceIntroduction: '',
        spaceLevel: null,
        spaceType: '1',
        order: "desc",
      },
      stats: [
        { label: "总空间数", value: 8 },
        { label: "普通版", value: 3 },
        { label: "专业版", value: 3 },
        { label: "企业版", value: 2 },
      ],
      dataList: [
        { id: 1, spaceName: "业务分析空间", spaceLevel: "1", createTime: "2025-10-01", spaceIntroduction: "用于业务数据分析的空间" },
        { id: 2, spaceName: "测试空间", spaceLevel: "2", createTime: "2025-09-20", spaceIntroduction: "测试功能用的空间" },
        { id: 3, spaceName: "营销空间", spaceLevel: "3", createTime: "2025-08-11", spaceIntroduction: "存放营销相关内容" },
        { id: 4, spaceName: "开发空间", spaceLevel: "1", createTime: "2025-07-09", spaceIntroduction: "项目开发资源空间" },
        { id: 5, spaceName: "统计空间", spaceLevel: "2", createTime: "2025-10-10", spaceIntroduction: "用于统计与报表展示" },
        { id: 6, spaceName: "广告营销空间", spaceLevel: "2", createTime: "2025-09-01", spaceIntroduction: "广告策略与预算管理" },
        { id: 7, spaceName: "自动化测试空间", spaceLevel: "1", createTime: "2025-09-22", spaceIntroduction: "自动化脚本与报告" },
        { id: 8, spaceName: "产品开发空间", spaceLevel: "2", createTime: "2025-08-20", spaceIntroduction: "用于产品开发协作" },
        { id: 9, spaceName: "自动化测试空间", spaceLevel: "1", createTime: "2025-09-22", spaceIntroduction: "自动化脚本与报告" },
        { id: 10, spaceName: "产品开发空间", spaceLevel: "2", createTime: "2025-08-20", spaceIntroduction: "用于产品开发协作" },
      ],
      currentPage: 1,
      pageSize: 8,
    };
  },
  computed: {
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleFilterChange() {
      this.currentPage = 1;
    },
    fetchData() {
      getSpaceList(this.pageQuery).then(res => {
        console.log('pictureList', res)
        this.dataList = res.data.records
      })
    },
  },
};
</script>

<style scoped>
.my-space-page {
  background: linear-gradient(180deg, #f5f7fa 0%, #eef1f5 100%);
  height: calc(100vh - 50px);
  padding: 30px 40px;
}

/* 统计卡片 */
.stat-bar {
  margin-bottom: 25px;
}
.stat-card {
  text-align: center;
  background: #fff;
  border-radius: 10px;
  transition: 0.2s;
}
.stat-card:hover {
  transform: translateY(-3px);
}
.stat-title {
  color: #888;
  font-size: 13px;
}
.stat-value {
  color: #409EFF;
  font-size: 20px;
  font-weight: bold;
  margin-top: 5px;
}

/* 筛选 */
.filter-card {
  margin-bottom: 25px;
}

/* 空间卡片 */
.space-list {
  margin-top: 10px;
}
.space-card {
  border-radius: 12px;
  margin-bottom: 25px;
  transition: 0.3s;
  background: #fff;
  border: none;
}
.space-card:hover {
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.2);
  transform: translateY(-4px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 10px;
}

.card-body {
  color: #666;
  font-size: 13px;
  line-height: 1.6;
  min-height: 60px;
}

.desc {
  margin-top: 4px;
  color: #888;
}

.card-footer {
  margin-top: 10px;
  text-align: right;
}

.pagination-container {
  text-align: center;
  margin-top: 25px;
}

.el-form-item {
  margin-bottom: 0;
}
</style>
