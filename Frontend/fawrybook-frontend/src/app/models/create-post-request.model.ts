export interface CreatePostRequest {

  title: string;

  content: string;

  categoryId: string;

  tagIds: string[];
}