<template>
    <div class="audit">
        <h3 style="font-weight: bold;">活动管理</h3>

        <div class="search">
            <el-input class="item" v-model="search.activityTheme">
                <template #prepend>活动名称</template>
            </el-input>
            <el-button-group class="btn1">
                <el-button type="info" plain @click="reset">重置</el-button>
                <el-button type="primary" plain @click="toSearch()">搜索</el-button>
            </el-button-group>
            <el-button-group class="btn2">
                <el-button size="small" :class="{ active: activeButton === 0 }" type="info" plain
                    @click="toSearch(0,0)">全部</el-button>
                <el-button size="small" :class="{ active: activeButton === 1 }" type="info" plain
                    @click="toSearch(1,0)">正在进行</el-button>
                <el-button size="small" :class="{ active: activeButton === 2 }" type="info" plain
                    @click="toSearch(2,0)">已过期</el-button>
                <el-button size="small" :class="{ active: activeButton === 3 }" type="info" plain
                    @click="toSearch(3,0)">已下架</el-button>
                <el-button size="small" :class="{ active: activeButton === 4 }" type="info" plain
                    @click="toSearch(0,3)">违规</el-button>
            </el-button-group>
        </div>

        <el-table v-loading="loading" :data="tableList" border style="width: 100%" height="720" max-height="720"
            empty-text="暂无数据" size="small">
            <el-table-column fixed prop="headUrl" label="头像" width="70" align="center">
                <template #default="scope">
                    <img class="headUrl" :src="scope.row.headUrl ? scope.row.headUrl : nullheadPng" />
                </template>
            </el-table-column>
            <el-table-column prop="id" label="活动ID" width="70" align="center" />
            <el-table-column prop="theme" label="活动主题" align="center" />
            <el-table-column prop="locationName" label="活动地址" align="center">
                <template #default="scope">
                    {{ scope.row.locationName + ": " + scope.row.locationAddress }}
                </template>
            </el-table-column>
            <el-table-column prop="image" label="活动图片" width="130" align="center">
                <template #default="scope">
                    <el-image class="image" preview-teleported hide-on-click-modal
                        :src="scope.row.image" 
                        :preview-src-list="[scope.row.image]" 
                        :initial-index="1"
                        fit="cover" />
                </template>
            </el-table-column>
            <el-table-column prop="nickName" label="发布人昵称" width="100" align="center" />
            <el-table-column prop="contactType" label="发布人联系方式" width="130" align="center">
                <template #default="scope">
                    {{ scope.row.contactType + ": " + scope.row.contact }}
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="提交发布时间" width="150" align="center" />
            <el-table-column label="状态" align="center" width="140">
                <template #default="scope">
                    <div class="authopt-no" v-if="scope.row.wxImgCheck == 3">
                        {{scope.row.auditCont}} 自动下架
                    </div>
                    <view v-else>
                        <view style="display: flex; justify-content: center; gap: 10px">
                        <div v-if="(activeButton==0 || activeButton == 1) && scope.row.isPast=='0' && scope.row.isDelete=='0'" style="color: #05a902;">{{ scope.row.recommend=='1'? '（推荐）':''}}活动进行中</div>
                        </view>
                        <div v-if="(activeButton==0 || activeButton == 2) && scope.row.isPast=='1'" style="color:#d9a746;"> 活动已过期 </div>
                        <div v-if="scope.row.isDelete=='1'" style="color:red"> 活动已被下架 </div>
                    </view>
                </template>
            </el-table-column>

            <el-table-column fixed="right" label="查看详情" align="center">
                <template #default="scope">
                    <el-button-group v-if="scope.row.wxImgCheck != '3' && activeButton == 1 && scope.row.isDelete=='0'">
                        <el-button v-if="scope.row.recommend == '1'" size="small" type="info" @click="handleRecommend(scope.row.id)">取消推荐</el-button>
                        <el-button v-else size="small" type="primary" @click="handleRecommend(scope.row.id)">设置推荐</el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">下架活动</el-button>
                        <!-- 设置/取消轮播图按钮 -->
                        <el-button v-if="scope.row.swiper == '1'" size="small" type="info" @click="handleCarousel('0',scope.row.id)">取消轮播图</el-button>
                        <el-button v-else size="small" type="primary" @click="handleCarousel('0',scope.row.id)">设置轮播图</el-button>
                    </el-button-group>
                    <el-button size="small" @click="openActivityDialog(scope.row.id)"> 查看详情 </el-button>
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


        <el-dialog v-model="activityDetailDialogVisible" title="活动详情" width="60%">
            <div class="activityDetailDialog">
                <div class="dialog-left">
                    <view>{{"活动主题："+activityDetail.theme}}</view>
                    <el-image class="dialog-left-image" preview-teleported hide-on-click-modal
                        :src="activityDetail.image" 
                        :preview-src-list="[activityDetail.image]" 
                        :initial-index="1"
                        fit="cover" />
                    <view style="margin-top: 6px; color: #666;">活动描述：</view>
                    <view class="dialog-left-intro">{{activityDetail.intro}}</view>
                    <view style="margin-top: 6px; color: #666;">活动时间：</view>
                    <view style="font-weight: bold;margin-left: 8px;">{{ activityDetail.activityStart +" ---- "+activityDetail.activityEnd }}</view>
                    <view style="margin-top: 6px; color: #666;">活动要求：</view>
                    <view>
                        <view class="dialog-left-conditArray-item" v-for="(item, index) in activityDetail.conditArray" :key="index">{{item}}</view>
                    </view>
                    <view style="margin-top: 6px; color: #666;">活动地点：</view>
                    <view>【{{ activityDetail.location.locationName +"】 "+activityDetail.location.locationAddress + " "+(activityDetail.location.address ?activityDetail.location.address:'' ) }}</view>
                </div>
                <div class="dialog-right">
                    {{"报名人数：" + activityDetail.applyPeople.length + " 人"}}
                    <el-table :data="activityDetail.applyPeople" border size="small" style="width: 100%" max-height="55vh">
                        <el-table-column fixed prop="userHeadUrl" label="头像" width="70" align="center">
                            <template #default="scope">
                                <div v-if="scope.row.isIssue=='1'">
                                    <img class="headUrl-Issue" :src="scope.row.userHeadUrl ? scope.row.userHeadUrl : nullheadPng" >
                                        <div class="headUrl-Issue-tag">发布人</div>
                                    </img>
                                </div>
                                <div v-else>
                                    <img class="headUrl" :src="scope.row.userHeadUrl ? scope.row.userHeadUrl : nullheadPng"/>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column property="userId" label="用户ID" width="70" align="center"/>
                        <el-table-column property="sex" label="性别" width="70" align="center">
                            <template #default="scope">
                                <div v-if="scope.row.sex == 1">男</div>
                                <div v-else-if="scope.row.sex == 2">女</div>
                                <div v-else>未知</div>
                            </template>
                        </el-table-column>
                        <el-table-column property="grade" label="年级" width="70" align="center"/>
                        <el-table-column property="nickName" label="称呼" width="150" align="center"/>
                        <el-table-column property="contact" label="联系方式" align="center" width="150"/>
                    </el-table>
                </div>
            </div>
        </el-dialog>

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
                theme: '',
                tag: 0,
                wxCheck: 0
            },
            tableList: [],
            activityDetail: {},
            activityDetailDialogVisible: false
        }
    },
    created() {
        this.toSearch(0,0);
    },
    methods: {
        reset() {
            this.search = {
                pageCount: 1,
                pageSize: 10,
                theme: '',
                tag: 0,
                wxCheck: 0
            };
            this.loading = false;
            this.activeButton = 0;
        },
        toSearch(st, wg) {
            this.loading = true;
            this.activeButton = st;
            if (st && st != '0') {
                this.search.tag = st;
            } else {
                this.search.tag = ''
            }
            this.search.wxCheck = wg;
            
            this.$apis.activity.search(this.search).then(res => {
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

        openActivityDialog(id) {
            this.$apis.activity.pc_detail({ id: id }).then(res => {
                if (res.code == 200) {
                    console.log(res.data)
                    this.activityDetail = res.data;
                    this.activityDetailDialogVisible = true;
                } else {
                    ElMessage({ type: 'error', message: '详情查询失败！' })
                }
            });
        },

        handleDelete(id) {
            this.loading = true;
            ElMessageBox.confirm('将不再显示给用户', '确定下架该活动？', { 
                confirmButtonText: '确定', 
                cancelButtonText: '取消' 
            }).then(() => {
                this.$apis.activity.deleteActivity({ id: id }).then(res => {
                    if (res.code === 200) {
                        ElMessage({ type: 'success', message: '操作成功！' });
                        this.toSearch(this.activeButton);
                    } else {
                        ElMessage({ type: 'error', message: '操作失败！' });
                    }
                    this.loading = false; // 异步请求完成后关闭 loading
                }).catch(error => {
                    ElMessage({ type: 'error', message: '请求出错：' + error.message });
                    this.loading = false; // 请求出错时也应关闭 loading
                });
            }).catch(() => {
                this.loading = false;
            });
        },

        handleRecommend(id) {
            this.loading = true;
            ElMessageBox.confirm('将显示or隐藏在首页推荐', '确定操作？', { 
                confirmButtonText: '确定', 
                cancelButtonText: '取消' 
            }).then(() => {
                this.$apis.activity.recommendActivity({ id: id }).then(res => {
                    if (res.code === 200) {
                        ElMessage({ type: 'success', message: '操作成功！' });
                        this.toSearch(this.activeButton);
                    } else {
                        ElMessage({ type: 'error', message: '操作失败！' });
                    }
                    this.loading = false;
                }).catch(error => {
                    ElMessage({ type: 'error', message: '请求出错：' + error.message });
                    this.loading = false;
                });
            }).catch(() => {
                this.loading = false;
            });
        },

        handleCarousel(value, id) {
            this.loading = true;

            // 确认框中的提示信息，可以是一个通用的提示
            ElMessageBox.confirm(`确定修改轮播图状态吗？`, '操作确认', { 
                confirmButtonText: '确定', 
                cancelButtonText: '取消' 
            }).then(() => {
                // 调用 API，传入 id 和对应的 value
                this.$apis.activity.setCarousel({ value: value, id: id }).then(res => {
                    if (res.code === 200) {
                        // 成功后提示操作成功
                        ElMessage({ type: 'success', message: '轮播图状态修改成功！' });
                        this.toSearch(this.activeButton); // 刷新列表
                    } else {
                        ElMessage({ type: 'error', message: '操作失败！' });
                    }
                    this.loading = false; // 操作完成后关闭 loading
                }).catch(error => {
                    ElMessage({ type: 'error', message: '请求出错：' + error.message });
                    this.loading = false; // 请求出错时关闭 loading
                });
            }).catch(() => {
                this.loading = false; // 用户取消操作时关闭 loading
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

:deep(.el-table th.el-table__cell) {
    background-color: #cdcdcd;
}
:deep(.el-table thead) {
    color: #141414;
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


.headUrl {
    width: 40px;
    height: 40px;
    border-radius: 40px;
    cursor: pointer;
    vertical-align: middle;
}
.headUrl-Issue {
    width: 40px;
    height: 40px;
    border-radius: 40px;
    cursor: pointer;
    vertical-align: middle;
    position: relative;
    border: .5px solid #cdcdcd;
}
.headUrl-Issue-tag {
    position: absolute;
    width: 35px;
    height: 16px;
    line-height: 16px;
    text-align: center;
    border-radius: 5px;
    font-size: 10px;
    top: 1px;
    right: 0;
    cursor: pointer;
    background: #f5c362;
    color: #fff;
}



.activityDetailDialog {
    box-sizing: border-box;
    display: flex;
    gap: 20px;
    padding: 5px 20px 20px 20px; 
    color: #000;

    .dialog-right {
        flex: 1;
        box-sizing: border-box;
        border: 1px solid #ddd;
        border-radius: 10px;
        box-shadow: 0 10px 200px rgba(0,0,0,0.2);
        height: 60vh;
        padding: 10px;
        text-align: center;
    }
    .dialog-left {
        flex: 1;
        box-sizing: border-box;
        border-radius: 10px;
        height: 60vh;
        padding: 10px;
        display: flex;
        // border: 1px solid #ccc;
        box-shadow: 0 10px 200px rgba(0,0,0,0.2);
        background-color: #fff;
        flex-direction: column;
    }
    .dialog-left-image {
        margin-top: 5px;
        height: 180px;
        border-radius: 5px;
    }
    .dialog-left-intro {
        border-radius: 5px;
        background: #fff;
        border: 1px solid #cdcdcd;
        padding: 10px;
    }
    .dialog-left-conditArray-item {
        background: #b1cfe9;
        padding: 2px 10px;
        border-radius: 5px;
        margin-right: 10px;
    }
}

.act-delete {
    border: 1px solid #555;
    cursor: pointer;
    background: red;
    height: 42px;
    padding: 2px 10px;
    border-radius: 5px;
    color: #fff;
    margin: 0 10px;
}
.act-recommend {
    border: 1px solid #555;
    cursor: pointer;
    background: rgb(175, 255, 83);
    height: 42px;
    padding: 2px 10px;
    border-radius: 5px;
    color: #fff;
    margin: 0 10px;
}
.act-recommend-no {
    background: #fff;
}

.authopt-no {
    color: red;
}

</style>
