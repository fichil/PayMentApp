<template>
    <div style="margin-left: 20px;margin-right: 20px;">

        <!-- ID搜索框 -->
        <el-input v-model="adminIdQuery" placeholder="输入ID进行搜索"
            style="margin-bottom: 20px; width: 200px; "></el-input>

        <!-- 名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入昵称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px; margin-left: 15px;"></el-input>

        
        <!-- 状态选择下拉框 -->
        <el-select v-model="params.state" placeholder="选择商户状态" style="margin-left: 15px; width: 150px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
        </el-select>

        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>

        <!-- 表格 -->
        <el-table :data="List" style="width: 100%; background-color: #f5f5f5;" v-loading="loading">
            <el-table-column label="ID" prop="id"></el-table-column>
            <el-table-column label="账号" prop="account"></el-table-column>
            <el-table-column label="昵称" prop="nickname"></el-table-column>
            <el-table-column label="账户状态" prop="state" width="200">
                <template slot-scope="scoped">
                    <el-switch v-model="scoped.row.state" :active-value="1" :inactive-value="0" active-color="#13ce66"
                        inactive-color="#ff4949" @change="handleChange(scoped.row)" />
                </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" :formatter="timeMatter"></el-table-column>
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize"
            :current-page.sync="params.currentPage" @update:currentPage="fetchData" />

       
    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetAdmin } from '@/api/platAdmin';
import{GetAdminmodifyStatus} from '@/api/modify.js';

export default {
    name: 'platAdmin',
    components: {
        Pagination
    },
    data() {
        return {
            params: {
                pageSize: 10,
                currentPage: 1,
                nickname: '',
                state: '', // 状态参数
                adminId: '' ,// 商户ID参数
                total:1
            },
            loading:false,
            List: [],
            searchQuery: '', // 搜索商户名称
            adminIdQuery: '', // 搜索商户ID
            drawerVisible: false, // 控制侧边栏的显示与隐藏
            selectedMerchant: {}   // 用于存储被修改的商户信息
        };
    },
    created() {
        this.fetchData();
    },
    computed:{
    },
    methods: {
        fetchData() {
            this.loading = true;
            this.params.nickname = this.searchQuery; // 更新名称参数
            this.params.adminId = this.adminIdQuery; // 更新ID参数
            this.params.state = this.params.state || ''; // 更新状态参数
            GetAdmin(this.params).then(res => {
                console.log(res.data);
                
                this.List = res.data.list;
                this.params.total = res.data.total;
                this.loading = false;
            });
        },
        resetFilters() {
            this.searchQuery = '';
            this.adminIdQuery = '';
            this.params.state = '';
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
        // 状态修改
        handleChange(row) {
            const newState = row.state === 1 ? 1 : 0;
            GetAdminmodifyStatus({
                id: row.id,
                state: newState
            }
            ).then(res => {
                this.$message({
                    type: 'success',
                    message: '修改成功'
                });
            });
        }
        
        
    }
}
</script>

<style scoped>
/* 您可以在这里添加样式 */
</style>