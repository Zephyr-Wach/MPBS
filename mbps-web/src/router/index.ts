import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Layout from '@/layout/Layout.vue';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: Layout,
        children: [
            {
                path: '',
                name: 'Home',
                component: () => import('@/views/Home.vue'),
            },
            {
                path: 'blog',
                name: 'Blog',
                component: () => import('@/views/Blog.vue'),
            },
            {
                path: 'cloud',
                name: 'Cloud',
                component: () => import('@/views/Cloud.vue'),
            },
            {
                path: 'admin/users',
                name: 'UserManage',
                component: () => import('@/views/admin/UserManage.vue'),
            },
            {
                path: 'admin/blogs',
                name: 'BlogManage',
                component: () => import('@/views/admin/BlogManage.vue'),
            },
            {
                path: 'admin/clouds',
                name: 'CloudManage',
                component: () => import('@/views/admin/CloudManage.vue'),
            },
            {
                path: 'admin/deliver',
                name: 'DeliverBlog',
                component: () => import('@/views/admin/DeliverBlog.vue'),
            },
        ],
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue'),
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
