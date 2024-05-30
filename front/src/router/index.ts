import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
const routes: Array<RouteRecordRaw> = [
    {
        //路由初始指向
        path: '/',
        name: 'login',
        component:()=>import('../views/Login.vue'),
    },
    {
        path: '/manager',
        name: 'manager',
        component:()=>import('../views/Manager.vue'),
        children: [
            {
                path: '/adminHome',
                name: 'adminHome',
                component:()=>import('../views/manager/Home.vue')
            },
            {
                path: '/course',
                name: 'course',
                component:()=>import('../views/manager/Course.vue')
            },
            {
                path: '/teachingClass',
                name: 'teachingClass',
                component:()=>import('../views/manager/teachingClass.vue')
            },
            {
                path: '/studentManager',
                name: 'studentManager',
                component:()=>import('../views/manager/Student.vue')
            },
            {
                path: '/gradeManager',
                name: 'gradeManager',
                component:()=>import('../views/manager/Grade.vue')
            },
            {
                path: '/intervalGrade',
                name: 'intervalGrade',
                component:()=>import('../views/manager/intervalGrade.vue')
            }
        ]
    },
    {
        path: '/student',
        name: 'student',
        component:()=>import('../views/Student.vue'),
        children: [
            {
                path: '/studentHome',
                name: 'studentHome',
                component:()=>import('../views/student/Home.vue')
            },
            {
                path: '/studentGrade',
                name: 'studentGrade',
                component:()=>import('../views/student/Grade.vue')
            }
        ]
    },
    {
        path: '/teacher',
        name: 'teacher',
        component:()=>import('../views/Teacher.vue'),
        children: [
            {
                path: '/teacherHome',
                name: 'teacherHome',
                component:()=>import('../views/teacher/Home.vue')
            },
            {
                path: '/gradeManagement',
                name: 'gradeManagement',
                component:()=>import('../views/teacher/Grade.vue')
            }
        ]
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
