<template>
    <div style="margin-left: 20px; margin-right: 20px;">

        <!-- 法人搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入法人名称进行搜索" style="margin-bottom: 20px; width: 200px;"></el-input>




        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>

        <div style="display: flex; flex-wrap: wrap; margin-top: 20px;">
            <el-card v-for="(item) in List" :key="item.id"
                style="flex: 0 0 calc(50% - 10px); margin: 5px; display: flex; flex-direction: column;">
                <el-row :gutter="20" style="flex-grow: 1;">
                    <el-col :span="12">
                        <p style="margin-left: 20px;"><span>法人：</span><span>{{ item.leglePerson }}</span></p>
                    </el-col>
                    <el-col :span="12">
                        <p style="margin-left: 20px;"><span>法人电话：</span><span>{{ item.tele }}</span></p>
                    </el-col>
                    <el-col :span="12">
                        <el-image :src="item.identityFront" :preview-src-list="srcList"
                            style="width: 200px; height: 200px;"></el-image>
                    </el-col>
                    <el-col :span="12">
                        <el-image :src="item.identityBack" style="width: 200px; height: 200px;"></el-image>
                    </el-col>
                    <el-col :span="12">
                        <el-image :src="item.certificate" style="width: 200px; height: 200px;"></el-image>
                    </el-col>
                </el-row>
                <el-col :span="24" style="text-align: right;">
                    <el-button type="primary" @click="audit(item)" style="margin: 10px;">审核</el-button>
                    <el-button type="danger" @click="reject(item)" style="margin: 10px;">拒绝</el-button>
                </el-col>
            </el-card>
        </div>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />

        <!-- 拒绝审核弹窗 -->
        <el-dialog title="拒绝审核" :visible.sync="dialogVisible" width="30%">
            <el-form :model="form">
                <el-form-item label="拒绝原因" :label-width="formLabelWidth">
                    <el-input v-model="reason" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetAuditList } from '@/api/Pagination';
import { GetMerchantagree, GetMerchantrefuse } from '@/api/modify';

export default {
    name: 'merchancheck',
    components: {
        Pagination
    },
    data() {
        return {
            srcList: [

            ],

            dialogVisible: false, // 控制拒绝审核弹窗的显示与隐藏
            form: {
                id: '',
            },
            reason: '',
            formLabelWidth: '120px',

            total: 0,
            params: {
                pageSize: 2, // 每页2条数据
                currentPage: 1,
                legalPerson: '',
            },
            loading: false,
            List: [],
            searchQuery: '', // 搜索法人名称
            sfzbackurl: "",
            sfzurfonturl: "",
            pzurl: "",
            qrurl: "",
            state: '',
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.loading = true;
            this.params.legalPerson = this.searchQuery; // 更新商户名称参数
            GetAuditList(this.params).then(res => {
                this.List = res.data.list;
                this.params.total = res.data.total;
                this.List.forEach(item => {
                    this.srcList.push(item.identityFront, item.identityBack, item.certificate);
                });
                this.loading = false;

            });
        },
        resetFilters() {
            this.searchQuery = '';
            this.merchantIdQuery = '';
            this.params.state = '';
            this.fetchData(); // 重置查询条件
        },
        audit(item) {
            this.$confirm('确定审核通过吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {

                GetMerchantagree({
                    id: item.id,
                    state: 1,
                }).then(res => {
                    this.$message({
                        type: 'success',
                        message: '审核成功'
                    });
                    this.fetchData();
                });
            });
        },
        reject(item) {
            this.dialogVisible = true;
            this.reason = '';
            this.form.id = item.id;

        },
        submitForm() {
            this.dialogVisible = false;
            //拒绝原因不能为空
            if (this.reason == '') {
                this.$message({
                    type: 'warning',
                    message: '拒绝原因不能为空'
                });
                return;
            }
            GetMerchantrefuse({
                id: this.form.id,
                reason: this.reason,
                state: 2,
            }).then(res => {
                this.$message({
                    type: 'success',
                    message: '拒绝成功'
                });
                this.fetchData();
            });
        }

    },
}
</script>

<style scoped></style>
