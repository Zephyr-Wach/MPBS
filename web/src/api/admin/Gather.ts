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

export function listNotesFromCollection() {
    return request({
        url: '/public/relation/queryGatherNotes?gatherId=',
        method: 'get'
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
