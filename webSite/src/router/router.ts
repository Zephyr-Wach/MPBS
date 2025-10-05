import { createRouter, createWebHistory } from "vue-router"
import type { RouteRecordRaw } from "vue-router"
import { useUserStore } from "@/store/userStore"
import { useLoginModal } from "@/store/loginModal"
import Home from "@/pages/Home.vue"
import UserProfile from "@/pages/UserProfile.vue"
import NotFound from "@/pages/NotFound.vue"
import About from "@/pages/About.vue";

import Blog from "@/pages/Blog.vue"
import BlogDetail from "@/pages/BlogDetail.vue";
import PostBlog from "@/pages/admin/PostBlog.vue";
import BlogEdit from "@/pages/admin/BlogEdit.vue";
import BlogManage from "@/pages/admin/BlogManage.vue";

// import Gather from "@/pages/Gather.vue";
// import PostNote from "@/pages/admin/PostNote.vue";
// import GatherManager from "@/pages/admin/GatherManager.vue";

// import Cloud from "@/pages/admin/Cloud.vue";
// import CloudManage from "@/pages/admin/MediaManage.vue"

// import UserManage from "@/pages/admin/UserManage.vue";
// import Profile from "@/pages/user/Profile.vue";

// import LogView from "@/pages/admin/LogView.vue";

const routes: RouteRecordRaw[] = [
    { path: "/", component: Home },
    { path: '/about', component: About,},

    { path: "/userCenter", component: UserProfile, meta: { requiresAuth: true } },

    { path: "/blog", component: Blog},
    { path: "/blog/detail/:id", component: BlogDetail, props: true},
    { path: "/admin/blogs/edit/:id", component: BlogEdit, props: true, meta: {requiresAuth: true}},
    { path: "/admin/deliver", component: PostBlog, meta: { requiresAuth: true}},
    { path: '/admin/blogs', component: BlogManage, meta: { requiresAuth: true },},
    //
    // { path: "/gather", component: Gather},
    // { path: "/admin/postNote", component: PostNote, meta: { requiresAuth: true}},
    // { path: '/admin/gather', component: GatherManager, meta: { requiresAuth: true },},
    //
    // { path: 'cloud', component: Cloud, meta: { requiresAuth: true }},
    // { path: 'admin/clouds', component: CloudManage, meta: { requiresAuth: true },},
    //
    // { path: 'admin/users', component: UserManage, meta: { requiresAuth: true },},
    // { path: 'profile', component: Profile, meta: { requiresAuth: true },},
    //

    // { path: 'admin/log', component: LogView, meta: { requiresAuth: true },},

    { path: "/:pathMatch(.*)*", name: "NotFound", component: NotFound },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, _from, next) => {
    const userStore = useUserStore()
    const loginModal = useLoginModal()

    if (to.meta.requiresAuth && !userStore.isLoggedIn) {
        loginModal.open(to.fullPath)
        next({ path: "/" })
    } else {
        next()
    }
})

export default router
