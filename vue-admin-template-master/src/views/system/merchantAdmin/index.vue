<template>
    <div style="margin-left: 20px;margin-right: 20px;">
        <!-- 名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入商铺名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px; margin-left: 15px;"></el-input>


        <!-- ID搜索框 -->
        <el-input v-model="adminIdQuery" placeholder="输入店铺ID进行搜索"
            style="margin-bottom: 20px; width: 200px;margin-left: 15px "></el-input>




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
            <el-table-column label="店铺ID" prop="storeNumber"></el-table-column>
            <el-table-column label="账号" prop="account"></el-table-column>
            <el-table-column label="昵称" prop="nickname"></el-table-column>
            <el-table-column label="账户状态" prop="state" width="200">
                <template slot-scope="scoped">
                    <el-switch v-model="scoped.row.state" :active-value="1" :inactive-value="0" active-color="#13ce66"
                        inactive-color="#ff4949" @change="handleChange(scoped.row)" />
                </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" :formatter="timeMatter"></el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button type="danger" @click="resetPassword(scope.row)">重置密码</el-button>

                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />


    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetShop } from '@/api/platAdmin';
import { GetAdminresetPassword, GetMerchantmodifyStatus } from '@/api/modify.js';

export default {
    name: 'merchantAdmin',
    components: {
        Pagination
    },
    data() {
        return {
            total: 1,
            newpasswrod: 'e10adc3949ba59abbe56e057f20f883e',
            params: {
                pageSize: 10,
                currentPage: 1,
                nickname: '',
                state: '', // 状态参数
                storeNumber: '',// 商铺ID参数

            },
            loading: false,
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
    computed: {
    },
    methods: {
        fetchData() {
            this.loading = true;
            this.params.nickname = this.searchQuery; // 更新名称参数
            this.params.sotreNumber = this.adminIdQuery; // 更新ID参数
            this.params.state = this.params.state || ''; // 更新状态参数
            GetShop(this.params).then(res => {
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
        // 修改状态
        handleChange(row) {
            const newState = row.state === 1 ? 1 : 0;
            GetMerchantmodifyStatus({
                id: row.id,
                state: newState
            }
            ).then(res => {
                this.$message({
                    type: 'success',
                    message: '修改成功'
                });
            });
        },
        // 重置密码
        resetPassword(row) {
            this.$confirm('确定要重置密码吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                GetAdminresetPassword({ id: row.id, password: this.newpasswrod })
                    .then(() => {
                        this.$message({
                            type: 'success',
                            message: '重置成功'
                        });
                    })
            });
        }
    }
}
</script>

<style scoped>
/* 您可以在这里添加样式 */
</style>