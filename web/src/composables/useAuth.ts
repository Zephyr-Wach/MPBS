import { ref, computed } from 'vue'

const token = ref<string | null>(localStorage.getItem('token'))
const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))

const isLoggedIn = computed((): boolean => {
    return Boolean(token.value && refreshToken.value)
})

function login(newToken: string, newRefreshToken: string): void {
    token.value = newToken
    refreshToken.value = newRefreshToken
    localStorage.setItem('token', newToken)
    localStorage.setItem('refreshToken', newRefreshToken)
}

function logout(): void {
    token.value = null
    refreshToken.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
}

export function useAuth() {
    return {
        token,
        refreshToken,
        isLoggedIn,
        login,
        logout
    }
}
