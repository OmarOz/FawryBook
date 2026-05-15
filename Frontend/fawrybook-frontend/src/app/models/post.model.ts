import { Author } from './user.model';
import { Category } from './category.model';
import { CommentRequest } from './comment.model';
import { Tag } from './tag.model';

export interface Post {
  id: string;
  title: string;
  content: string;
  author: Author;
  category: Category;
  tags: Tag[];
  createdAt: string;
  updatedAt: string;
  likesCount: number;
  dislikesCount: number;
  commentsCount: number;
  comments: CommentRequest[];
  rating: number;
}

export interface CreatePostRequest {
  title: string;
  content: string;
  categoryId: string;
  tagIds: string[];
}