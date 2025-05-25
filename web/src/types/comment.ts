export interface CommentListItem {
    id: number;
    userId: number;
    userName: string;
    content: string;
    createdAt: string;
    children?: CommentListItem[];
}
