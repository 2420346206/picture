<template>
  <div class="picture-list">
    <el-row :gutter="16">
      <el-col
        v-for="picture in dataList"
        :key="picture.id"
        :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
      >
        <el-card
          class="picture-card"
          shadow="hover"
          @click="doClickPicture(picture)"
        >
          <!-- 图片封面 -->
          <img
            :src="picture.url"
            :alt="picture.name"
            class="picture-img"
          />

          <!-- 标题和描述 -->
          <div class="card-header">
            <span class="picture-title">{{ picture.name }}</span>
          </div>
          <div class="card-tags">
            <el-tag type="success" size="mini" style="margin-right: 8px">
              {{ picture.category || '默认' }}
            </el-tag>
            <el-tag
              v-for="tag in picture.tags"
              :key="tag"
              size="mini"
              type="info"
              style="margin-right: 8px"
            >
              {{ tag }}
            </el-tag>
          </div>

          <!-- 操作按钮 -->
          <!-- <div v-if="showOp" class="card-actions">
            <el-button
              type="text"
              icon="el-icon-share"
              @click.stop="doShare(picture)"
            />
            <el-button
              type="text"
              icon="el-icon-search"
              @click.stop="doSearch(picture)"
            />
            <el-button
              v-if="canEdit"
              type="text"
              icon="el-icon-edit"
              @click.stop="doEdit(picture)"
            />
            <el-button
              v-if="canDelete"
              type="text"
              icon="el-icon-delete"
              @click.stop="doDelete(picture)"
            />
          </div> -->
        </el-card>
      </el-col>
    </el-row>

    <!-- 分享弹窗 -->
    <!-- <el-dialog title="分享图片" :visible.sync="shareDialogVisible" width="30%">
      <div>
        分享链接：<el-input v-model="shareLink" readonly />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="shareDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog> -->
  </div>
</template>

<script>
export default {
  name: 'PictureList',
  props: {
    dataList: {
      type: Array,
      default: () => [],
    }
  },
  data() {
    return {

    }
  },
  methods: {

  },
}
</script>

<style scoped>
.picture-card {
  margin-top: 8px;
  margin-bottom: 8px;
  cursor: pointer;
}

.picture-img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.card-header {
  margin-top: 8px;
  font-weight: bold;
}

.card-tags {
  margin-top: 8px;
}

.card-actions {
  margin-top: 8px;
  text-align: right;
}
</style>
