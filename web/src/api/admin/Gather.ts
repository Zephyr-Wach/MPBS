import request from '@/utils/request';

interface NoteParams {
    title: string;
    contentMd: string;
    isPublic: string;
}

interface NoteResponse {
    code: number;
    message: string;
    data: {
    };
}
export function postNote(params: NoteParams): Promise<NoteResponse> {
    return request({
        url: '/ULTIMATE/gather/addNote',
        method: 'post',
        data: params,
    });
}

export function listCollections() {
    return request({
        url: '/public/gather/list',
        method: 'get'
    });
}