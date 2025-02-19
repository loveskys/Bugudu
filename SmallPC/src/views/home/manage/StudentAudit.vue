<template>
    <div class="audit">
        <h3 style="font-weight: bold;">学生认证审核</h3>

        <div class="search">
            <el-input class="item" v-model="search.schoolName">
                <template #prepend>学校名称</template>
            </el-input>
            <el-input class="item" v-model="search.studentName">
                <template #prepend>学生姓名</template>
            </el-input>
            <el-button-group class="btn1">
                <el-button type="info" plain @click="reset">重置</el-button>
                <el-button type="primary" plain @click="toSearch()">搜索</el-button>
            </el-button-group>
            <el-button-group class="btn2">
                <el-button size="small" :class="{ active: activeButton === 0 }" type="info" plain
                    @click="toSearch(0)">全部</el-button>
                <el-button size="small" :class="{ active: activeButton === 1 }" type="info" plain
                    @click="toSearch(1)">待审核</el-button>
                <el-button size="small" :class="{ active: activeButton === 2 }" type="info" plain
                    @click="toSearch(2)">已通过</el-button>
                <el-button size="small" :class="{ active: activeButton === 3 }" type="info" plain
                    @click="toSearch(3)">未通过</el-button>
            </el-button-group>
        </div>

        <el-table v-loading="loading" :data="tableList" border style="width: 100%" height="680" max-height="680"
            empty-text="暂无数据" size="small">
            <el-table-column fixed prop="headUrl" label="头像" width="60" align="center">
                <template #default="scope">
                    <img class="headUrl" :src="scope.row.headUrl ? scope.row.headUrl : nullheadPng" />
                </template>
            </el-table-column>
            <el-table-column prop="userName" label="用户名" align="center" />
            <el-table-column prop="nickName" label="昵称" width="100" align="center" />
            <el-table-column prop="schoolName" label="学校名称" align="center" />
            <el-table-column prop="authenType" label="证件类型" width="90" align="center" />
            <el-table-column prop="authenImg" label="证件照片" width="120" align="center">
                <template #default="scope">
                    <el-image class="authenImg" preview-teleported hide-on-click-modal :src="scope.row.authenImg"
                        :preview-src-list="[scope.row.authenImg]" :initial-index="1" fit="cover" />
                </template>
            </el-table-column>
            <el-table-column prop="grade" label="年级" width="100" align="center" />
            <el-table-column prop="studentNum" label="学号" width="100" align="center" />
            <el-table-column prop="createTime" label="申请时间" width="150" align="center" />

            <!-- 新增角色选择列 -->
            <el-table-column prop="role" label="角色" width="150" align="center">
                <template #default="scope">
                    <!-- 无论认证状态，均可以修改角色 -->
                    <el-select v-model="scope.row.selectedCategory" placeholder="选择角色" @change="handleRoleChange(scope.row)">
                        <el-option label="普通用户" value="0"></el-option>
                        <el-option label="开发者" value="1"></el-option>
                        <el-option label="校园大使" value="2"></el-option>
                        <el-option label="官方认证" value="3"></el-option>
                    </el-select>
                </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" align="center">
                <template #default="scope">
                    <div class="authopt-wait" v-if="scope.row.status == '1' && scope.row.wxImgCheck != '3'">
                        <el-button size="small" type="danger" @click="handleAuth(scope.row.id, 3, scope.row.userId)">不通过</el-button>
                        <el-button size="small" type="primary" @click="handleAuth(scope.row.id, 2, scope.row.userId)">通过</el-button>
                    </div>
                    <div class="authopt-ok" v-if="scope.row.status == '2'"> 已通过认证 </div>
                    <div class="authopt-no" v-if="scope.row.status == '3' && scope.row.wxImgCheck != '3'"> 不通过：{{ scope.row.cont }} </div>
                    <div class="authopt-no" v-if="scope.row.status == '3' && scope.row.wxImgCheck == '3'">
                        {{scope.row.cont}}，自动失败
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <div class="fy">
            <el-pagination size="small" 
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="search.pageCount"
                :page-size="search.pageSize"
                :page-sizes="[10, 20, 30, 50]"
                background 
                layout="total, sizes, prev, pager, next, jumper" :total="total" />
        </div>
    </div>
</template>

<script>
import nullhead from '@/assets/image/nullhead.png'

