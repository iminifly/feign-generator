
<template>
  <div class="main-body">
    <el-form class="demo-form-inline query_form" :inline="true">
      <el-form-item>
        <el-select v-model="projectId" style="width: 450px;">
          <el-option
            v-for="item in projects"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
      </el-form-item>
    </el-form>
    <br>
    <!-- 数据表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="packageName"
        label="包名"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="modelName"
        label="Model名"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="updateTime"
        label="更新时间"
      />
      <el-table-column
        label="操作"
        class-name="not-pre-cell"
      >
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleSource(scope.row)">
            源码
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>
    <div class="text_r" style="margin-top: 15px;">
      <el-pagination
        popper-class="el-pagination--small"
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        :total="totalRecord"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <el-dialog title="源代码" :visible.sync="sourceDialogVisiable" center custom-class="feign-source-dialog">
      <SourceDialog :key="timer" :source="source" @func="closeSourceDialog" @success="closeSourceDialog" />
    </el-dialog>
  </div>
</template>

<script>
import { allProjects, searchModels, model } from '@/api/api.js'
import SourceDialog from '@/views/feign/components/SourceDialog'
export default {
  name: 'FeignModels',
  components: { SourceDialog },
  data() {
    return {
      tableData: [],
      pageSizes: [10, 20, 50],
      pageSize: 10,
      projectId: 0,
      projects: [],
      totalRecord: 0,
      currentPage: 1,
      timer: 0,
      sourceDialogVisiable: false,
      source: ''
    }
  },
  mounted() {
    // 初始化工程下拉列表
    this.loadProjects()
  },
  methods: {
    async loadProjects() {
      await allProjects().then(response => {
        this.projects = response.data.body
        const pid = this.$route.query.projectId
        if (pid) {
          this.projectId = parseInt(pid)
        } else {
          if (this.projects.length > 0 && !pid) {
            this.projectId = this.projects[0].id
          }
        }
        // 初始化列表数据
        this.handleCurrentChange(1)
      }).catch(() => {})
    },
    // 每页数据量变化时调用
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.handleCurrentChange(this.currentPage)
    },
    // 查询数据列表
    handleCurrentChange(page) {
      this.currentPage = page
      searchModels({
        page: page,
        size: this.pageSize,
        projectId: this.projectId
      }).then(response => {
        this.tableData = response.data.body.list
        this.totalRecord = response.data.body.total
      }).catch(() => {})
    },
    search() {
      this.currentPage = 1
      this.handleCurrentChange(1)
    },
    handleSource(row) {
      model(row.id).then(response => {
        this.source = response.data.body.modelClassContent
        this.openSourceDialog()
      }).catch(() => {})
    },
    openSourceDialog() {
      this.sourceDialogVisiable = true
      this.timer = new Date().getTime()
    },
    closeSourceDialog() {
      this.sourceDialogVisiable = false
    }
  }
}
</script>
<style>
.feign-source-dialog .el-dialog__body {
  height: 600px;
  overflow: auto;
}
.main-body {
  margin-top: 20px;
  margin-left: 20px;
}
</style>
