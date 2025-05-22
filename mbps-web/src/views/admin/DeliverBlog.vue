<template>
  <div>
    <el-input v-model="form.title" placeholder="标题" />
    <el-input type="textarea" v-model="form.content" placeholder="内容" />
    <el-upload
        :action="uploadUrl"
        list-type="picture"
        :on-success="handleUploadSuccess"
        multiple>
      <el-button type="primary">上传图片/视频</el-button>
    </el-upload>
    <el-button type="success" @click="submit">发表</el-button>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import axios from 'axios'

const uploadUrl = '/file/upload'
const form = ref({
  title: '',
  content: '',
  mediaUrls: [] as string[]
})

const handleUploadSuccess = (res: any) => {
  form.value.mediaUrls.push(res.url)
}

const submit = async () => {
  await axios.post('/blog/create', form.value)
  alert('发表成功')
}
</script>

<style scoped>
</style>
