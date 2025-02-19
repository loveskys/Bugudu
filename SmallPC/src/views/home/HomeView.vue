<template>
    <main class="home">
        <div class="title">
            <div>管理中心</div>
            <div>
                <span>{{ "当前管理员【" + userInfo.username + "】" }}</span>
                <span style="color: gray;">I</span>
                <span @click="logout">退出登录</span>
            </div>
        </div>

        <div class="left-menu">
            <el-aside width="160px">
                <el-menu :router="true" :collapse="false">
                    <!-- <el-sub-menu index="1">
						<template #title><i class="miconcustomer-group"></i>学生管理</template>
                        <el-menu-item-group class="opfont">
                            <el-menu-item index="studentInfo">学生信息</el-menu-item>
                        </el-menu-item-group>
                    </el-sub-menu> -->

                    <el-menu-item index="studentAudit">学生认证审核</el-menu-item>

                    <el-menu-item index="activityAudit">活动管理</el-menu-item>

                    <el-menu-item index="studentInfo">轮播图发布</el-menu-item>

                    <el-menu-item index="messageSend">通知发布</el-menu-item>

               </el-menu>
            </el-aside>
            <RouterView class="content"/>
        </div>
    </main>
</template>
<script>
import { RouterView } from 'vue-router';
import { removeAll } from '@/utils/localstore';
export default {
    data() {
        return {
            userInfo: {
                username: 'admin',
                password: '123456'
            },
        }
    },
    methods: {        
        logout() {
            removeAll();
            this.$router.push('/login')
        }
    }
}
</script>

<style lang="less" scoped>
.home {
    width: 100vw;
    height: 100vh;
}

.title {
    width: 100%;
    display: flex;
    box-sizing: border-box;
    background: #273c51;
    align-items: center;
    font-size: 14px;
    height: 4vh;
    color: #f0f0f0;
    justify-content: space-between;
    padding: 0 10px;
    span {
        cursor: pointer;
        margin: 0px 8px;
    }
}

.left-menu {
    display: flex;
    .el-aside {
        border-right: 3px solid rgb(225, 225, 225);
        .el-menu {
            border:none;
            i {
                margin-right: 10px;
            }
        }
    }

}

.content {
    width: calc(100vw - 160px);
    height: 96vh;
}
</style>