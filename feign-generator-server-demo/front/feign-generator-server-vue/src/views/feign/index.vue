<template>
  <div class="main-body">
    <el-form class="demo-form-inline query_form" :inline="true">
      <el-form-item>
        <el-input v-model="groupId" placeholder="请输入groupId" style="width: 280px;" clearable />
      </el-form-item>
      <el-form-item>
        <el-input v-model="projectName" placeholder="请输入projectName" style="width: 280px;" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <br>
    <!-- 数据表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="groupId" label="groupId" />
      <el-table-column prop="projectName" label="projectName" :show-overflow-tooltip="true" />
      <el-table-column prop="serviceVersion" label="服务版本" />
      <el-table-column prop="description" label="服务描述" :show-overflow-tooltip="true" />
      <el-table-column prop="size" label="大小" :formatter="formatLength" />
      <el-table-column prop="updateTime" label="更新时间" />
      <el-table-column label="操作" class-name="not-pre-cell" width="280px">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleDownload(scope.row)">
            下载
          </el-button>
          <el-button type="text" size="small" @click="handleFeignClient(scope.row)">
            FeignClient
          </el-button>
          <el-button type="text" size="small" @click="handleFeignModel(scope.row)">
            FeignModel
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
    <a ref="exportLink" style="display: none;" />
  </div>
</template>

<script>
import consts from '@/utils/consts'
import { projectSearch } from '@/api/api.js'
export default {
  name: 'FeignProject',
  data() {
    return {
      tableData: [],
      pageSizes: [10, 20, 50],
      pageSize: 10,
      groupId: '',
      projectName: '',
      totalRecord: 0,
      currentPage: 1
    }
  },
  mounted() {
    // 初始化列表数据
    this.handleCurrentChange(1)
  },
  methods: {
    // 下载按钮
    handleDownload(row) {
      this.$axios.post(consts.baseUrl + '/api/v1/feign/download/' + row.id, {}, {
        responseType: 'blob'
      }).then(response => {
        var blob = new Blob([response.data])
        var downloadElement = this.$refs.exportLink
        var href = window.URL.createObjectURL(blob)
        downloadElement.href = href
        downloadElement.download = row.projectName + '.zip'
        downloadElement.click()
        window.URL.revokeObjectURL(href)
      }).catch(e => {
        console.log(e)
      })
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
      projectSearch({
        page: page,
        size: this.pageSize,
        groupId: this.groupId,
        projectName: this.projectName
      }).then(response => {
        this.tableData = response.data.body.list
        this.totalRecord = response.data.body.total
      }).catch(() => {})
    },
    search() {
      this.currentPage = 1
      this.handleCurrentChange(1)
    },
    // 重置查询条件
    reset() {
      this.groupId = ''
      this.projectName = ''
    },
    formatLength(row, column, cellValue, index) {
      if (cellValue / 1000000000 > 1) {
        return (cellValue / 1000000000).toFixed(2) + 'GB'
      } else if (cellValue / 1000000 > 1) {
        return (cellValue / 1000000).toFixed(2) + 'MB'
      } else if (cellValue / 1000 > 1) {
        return (cellValue / 1000).toFixed(2) + 'KB'
      } else {
        return cellValue + 'B'
      }
    },
    handleFeignClient(row) {
      this.$router.push({ path: '/feignClient/clients', query: { projectId: row.id }})
    },
    handleFeignModel(row) {
      this.$router.push({ path: '/feignClient/models', query: { projectId: row.id }})
    }
  }
}
</script>
<style>
.main-body {
  margin-top: 20px;
  margin-left: 20px;
}
</style>
