import { ref } from "vue"
import { defineStore } from "pinia"

export const useLoginModal = defineStore("loginModal", () => {
    const visible = ref(false)
    const redirectTo = ref<string | null>(null)

    const open = (path?: string) => {
        visible.value = true
        if (path) redirectTo.value = path
    }

    const close = () => {
        visible.value = false
    }

    return { visible, redirectTo, open, close }
})
