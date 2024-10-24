<template>
    <div style="margin-left: 20px;margin-right: 20px;">
        <!-- 商户名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入商户名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px;"></el-input>

        <!-- 商户ID搜索框 -->
        <el-input v-model="merchantIdQuery" placeholder="输入商户ID进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-left: 15px;"></el-input>

        <!-- 状态选择下拉框 -->
        <el-select v-model="params.state" placeholder="选择商户状态" style="margin-left: 15px; width: 150px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="启用" value="enabled"></el-option>
            <el-option label="禁用" value="disabled"></el-option>
        </el-select>

        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>

        <!-- 表格 -->
        <el-table :data="List" style="width: 100%; background-color: #f5f5f5;" v-loading="loading">
            <el-table-column label="商户名称" prop="merchant_name"></el-table-column>
            <el-table-column label="钱包" prop="wallet"></el-table-column>
            <el-table-column label="法人" prop="legal_person"></el-table-column>
            <el-table-column label="法人电话" prop="tele"></el-table-column>
            <el-table-column label="阿里账户" prop="alipay_account"></el-table-column>
            <el-table-column label="微信账户" prop="wechat_account"></el-table-column>
            <el-table-column label="商户状态" prop="state" width="200">
                <template slot-scope="scoped">
                    <el-switch v-model="scoped.row.state" :active-value="2" :inactive-value="3" active-color="#13ce66"
                        inactive-color="#ff4949" @change="handleChange(scoped.row)" />
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template #default="{ row }">
                    <el-button @click="handleEdit(row)" type="primary" size="small">修改</el-button>
                    <el-button @click="handleDelete(row)" type="danger" size="small"
                        style="margin-left: 8px;">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize"
            :current-page.sync="params.currentPage" @update:currentPage="fetchData" />

        <!-- 侧边栏 -->
        <el-drawer title="修改商户信息" :visible.sync="drawerVisible" size="400px" @close="closeSidebar">
            <el-form :model="selectedMerchant" ref="form">
                <el-form-item label="商户名称">
                    <el-input v-model="selectedMerchant.merchant_name"></el-input>
                </el-form-item>
                <el-form-item label="商户状态">
                    <el-radio-group v-model="selectedMerchant.state">
                        <el-radio label="1">正常</el-radio>
                        <el-radio label="0">冻结</el-radio>
                    </el-radio-group>
                </el-form-item>
                <!-- 添加其他需要修改的字段 -->
                <el-form-item>
                    <el-button type="primary" @click="saveChanges">保存</el-button>
                    <el-button @click="closeSidebar">取消</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetMerchantList } from '@/api/Pagination';

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
                storeNumber: '',
                nickname: '',
                state: '', // 状态参数
                merchantId: '' ,// 商户ID参数
                total:1
            },
            loading : false,
            List: [],
            searchQuery: '', // 搜索商户名称
            merchantIdQuery: '', // 搜索商户ID
            drawerVisible: false, // 控制侧边栏的显示与隐藏
            selectedMerchant: {}   // 用于存储被修改的商户信息
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.loading = true;
            this.params.nickname = this.searchQuery; // 更新商户名称参数
            this.params.merchantId = this.merchantIdQuery; // 更新商户ID参数
            this.params.state = this.params.state || ''; // 更新状态参数
            GetMerchantList(this.params).then(res => {
                this.List = res.data.list;
                this.params.total = res.data.total;
                this.loading = false;
            });
        },
        resetFilters() {
            this.searchQuery = '';
            this.merchantIdQuery = '';
            this.params.state = '';
            this.params.currentPage = 1; // 重置页码
            this.fetchData(); // 重置查询条件
        },
        openSidebar(row) {
            this.selectedMerchant = { ...row }; // 克隆选中的行数据
            this.drawerVisible = true; // 显示侧边栏
        },
        closeSidebar() {
            this.drawerVisible = false; // 隐藏侧边栏
        },
        saveChanges() {
            // 这里可以添加保存修改的逻辑，比如调用接口
            console.log('保存修改:', this.selectedMerchant);
            this.closeSidebar(); // 隐藏侧边栏
        },
        handleChange(row) {
            console.log('状态变更:', row);
        },
        handleEdit(row) {
            console.log('编辑行:', row);
        },
        handleDelete(row) {
            console.log('删除行:', row);
        }
    }
}
</script>

<style scoped>
/* 您可以在这里添加样式 */
</style>