import { ElMessage, ElMessageBox } from 'element-plus'
export default {
    data() {
        return {
            nullheadPng: nullhead,
            loading: false,
            activeButton: 0,
            total: 0,
            search: {
                pageCount: 1,
                pageSize: 10,
                schoolName: '',
                studentName: '',
                status: ''
            },
            tableList: []
        }
    },
    created() {
        this.toSearch(0);
    },
    methods: {
        reset() {
            this.search = {
                pageCount: 1,
                pageSize: 10,
                schoolName: '',
                studentName: '',
                status: ''
            };
            this.loading = false;
            this.activeButton = 0;
        },
        toSearch(st) {
            this.loading = true;
            this.activeButton = st;
            if (st && st != '0') {
                this.search.status = st;
            } else {
                this.search.status = ''
            }
            this.$apis.authen.search(this.search).then(res => {
                if (res.code == 200) {
                    this.tableList = res.data.records;
                    this.total = res.data.total;
                } else {
                    this.tableList = [];
                    this.total = 0;
                }
                this.loading = false;
            });
        },

        // 处理角色选择变化
        handleRoleChange(row) {
            const userId = row.userId;
            const selectedCategory = row.selectedCategory;  
            this.updateUserRole(userId, selectedCategory);
        },

        // 向后端发送角色变更请求
        updateUserRole(userId, selectedCategory) {
            this.$apis.authen.updateRole({ id : userId, category: selectedCategory }).then(res => {
                if (res.code === 200) {
                    ElMessage({ type: 'success', message: '角色更新成功！' });
                } else {
                    ElMessage({ type: 'error', message: '角色更新失败！' });
                }
            });
        },

        handleAuth(id, status, userId) {
            if (status === 2) {
                ElMessageBox.prompt('请输入Ta的年级（将显示在该用户的主页）：', '通过该学生身份认证', { confirmButtonText: '通过', cancelButtonText: '取消' })
                .then(({ value }) => {
                    this.updateAuthStatus(id, status, value, userId)
                })
            }
            if (status === 3) {
                ElMessageBox.prompt('请输入原因：', '驳回该学生身份认证', {confirmButtonText: '不通过', cancelButtonText: '取消',
                }).then(({ value }) => {
                    this.updateAuthStatus(id, status, value, userId)
                })
            }
        },
        updateAuthStatus(id, status, cont, userId) {
            this.loading = true;
            this.$apis.authen.updateStatus({ id: id, status: status, cont: cont, userId: userId }).then(res => {
                if (res.code == 200) {
                    ElMessage({ type: 'success', message: '操作成功！' });
                    this.toSearch(this.activeButton);
                } else {
                    ElMessage({ type: 'error', message: '修改失败！' })
                }
                this.loading = false;
            });
        },

        handleSizeChange(val) {
            this.search.pageSize = val;
            this.toSearch(this.activeButton);
        },
        handleCurrentChange(val) {
            this.search.pageCount = val;
            this.toSearch(this.activeButton);
        },

    }
}
</script>


<style lang="less" scoped>
.audit {
    padding: 5px 15px;
}

.search {
    box-sizing: border-box;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: repeat(2, 1fr);
    gap: 5px;
    border-radius: 5px;
    padding: 5px 5px 5px 0;
    margin-bottom: 5px;

    .btn2 {
        margin-top: 5px;
        grid-row: 2;
        grid-column: 1;
        display: flex;
        align-items: start;
    }

    .btn2 .el-button--info.is-plain.active {
        background-color: #409eff;
        color: #fff;
        border-color: #409eff;
    }

    .btn2 .el-button--info.is-plain:active,
    .btn2 .el-button--info.is-plain:focus {
        background-color: #409eff;
        color: #fff;
        border-color: #409eff;
    }
}

.headUrl {
    width: 40px;
    height: 40px;
    border-radius: 40px;
    vertical-align: middle;
}

:deep(.el-table th.el-table__cell) {
    background-color: #cdcdcd;
}

:deep(.el-table thead) {
    color: #141414;
}

:deep(.authopt-wait .el-button--small) {
    padding: 6px;
}

.authopt-ok {
    color: #05a902;
}

.authopt-no {
    color: red;
}

.authenImg {
    width: 100px;
    height: 60px;
    border-radius: 5px;
    vertical-align: middle;
    box-sizing: border-box;
    border: 1px solid #cdcdcd;
}


.fy {
    margin-top: 10px;
    display: flex;
    justify-content: end;
}
</style>
