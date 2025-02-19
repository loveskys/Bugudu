import { createRouter, createWebHashHistory } from 'vue-router'

//配置router
const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        { path: "/", redirect: '/login' },
        { path: '/login', name: 'login', component: () => import('@/views/login/login.vue') },

        {
            path: '/home', name: 'home', redirect: '/studentAudit', component: () => import("@/views/home/HomeView.vue"),
            children: [
                { path: "/studentAudit", name: "studentAudit", component: () => import('@/views/home/manage/StudentAudit.vue') },
                { path: "/studentInfo", name: "studentInfo", component: () => import('@/views/home/manage/StudentInfo.vue') },
                
                { path: "/activityAudit", name: "activityAudit", component: () => import('@/views/home/manage/ActivityAudit.vue') },

                
                { path: "/messageSend", name: "messageSend", component: () => import('@/views/home/manage/MessageSend.vue') },
                
            ]
        },


    ]
})

export default router
