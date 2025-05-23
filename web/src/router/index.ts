import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Layout from '@/layout/Layout.vue';
import { ref } from 'vue';

export const showLoginModal = ref(false);

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
                path: 'blog/detail/:id',
                name: 'BlogDetail',
                component: () => import('@/views/BlogDetail.vue'),  // 博客详情页
                props: true, // 这样组件可以直接通过 props 接收 id
            },
            {
                path: 'cloud',
                name: 'Cloud',
                component: () => import('@/views/Cloud.vue'),
                meta: { requiresAuth: true }
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
                path: 'admin/blogs/edit/:id',
                name: 'BlogEdit',
                component: () => import('@/views/admin/BlogEdit.vue'),
                props: true,
                meta: { requiresAuth: true },
            },
            {
                path: 'admin/clouds',
                name: 'CloudManage',
                component: () => import('@/views/admin/MediaManage.vue'),
            },
            {
                path: 'admin/deliver',
                name: 'DeliverBlog',
                component: () => import('@/views/admin/PostBlog.vue'),
            },
            {
                path: 'about',
                name: 'about',
                component: () => import('@/views/About.vue'),
            },
            {
                path: 'profile',
                name: 'Profile',
                component: () => import('@/views/Profile.vue'),
                meta: { requiresAuth: true }, // 需要登录
            },
        ],
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/components/Login.vue'),
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

function isTokenExpired(token: string): boolean {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const now = Math.floor(Date.now() / 1000);
        return payload.exp && payload.exp < now;
    } catch {
        return true;
    }
}

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.meta.requiresAuth && (!token || isTokenExpired(token))) {
        showLoginModal.value = true; // 弹窗登录
        next(false); // 阻止跳转
    } else {
        next();
    }
});

export default router;
