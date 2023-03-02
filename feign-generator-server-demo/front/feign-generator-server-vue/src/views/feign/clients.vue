
<template>
  <div class="main-body">
    <el-form class="demo-form-inline query_form" :inline="true">
      <el-form-item>
        <el-select v-model="projectId" filterable style="width: 450px;">
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
        prop="feignClassName"
        label="接口名"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="url"
        label="url"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        prop="fallbackFactoryName"
        label="fallbackFactory名"
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
          <el-button type="text" size="small" @click="handleClientSource(scope.row)">
            接口源码
          </el-button>
          <el-button type="text" size="small" @click="handleFallBackSource(scope.row)">
            fallback源码
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
import { allProjects, searchClients, client } from '@/api/api.js'
import SourceDialog from '@/views/feign/components/SourceDialog'
export default {
  name: 'FeignClients',
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
    loadProjects() {
      allProjects().then(response => {
        const pid = this.$route.query.projectId
        if (pid) {
          this.projectId = parseInt(pid)
        } else {
          if (response.data.body.length > 0 && !pid) {
            this.projectId = response.data.body[0].id
          }
        }
        this.projects = response.data.body
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
      searchClients({
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
    handleClientSource(row) {
      client(row.id).then(response => {
        this.source = response.data.body.feignClassContent
        this.openSourceDialog()
      }).catch(() => {})
    },
    handleFallBackSource(row) {
      client(row.id).then(response => {
        this.source = response.data.body.feignClassContent
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
