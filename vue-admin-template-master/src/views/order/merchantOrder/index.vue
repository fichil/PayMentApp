<template>
    <div style="margin-left: 20px; margin-right: 20px;">
        <!-- 订单名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入商户编号进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px;"></el-input>

        <!-- 订单ID搜索框 -->
        <el-input v-model="orderIdQuery" placeholder="输入订单ID进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-left: 15px;"></el-input>

            <!-- 商户名称搜索框 -->
        <el-input v-model="merchantIdQuery" placeholder="输入商户名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-left: 15px;"></el-input>


        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>

        <!-- 表格 -->
        <el-table :data="List" style="width: 100%; background-color: #f5f5f5;" v-loading="loading">
            <el-table-column label="商户编号" prop="merchantNumber"></el-table-column>
            <el-table-column label="商户名称" prop="merchantName"></el-table-column>
            <el-table-column label="订单号" prop="orderNumber"></el-table-column>
            <el-table-column label="支付宝/微信订单号" prop="customerAccount"></el-table-column>
            <el-table-column label="支付金额" prop="price"></el-table-column>
            <el-table-column label="支付币种" width="120">
                <template #default="{ row }">
                    <span>{{ row.currency === 0 ? '人民币' : '美金' }}</span>
                </template>
            </el-table-column>
            <el-table-column label="支付方式" width="120">
                <template #default="{ row }">
                    <span>{{ row.channel === 0 ? '支付宝' : '微信' }}</span>
                </template>
            </el-table-column>
            <el-table-column label="支付时间" prop="time" :formatter="timeMatter"></el-table-column>
            
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />

    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetOrderList } from '@/api/Pagination';

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
                merchantNumber: '', // 商户名称
                orderNumber: '', // 订单号参数
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
            this.params.merchantNumber = this.searchQuery; // 更新订单名称参数
            this.params.orderNumber = this.orderIdQuery; // 更新订单ID参数
            this.params.merchantName = this.merchantIdQuery; // 更新商户名称参数
            GetOrderList(this.params).then(res => {
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