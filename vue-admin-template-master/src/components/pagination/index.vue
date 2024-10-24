<template>
  <div class="pagination">
    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      layout="total, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    total: {
      type: Number,
      required: true,
      default: 10
    },
    pageSize: {
      type: Number,
      default: 10
    },
    currentPage: {
      type: Number,
      default: 1
    }
  },
  methods: {
    handleCurrentChange(newPage) {
      this.$emit('update:currentPage', newPage);
    }
  }
}
</script>

<style scoped>
.pagination {
  /* margin: 20px 0; */
  margin-top: 20px;
  margin-bottom: 50px;
}
</style>