
<template>
  <div>
    <el-form class="demo-form-inline query_form" :inline="true">
        <el-form-item>
          <el-select v-model="projectId" style="width: 450px;">
              <el-option
              v-for="item in projects"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
    </el-form>
    <br/>
    <!-- 数据表格 -->
    <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
            prop="packageName"
            label="包名"
            :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
            prop="modelName"
            label="Model名"
            :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            label="更新时间">
        </el-table-column>
        <el-table-column
            label="操作" class-name="not-pre-cell">
            <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleSource(scope.row)">
                    源码
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    <br />
    <div class="text_r" style="margin-top: 15px;">
      <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          popper-class="el-pagination--small"
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalRecord">
      </el-pagination>
    </div>
    <el-dialog title="源代码" :visible.sync="sourceDialogVisiable" center custom-class="feign-source-dialog">
      <SourceDialog :key="timer" :source="source" @func="closeSourceDialog" @success="closeSourceDialog" />
    </el-dialog>
  </div>
</template>

<script>
  import consts from '@/assets/js/const';
  import SourceDialog from '@/pages/feign/components/SourceDialog';
  import qs from 'querystring';
  export default {
      name: 'feignModels',
      components: { SourceDialog },
      data () {
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
      mounted () {
          // 初始化工程下拉列表
          this.loadProjects();
      },
      methods: {
          async loadProjects() {
              await this.$get(consts.API_CONTEXT + '/api/v1/feign/project/all', {}, data => {
                  this.projects = data.responseBody;
                  const pid = this.$route.query.projectId;
                  if (pid) {
                      this.projectId = parseInt(pid);
                  } else {
                      if (this.projects.length > 0 && !pid) {
                          this.projectId = this.projects[0].id;
                      }
                  }
                  // 初始化列表数据
                  this.handleCurrentChange(1);
              }, e => {});
          },
          // 每页数据量变化时调用
          handleSizeChange (size) {
              this.pageSize = size;
              this.currentPage = 1;
              this.handleCurrentChange(this.currentPage);
          },
          // 查询数据列表
          handleCurrentChange (page) {
              console.log(this.projectId);
              this.currentPage = page;
              this.$get(consts.API_CONTEXT + '/api/v1/feign/models', {
                  page: page,
                  size: this.pageSize,
                  projectId: this.projectId
              }, data => {
                  this.tableData = data.responseBody.list;
                  this.totalRecord = data.responseBody.total;
              }, e => {});
          },
          search () {
              this.currentPage = 1;
              this.handleCurrentChange(1);
          },
          handleSource (row) {
              this.$get(consts.API_CONTEXT + '/api/v1/feign/model/' + row.id, {}, data => {
                  this.source = data.responseBody.modelClassContent;
                  this.openSourceDialog();
              }, e => {});
          },
          openSourceDialog () {
              this.sourceDialogVisiable = true;
              this.timer = new Date().getTime()
          },
          closeSourceDialog () {
              this.sourceDialogVisiable = false;
          }
      }
  }
</script>
<style>
.feign-source-dialog .el-dialog__body {
    height: 600px;
    overflow: auto;
}
</style>