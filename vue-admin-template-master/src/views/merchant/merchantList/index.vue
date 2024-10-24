<template>
    <div style="margin-left: 20px;margin-right: 20px;">

        <!-- 商户ID搜索框 -->
        <el-input v-model="merchantIdQuery" placeholder="输入商户号进行搜索"
            style="margin-bottom: 20px; width: 200px;"></el-input>

        <!-- 商户名称搜索框 -->
        <el-input v-model="searchQuery" placeholder="输入商户名称进行搜索"
            style="margin-bottom: 20px; width: 200px; margin-top: 20px; margin-left: 15px;"></el-input>


        <el-select v-model="params.state" placeholder="选择商户状态" style="margin-left: 15px; width: 150px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="正常" value="1"></el-option>
            <el-option label="未审核" value="0"></el-option>
            <el-option label="冻结" value="2"></el-option>
        </el-select>

        <!-- 查询按钮 -->
        <el-button type="primary" @click="fetchData" style="margin-left: 15px;">查询</el-button>

        <!-- 重置按钮 -->
        <el-button @click="resetFilters" style="margin-left: 8px;">重置</el-button>


        <!-- 添加商户按钮 -->
        <el-button type="success" @click="addMerchant"
            style="float: right; margin-bottom: 20px;margin-top: 20px;">添加商户</el-button>

        <!-- 表格 -->
        <el-table :data="List" style="width: 100%;" v-loading="loading">
            <el-table-column label="商户号" prop="merchantNumber"></el-table-column>
            <el-table-column label="商户名称" prop="merchantName"></el-table-column>
            <el-table-column label="钱包" prop="wallet"></el-table-column>
            <el-table-column label="法人" prop="legalPerson"></el-table-column>
            <el-table-column label="法人电话" prop="tele"></el-table-column>
            <el-table-column label="阿里账户" prop="alipayAccount"></el-table-column>
            <el-table-column label="微信账户" prop="wechatAccount"></el-table-column>
            <el-table-column label="商户状态" prop="state" width="200">
                <template #default="{ row }">
                    <span>
                        {{
                            row.state === 0 ? '未审核' :
                                row.state === 1 ? '正常' :
                                    row.state === 2 ? '冻结' :
                                        '未知状态'
                        }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template #default="{ row }">
                    <el-button @click="handleEdit(row)" type="primary" size="small">修改</el-button>
                    <el-button @click="row.state === 2 ? handleUnfreeze(row) : handleDelete(row)" type="danger"
                        size="small" style="margin-left: 8px;">
                        {{ row.state === 2 ? '解冻' : '冻结' }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <Pagination :total="params.total" :page-size="params.pageSize" :current-page.sync="params.currentPage"
            @update:currentPage="fetchData" />

        <!-- 侧边栏 -->
        <el-drawer title="修改商户信息" :visible.sync="drawerVisible" size="50%" @close="closeSidebar">
            <el-form :model="selectedMerchant" ref="merchantForm" style="margin-left: 20px;">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="商户名称" label-position="top">
                            <el-input v-model="selectedMerchant.merchantName" style="width: 300px;"
                                :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="法人" label-position="top">
                            <el-input v-model="selectedMerchant.legalPerson" style="width: 300px;"
                                :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="法人电话" label-position="top">
                            <el-input v-model="selectedMerchant.tele" style="width: 300px;" :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="阿里账户" label-position="top">
                            <el-input v-model="selectedMerchant.alipayAccount" style="width: 300px;"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信账户" label-position="top">
                            <el-input v-model="selectedMerchant.wechatAccount" style="width: 300px;"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" label-position="top">
                            <el-switch v-model="selectedMerchant.state" :active-value="1" :inactive-value="0"
                                class="switch-small" :disabled="false"></el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 图片区域 -->
                <el-row :gutter="20" style="margin-top: 20px; margin-bottom: 20px;">
                    <!-- 法人身份证正面 -->
                    <el-col :span="12" style="text-align: left;">
                        <p>法人身份证正面</p>
                        <el-image :src="sfzurfonturl" alt="法人身份证正面" style="height: 130px; width: 130px;" />
                    </el-col>

                    <el-col :span="12" style="text-align: left;">
                        <p>法人身份证反面</p>
                        <el-image :src="sfzbackurl" alt="法人身份证反面" style="height: 130px; width: 130px;" />
                    </el-col>

                    <el-col :span="12" style="text-align: left;"> <!-- 左对齐 -->
                        <p>商户凭证</p>
                        <el-image :src="pzurl" alt="商户配置" style="height: 130px; width: 130px;" />
                    </el-col>

                    <el-col :span="12" style="text-align: left;">
                        <p>商户二维码</p>
                        <el-image :src="qrurl" alt="商户二维码" style="height: 130px; width: 130px;" />
                        <el-button type="primary" @click="refreshQrCode" style="">刷新二维码</el-button>
                    </el-col>
                </el-row>
                <el-form-item>
                    <el-button type="primary" @click="GetMerchantmodify()">保存</el-button>
                    <el-button @click="closeSidebar()">取消</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>

        <!-- 添加商户弹窗 -->
        <el-dialog title="添加商户" :visible.sync="addMerchantVisible" width="50%">
            <el-form :model="addMerchantForm" :rules="rules" ref="addMerchantForm" style="margin-left: 20px;">
                <el-form-item label="商户名称" prop="merchantName" label-position="top">
                    <el-input v-model="addMerchantForm.merchantName" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item label="法人" prop="legalPerson" label-position="top">
                    <el-input v-model="addMerchantForm.legalPerson" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item label="法人电话" prop="tele" label-position="top">
                    <el-input v-model="addMerchantForm.tele" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item label="阿里账户" prop="alipayAccount" label-position="top">
                    <el-input v-model="addMerchantForm.alipayAccount" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item label="微信账户" prop="wechatAccount" label-position="top">
                    <el-input v-model="addMerchantForm.wechatAccount" style="width: 300px;"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="submitAddMerchant">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import Pagination from '@/components/pagination/index.vue';
import { GetMerchantList, GetRefreshQrCode } from '@/api/Pagination';
import { GetMerchantmodify, GetMerchantmodifyStatus, GetMerchantaddAccount, GetMerchantfreeze, GetMerchantunfreeze } from '@/api/modify';

export default {
    name: 'merchantList',
    components: {
        Pagination
    },
    data() {
        return {
            previewSrcList: [
                this.sfzurfonturl,
                this.sfzbackurl,
                this.pzurl,
                this.qrurl
            ],
            params: {
                total: 0,
                pageSize: 10,
                currentPage: 1,
                storeNumber: '',
                merchantName: '',
                loading: false, // 新增加载状态
                state: '', // 状态参数
            },
            loading: false,
            drawerLoading: false,
            isValidating: false, // 标志
            List: [],
            searchQuery: '', // 搜索商户名称
            drawerVisible: false,
            selectedMerchant: {}, // 用于存储当前选中的商户信息
            sfzbackurl: "",
            sfzurfonturl: "",
            pzurl: "",
            qrurl: "",
            merchantIdQuery: '', // 搜索商户ID
            addMerchantVisible: false, // 控制添加商户对话框的显示
            addMerchantForm: {
                merchantName: '',
                legalPerson: '',
                tele: '',
                alipayAccount: '',
                wechatAccount: ''
            },
            rules: {
                merchantName: [
                    { required: true, message: '商户名称不能为空', trigger: 'blur' },
                    { min: 2, max: 4, message: '商户名称必须为2-4个字', trigger: 'blur' }
                ],
                legalPerson: [
                    { required: true, message: '法人不能为空', trigger: 'blur' }
                ],
                tele: [
                    { required: true, message: '电话不能为空', trigger: 'blur' },
                    { pattern: /^[0-9]{11}$/, message: '请输入有效的电话号码', trigger: 'blur' } // 根据需要调整正则
                ],
                alipayAccount: [
                    { required: true, message: '阿里账户不能为空', trigger: 'blur' }
                ],
                wechatAccount: [
                    { required: true, message: '微信账户不能为空', trigger: 'blur' }
                ]
            }
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData() {

            this.loading = true;
            this.params.merchantName = this.searchQuery; // 更新商户名称参数
            this.params.storeNumber = this.merchantIdQuery; // 更新商户ID参数
            this.params.state = this.params.state || ''; // 更新状态参数
            GetMerchantList(this.params).then(res => {
                console.log(res.data.list);
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
            console.log('重置查询条件', this.params);
            this.fetchData(); // 重置查询条件
        },
        GetMerchantmodifyStatus(row) {
            const statusData = {
                id: row.id, // 假设每行数据有一个 id 字段
                state: row.state // 当前状态
            };
            GetMerchantmodifyStatus(statusData).then(res => {
                console.log(res);
            });

        },
        handleEdit(row) {
            this.selectedMerchant = { ...row };
            //获取图片
            this.sfzbackurl = row.identityBack;
            this.sfzurfonturl = row.identityFront;
            this.pzurl = row.certificate;
            this.qrurl = row.qrImg;
            console.log('当前数据', row);

            //打开侧边栏
            //如果需要加载
            console.log(this.selectedMerchant);
            this.drawerLoading = true;
            this.drawerVisible = true;

        },
        closeSidebar() {
            this.drawerLoading = false;
            this.drawerVisible = false;
        },
        handleDelete(row) {
            this.$confirm('此操作将冻结该商户, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 调用 API 删除商户
                GetMerchantfreeze({
                    id: row.id,
                    state: 2
                }).then(res => {
                    this.fetchData(); // 刷新数据
                }).catch(error => {
                    console.error('Error:', error);
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });

        },
        handleUnfreeze(row) {
            this.$confirm('此操作将解冻该商户, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 调用 API 解冻商户
                GetMerchantunfreeze({
                    id: row.id,
                    state: 1
                }).then(res => {
                    this.fetchData(); // 刷新数据
                }).catch(error => {
                    console.error('Error:', error);
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
            refreshQrCode() {
                // 获得商户的id
                const merchantNumber = this.selectedMerchant.merchantNumber;
                if (!merchantNumber) {
                    this.$message.error('商户号未审核');
                    return; // 退出函数，不发起请求
                }
                // 调用接口
                GetRefreshQrCode(merchantNumber).then(res => {
                    this.qrurl = res.data; // 直接更新绑定的属性
                    console.log(this.qrurl); // 在这里打印更新后的值
                });
            },
            addMerchant() {
                this.isValidating = false;
                this.addMerchantVisible = true;
                //清空表单数据
                this.addMerchantForm = {
                    merchantName: '',
                    legalPerson: '',
                    tele: '',
                    alipayAccount: '',
                    wechatAccount: ''
                };

            },
            submitAddMerchant(addMerchantForm) {
                // 打印表单数据以调试
                console.log('Submitting:', this.addMerchantForm);

                // 调用 API 添加商户
                GetMerchantaddAccount(this.addMerchantForm).then(res => {
                    console.log('Response:', res);
                    this.addMerchantVisible = false; // 关闭对话框
                    this.fetchData(); // 刷新数据
                }).catch(error => {
                    console.error('Error:', error);
                });
            },
            resetForm() {
                // 关闭窗口
                this.addMerchantVisible = false;
                this.$refs.addMerchantForm.resetFields(); // 重置表单

                this.addMerchantForm = {
                    merchantName: '',
                    legalPerson: '',
                    tele: '',
                    alipayAccount: '',
                    wechatAccount: ''
                }; // 重置表单数据
            },


        }
    }
</script>

<style scoped>
.el-form-item {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    /* 左对齐 */
}
</style>