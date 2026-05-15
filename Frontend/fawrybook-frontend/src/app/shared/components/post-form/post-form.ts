import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  ChangeDetectorRef,
  SimpleChanges
} from '@angular/core';

import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup
} from '@angular/forms';

import { CommonModule } from '@angular/common';

import { CategoryService }
from '../../../services/category.service';

import { Category }
from '../../../models/category.model';

import { TagService }
from '../../../services/tag.service';

import { Tag }
from '../../../models/tag.model';

import { forkJoin, of }
from 'rxjs';

import { switchMap }
from 'rxjs/operators';


@Component({
  selector: 'app-post-form',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule
  ],

  templateUrl: './post-form.html',

  styleUrl: './post-form.css'
})
export class PostFormComponent
implements OnInit {

  @Input() initialData: any = null;

  @Output() formSubmit =
    new EventEmitter<any>();
  categories: Category[] = [];
  postForm!: FormGroup;
  tags: Tag[] = [];

  constructor(
    private fb: FormBuilder,
    private categoryService:
    CategoryService,
    private tagService: TagService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['initialData'] && this.initialData && this.postForm) {
      this.postForm.patchValue({
        id: this.initialData.id,
        title: this.initialData.title,
        content: this.initialData.content,
        categoryId: this.initialData.category?.id,
        tags: this.initialData.tags?.map((t: any) => t.name).join(', ') || ''
      });
    }
  }

  ngOnInit(): void {

    this.loadCategories();
    this.loadTags();

    this.postForm = this.fb.group({

      id:[this.initialData?.id || '',
        Validators.required
      ],

      title: [
        this.initialData?.title || '',
        Validators.required
      ],

      content: [
        this.initialData?.content || '',
        Validators.required
      ],

      categoryId: [
        this.initialData?.category?.id || '',
        Validators.required
      ],

      tags: [
        this.initialData?.tags
          ?.map((t: any) => t.name)
          .join(', ') || ''
      ]
    });
  }

  loadTags() {

  this.tagService
    .getTags()
    .subscribe({

      next: tags => {

        this.tags = tags;
        console.log('Loaded tags:', tags);
        this.cdr.detectChanges();
      }
    });
}
  
  loadCategories() {

  this.categoryService
    .getCategories()
    .subscribe({

      next: categories => {

        this.categories = categories;
        this.cdr.detectChanges();
      }
    });
  }

  createCategory(name: string) {

  if (!name.trim()) return;

  this.categoryService
    .createCategory(name)
    .subscribe({

      next: category => {

        this.categories.push(category);

        this.postForm.patchValue({
          categoryId: category.id
        });
      }
    });
  }

submitForm() {
  if (this.postForm.invalid) return;

  const formValue = this.postForm.value;
  console.log('Form Value:', formValue);
  
  // 1. Clean the input string into an array of names
  const tagNames: string[] = formValue.tags
    ?.split(',')
    .map((t: string) => t.trim())
    .filter((t: string) => !!t) || [];

  // 2. Separate existing tags (already in database) from new names
  const existingTags = this.tags.filter(tag => tagNames.includes(tag.name));
  const newTagNames = tagNames.filter(name => !this.tags.some(t => t.name === name));

  // 3. Logic: If there are new tags, create them first. If not, just emit.
  if (newTagNames.length > 0) {
    this.tagService.createTags(newTagNames).subscribe({
      next: (newlyCreatedTags) => {
        const allTags = [...existingTags, ...newlyCreatedTags];
        this.emitFinalRequest(allTags, formValue);
      },
      error: (err) => console.error('Error creating tags:', err)
    });
  } else {
    this.emitFinalRequest(existingTags, formValue);
  }
}

// Helper to keep the code organized
private emitFinalRequest(allTags: Tag[], formValue: any) {
  const request = {
    id: formValue.id, 
    title: formValue.title,
    content: formValue.content,
    categoryId: formValue.categoryId,
    // Ensure we only send valid IDs
    tagIds: allTags.map(tag => tag.id).filter(id => !!id)
  };
  
  console.log('Final Request:', request);
  this.formSubmit.emit(request);
}
}