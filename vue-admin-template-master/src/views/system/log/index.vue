<template>
    <div style="margin-left: 20px;margin-right: 20px;">
        <!-- 日期搜索框 -->
        <div class="block" style="margin-top: 20px;">
            <el-date-picker v-model="value" type="daterange" align="right" unlink-panels range-separator="至"
                start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
            </el-date-picker>


            <!-- 商户ID搜索框 -->
            <el-input v-model="adminIdQuery" placeholder="输入用户Id进行搜索"
                style="margin-bottom: 20px; width: 200px; margin-left: 15px;"></el-input>

            <!-- 查询按钮 -->
            <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

            <!-- 重置按钮 -->
            <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>
        </div>

        <!-- 表格 -->
        <el-table :data="List" style="background-color: #f5f5f5;">
            <el-table-column label="操作人ID" prop="adminId" width=150></el-table-column>
            <el-table-column label="操作名" prop="logs"></el-table-column>
            <el-table-column label="操作时间" prop="logTime" :formatter="timeMatter" ></el-table-column>
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />

    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetLogs } from '@/api/log';

export default {
    name: 'log',
    components: {
        Pagination
    },
    data() {
        return {
            params: {
                pageSize: 10,
                currentPage: 1,
                total: 1,
                time: ["2023-12-31T16:00:00.000Z", "2099-12-31T16:00:00.000Z"]
            },
            List: [],
            adminIdQuery: '', // 搜索操作员ID

            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            value: ["2023-12-31T16:00:00.000Z", "2099-12-31T16:00:00.000Z"] //筛选出来的时间 是个对象
        };
    },
    created() {
        this.fetchData();
    },
    computed: {
    },
    methods: {
        fetchData() {
            this.params.adminId = this.adminIdQuery; // 更新商户ID参数
            this.params.time = this.value;
            GetLogs(this.params).then(res => {
                this.List = res.data.list;
                this.params.total = res.data.total;
            });
        },
        resetFilters() {
            this.adminIdQuery = '';
            this.value = ["2023-12-31T16:00:00.000Z", "2099-12-31T16:00:00.000Z"];
            this.time = ["2023-12-31T16:00:00.000Z", "2099-12-31T16:00:00.000Z"];
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
        },

    }
}
</script>

<style scoped></style>