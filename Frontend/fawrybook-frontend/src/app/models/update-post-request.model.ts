export interface UpdatePostRequest {

  id: string;

  title: string;

  content: string;

  categoryId: string;

  tagIds: string[];
}