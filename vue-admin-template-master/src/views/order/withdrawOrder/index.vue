<template>
    <div style="margin-left: 20px; margin-right: 20px;">
        <!-- 订单名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入提现账户名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px;"></el-input>

        <el-input v-model="merchantIdQuery" placeholder="输入商户名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-left: 15px;"></el-input>



        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>

        <!-- 表格 -->
        <el-table :data="List" style="width: 100%; background-color: #f5f5f5;" v-loading="loading">
            <el-table-column label="提现账户" prop="withdrawAccount"></el-table-column>
            <el-table-column label="商户名称" prop="merchantName"></el-table-column>
            <el-table-column label="提现金额" prop="price"></el-table-column>
            <el-table-column label="手续费" prop="tax"></el-table-column>
            <el-table-column label="提现时间" prop="createTime" :formatter="timeMatter"></el-table-column>

        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />

    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetRefundList } from '@/api/Pagination';

export default {
    name: 'Table',
    components: {
        Pagination
    },
    data() {
        return {
            params: {
                pageSize: 10,
                currentPage: 1,
                total: 1,
                withdrawAccount: '', // 搜索提现账户
            },
            loading: false,
            List: [],
            searchQuery: '', // 搜索订单名称
            merchantIdQuery: '', // 搜索商户名称
            orderIdQuery: '', // 搜索订单ID
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.loading = true;
            this.params.withdrawAccount = this.searchQuery; // 更新订单名称参数
            this.params.merchantName = this.merchantIdQuery; // 更新商户名称参数
            GetRefundList(this.params).then(res => {
                this.List = res.data.list;
                this.params.total = res.data.total;
                this.loading = false;
            });
        },
        resetFilters() {
            this.searchQuery = '';
            this.merchantIdQuery = '';
            this.params.currentPage = 1; // 重置页码
            this.fetchData(); // 重置查询条件
        },


        // 日期格式化
        timeMatter(row, colum, cellValue) {
            let myDate = new Date(cellValue);
            return myDate.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
        }
    }
}
</script>

<style scoped>
/* 您可以在这里添加样式 */
</style>