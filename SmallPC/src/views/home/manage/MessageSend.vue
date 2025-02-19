<template>
    <div class="audit">
        <h3 style="font-weight: bold;">官方消息发布</h3>

        <div class="message">
            <el-input class="item" v-model="msg.msgTheme" placeholder="请输入消息主题" clearable/>
            <el-input v-model="msg.msgText" maxlength="1000" placeholder="请输入消息内容" show-word-limit type="textarea"
                :autosize="{ minRows: 8, maxRows: 8 }" />
            <el-button style="width: 100px; margin: 0 auto;" type="primary" plain @click="send()">发布</el-button>
        </div>

        <el-table v-loading="loading" :data="tableList" border style="width: 100%" height="480" max-height="480"
            empty-text="暂无数据" size="small">
            <el-table-column fixed prop="userHeadUrl" label="头像" width="60" align="center">
                <template #default="scope">
                    <img class="headUrl" :src="scope.row.userHeadUrl ? scope.row.userHeadUrl : nullLogoPng" />
                </template>
            </el-table-column>
            <el-table-column prop="msgTheme" label="消息主题" width="400" align="center" />
            <el-table-column prop="msgText" label="消息内容" align="center" />
            <el-table-column prop="createTime" label="发布时间" width="200" align="center" />

            <el-table-column fixed="right" label="操作" align="center">
                <template #default="scope">
                    <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"> 删除 </el-button>
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
import logo from '@/assets/image/logo.png'

import { ElMessage, ElMessageBox } from 'element-plus'
export default {
    data() {
        return {
            nullLogoPng: logo,
            loading: false,
            total: 0,

            search: {
                pageCount: 1,
                pageSize: 10,
            },

            msg: {
                msgTheme: "不咕嘟",
                msgText: ""
            },

            tableList: []
        }
    },
    created() {
        this.toSearch();
    },
    methods: {
        toSearch() {
            this.loading = true;
            this.$apis.message.search(this.search).then(res => {
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

        send() {
            if (!this.msg.msgTheme) {
                ElMessage({ type: 'error', message: '请输入消息主题' }); return;
            }
            if (!this.msg.msgText) {
                ElMessage({ type: 'error', message: '请输入消息内容' }); return;
            }

            this.loading = true;
            this.$apis.message.send(this.msg).then(res => {
                if (res.code == 200) {
                    ElMessage({ type: 'success', message: '发送成功！' });
                    this.msg = {
                        msgTheme: "",
                        msgText: ""
                    };
                    this.toSearch(this.activeButton);
                } else {
                    ElMessage({ type: 'error', message: '发送失败！' }); return;
                }
                this.loading = false;
            });
        },

        handleDelete(id) {
            this.loading = true;
            ElMessageBox.confirm('将不再显示给用户', '确定删除此官方通知？', { confirmButtonText: '确定', cancelButtonText: '取消' })
            .then(() => {
                this.$apis.message.deleteMsg({ id: id }).then(res => {
                    if (res.code == 200) {
                        ElMessage({ type: 'success', message: '删除成功！' });
                        this.toSearch(this.activeButton);
                    } else {
                        ElMessage({ type: 'error', message: '删除失败！' })
                    }
                    this.loading = false;
                });
            })
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

.message {
    width: 50%;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    gap: 5px;
    border-radius: 5px;
    padding: 5px 5px 5px 0;
    margin-bottom: 5px;
    justify-content: center;
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
    text-align: start;
}

.image {
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
