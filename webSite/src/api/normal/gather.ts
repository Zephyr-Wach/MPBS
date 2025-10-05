import request from '@/utils/request.ts'

// 获取合集列表
export function getCollectionList() {
    return request.get('/public/gather/list');
}

// 搜索合集
export function searchCollection(keyword: string) {
    return request.get('/public/gather/search', {
        params: { keyword }
    });
}

// 从合集从获取笔记列表
export function getNotesInCollection(collectionId:string){
    return request.get(`/public/relation/queryGatherNotes?gatherId=${collectionId}`)
}

// 获取笔记内容
export function getNote(noteId:string){
    return request.get(`/public/gather/getNote?noteId=${noteId}`)
}

