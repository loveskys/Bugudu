<template>
    <main class="login">
        <div class="login-form">
            <div class="myinput">
                <i class="miconyonghuming" />
                <input v-model="userInfo.username" placeholder="用户名">
            </div>
            <div class="myinput">
                <i class="miconmima" />
                <input class="myinput" type="password" v-model="userInfo.password" @keyup.enter="dialogverCode = true"
                    placeholder="密码">
            </div>
            <div class="login-button">
                <button @click="login()">登录</button>
            </div>
        </div>
    </main>
</template>

<script>
import { setToken } from '@/utils/localstore';
export default {
    data() {
        return {
            dialogverCode: false,
            userInfo: {
                username: 'admin',
                password: '123456'
            }
        }
    },
    methods: {
        login() {
            this.$apis.login.adminLogin(this.userInfo).then(response => {
                if (response.code == 200) {
                    setToken(response.data.token);
                    this.$router.push('/home');
                    this.$message.success({ message: '登录成功', duration: 500 });
                }
            });
        }
    }
}
</script>

<style lang="less" scoped>
.login {
    width: 100vw;
    height: 100vh;
    box-sizing: border-box;
    background-color: #ffffff;
    background: url('@/assets/image/loginbg.jpg') no-repeat fixed;
    background-size: cover;
    background-position: center center;
    display: grid;
    justify-items: flex-end;
    align-content: center;
}

.login-form {
    width: 250px;
    height: 170px;
    z-index: 2;
    border: 1px solid #222222;
    margin-right: 20%;
    margin-bottom: 6%;
    border-radius: 10px;
    background-color: rgba(40, 45, 62, 0.8);
    color: aliceblue;
    display: flex;
    flex-direction: column;
    padding: 5px 10px;

    .myinput {
        width: 100%;
        display: flex;
        height: 36px;
        margin-bottom: 15px;
        i {
            font-size: 18px;
            width: 40px;
            margin-top: 8px;
            color: #bebebe;
            text-align: center;
            cursor: pointer;
        }
        input {
            flex: 1;
            font-size: 13px;
            color: #bebebe;
            line-height: 35px;
            margin-left: 5px;
            box-sizing: border-box;
            background: none;
            border: none;
            border-bottom: 2px solid #bebebe;
            outline: 0;
        }
    }

    .login-button {
        margin: 10px auto;
        width: 65%;
        height: 32px;
        display: flex;
        button {
            width: 80%;
            font-size: 13px;
            box-sizing: border-box;
            border: none;
            margin: 1px 1px 1px 30px;
            border-radius: 15px;
            cursor: pointer;
            outline: 0;
            line-height: 15px;
            background: #bebebe;
            &:hover {
                background: #6daff7;
            }
        }
    }
}

</style>