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
        url: '/ULTIMATE/gather/list',
        method: 'get'
    });
}

export function listNotesFromCollection(params: { gatherId: string }) {
    return request({
        url: '/ULTIMATE/relation/queryGatherNotes',
        method: 'get',
        params,
    });
}


interface updateCollectionParams {
    title: string;
    description: string;
    isPublic: string;
}

interface updateCollectionResponse {
    code: number;
    message: string;
    data: {
    };
}
export function updateCollection(params: updateCollectionParams, gatherId: string): Promise<updateCollectionResponse> {
    return request({
        url: `/ULTIMATE/gather/updateCollection?gatherId=${gatherId}`,
        method: 'post',
        data: params,
    });
}

export function delCollection(gatherId: string) {
    return request({
        url: `/ULTIMATE/gather/deleteCollection?gatherId=${gatherId}`,
        method: 'get'
    });
}
