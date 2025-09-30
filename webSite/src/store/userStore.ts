import { defineStore } from 'pinia'
import {computed, ref, watch} from 'vue'

export const useUserStore = defineStore('user', () => {

    const userId = ref<string | null>(null)
    const userName = ref<string | null>(null)
    const token = ref<string | null>(null)
    const refreshToken = ref<string | null>(null)

    const userProfile = ref<Array<{ key: string; label: string; value: any }>>([])

    function login(payload: {
        userId: string
        userName: string
        token: string
        refreshToken: string
    }) {
        userId.value = payload.userId
        userName.value = payload.userName
        token.value = payload.token
        refreshToken.value = payload.refreshToken
    }

    function setUserProfile(profile: Array<{ key: string; label: string; value: any }>) {
        userProfile.value = profile
    }
    const getUserProfile = computed(() => userProfile.value)

    function logout() {
        userId.value = null
        userName.value = null
        token.value = null
        refreshToken.value = null
        userProfile.value = []
    }

    const isLoggedIn = ref(false)
    watch([token, userId], () => {
        isLoggedIn.value = !!token.value && !!userId.value
    }, { immediate: true })

    return { userId, userName, token, refreshToken, isLoggedIn, login, logout, getUserProfile, setUserProfile }
})
