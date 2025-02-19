<template>
    <div class="audit">
        <h3 style="font-weight: bold; text-align: center; margin-bottom: 20px;">轮播图发布</h3>

        <div class="message">
            <!-- 上传图片 -->
            <el-upload
                class="upload"
                action="http://175.178.118.254:8080/api/loopimg/upload" 
                list-type="picture-card"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"  
                :limit="1"
                :file-list="fileList"
                :before-upload="beforeUpload"
            >
                <i class="el-icon-plus"></i>
            </el-upload>

            <!-- 图片跳转链接输入框 -->
            <el-input
                v-model="msg.imgUrl"
                placeholder="请输入图片跳转链接"
                clearable
            />

            <!-- 发布按钮 -->
            <el-button type="primary" plain @click="send()">发布</el-button>
        </div>

        <!-- 数据表格，显示已上传的轮播图 -->
        <el-table
            v-loading="loading"
            :data="tableList"
            border
            style="width: 100%;"
            height="480"
            max-height="480"
            empty-text="暂无数据"
            size="small"
        >
            <el-table-column fixed prop="imgUrl" label="轮播图图片" width="400" align="center">
                <template #default="scope">
                    <img class="image" :src="scope.row.image" />
                </template>
            </el-table-column>
            
            <el-table-column prop="url" label="链接" width="300" align="center">
                <template #default="scope">
                    <a :href="scope.row.url" target="_blank">{{ scope.row.url }}</a>
                </template>
            </el-table-column>
    
            <el-table-column fixed="right" label="操作" align="center">
                <template #default="scope">
                    <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"> 删除 </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="fy">
            <el-pagination
                size="small"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="search.pageCount"
                :page-size="search.pageSize"
                :page-sizes="[10, 20, 30, 50]"
                background
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
            />
        </div>
    </div>
</template>


<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { getLoopImageList, uploadLoopImage, deleteLoopImage } from '@/api/api_banner'  // 引入API封装

export default {
    data() {
        return {
            loading: false,
            total: 0,
            fileList: [],

            search: {
                pageCount: 1,
                pageSize: 10,
            },

            msg: {
                imgUrl: ""  // 保存上传图片的URL
            },

            tableList: []
        }
    },
    created() {
        this.toSearch();
    },
    methods: {
        // 获取轮播图列表
        toSearch() {
            this.loading = true;
            getLoopImageList(this.search).then(res => {  // 调用获取轮播图列表API
                if (res.code == 200) {
                    // 过滤掉已删除的记录
                    this.tableList = res.data.records.filter(item => item.isDelete !== "1");
                    this.total = res.data.total;  // 确保总数正确更新
                } else {
                    this.tableList = [];
                    this.total = 0;
                }
                this.loading = false;
            }).catch(() => {
                ElMessage({ type: 'error', message: '获取轮播图失败' });
                this.loading = false;
            });
        },


        // 上传轮播图
        send() {
            if (this.fileList.length === 0) {
                ElMessage({ type: 'error', message: '请上传轮播图图片' });
                return;
            }
            if (!this.msg.imgUrl) {
                ElMessage({ type: 'error', message: '请输入图片跳转链接' });
                return;
            }

            this.loading = true;
            // 生成 FormData 对象，用于上传
            const formData = new FormData();
            formData.append('value', '1');  // 固定的字符串 "1"
            formData.append('loopImg', this.fileList[0].raw);  // 上传的图片文件
            formData.append('imgUrl', this.msg.imgUrl);        // 图片跳转链接

            // 调用上传 API
            uploadLoopImage(formData).then(res => {
                if (res.code == 200) {
                    ElMessage({ type: 'success', message: '发送成功！' });

                    // 清空表单数据
                    this.msg = { imgUrl: "" };
                    this.fileList = [];  // 清空文件列表

                    // 重新获取列表
                    this.toSearch();
                } else {
                    ElMessage({ type: 'error', message: `发送失败：${res.message || '未知错误'}` });
                }
                this.loading = false;
            }).catch(err => {
                const errorMessage = err.response?.data?.message || '上传失败，可能是网络或服务器问题';
                ElMessage({ type: 'error', message: `轮播图上传失败: ${errorMessage}` });
                this.loading = false;
            });
        },

        // 删除轮播图
        handleDelete(id) {
        this.loading = true;
        ElMessageBox.confirm('将不再显示给用户', '确定删除此轮播图？', { confirmButtonText: '确定', cancelButtonText: '取消' })
        .then(() => {
            deleteLoopImage({ id }).then(res => {  // 调用删除轮播图的API
                if (res.code == 200) {
                    ElMessage({ type: 'success', message: '删除成功！' });

                    // 重新计算总数，如果当前页为空，则回退到上一页
                    this.tableList = this.tableList.filter(item => item.id !== id);
                    if (this.tableList.length === 0 && this.search.pageCount > 1) {
                        this.search.pageCount -= 1;  // 回退到前一页
                    }

                    // 重新获取列表，确保分页正确
                    this.toSearch();
                } else {
                    ElMessage({ type: 'error', message: '删除失败！' });
                }
                this.loading = false;
            }).catch(() => {
                ElMessage({ type: 'error', message: '删除轮播图失败' });
                this.loading = false;
            });
        });
    },



        // 处理图片上传成功的回调
        handleUploadSuccess(response, file, fileList) {
            ElMessage({ type: 'success', message: '图片上传成功' });
            this.fileList = fileList;
        },

        handleUploadError(err, file, fileList) {
            console.error("图片上传失败：", err);
            ElMessage({ type: 'error', message: `图片上传失败: ${err.message || '未知错误'}` });
        },

        // 处理分页大小的变化
        handleSizeChange(val) {
            this.search.pageSize = val;
            this.toSearch();
        },
        // 处理分页页码的变化
        handleCurrentChange(val) {
            this.search.pageCount = val;
            this.toSearch();
        }
    }
}
</script>


<style lang="less" scoped>
.audit {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;  /* 页面居中布局 */
}

.message {
    width: 70%;
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.el-input,
.el-textarea {
    border-radius: 3px;  /* 简单的圆角 */
    border: 1px solid #dcdcdc;  /* 较浅的边框颜色 */
    padding: 10px;
}

.el-button {
    width: 150px;
    margin-top: 10px;
    border-radius: 3px;
    font-weight: bold;
    text-transform: uppercase;
}

.el-button--primary {
    background-color: #409eff;  /* Element Plus 默认的主色调 */
    border-color: #409eff;
    color: white;
}

.el-button--primary:hover {
    background-color: #66b1ff;
}

.upload {
    margin-top: 10px;
    padding: 20px;
    text-align: center;
}

.upload i {
    font-size: 24px;
    color: #409eff;
}

.el-table {
    border: 1px solid #e0e0e0;
    border-collapse: separate;
    border-spacing: 0;
}

.el-table th {
    background-color: #f9f9f9;
    color: #333;
    font-weight: bold;
    text-align: center;
}

.el-table td {
    padding: 15px;
    text-align: center;
}

.el-table img {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 3px;  /* 圆角处理 */
}

.fy {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.el-pagination {
    display: inline-block;
}
</style>
