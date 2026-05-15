import { TestBed } from '@angular/core/testing';

import { PostDetails } from './post-details';

describe('PostDetails', () => {
  let service: PostDetails;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostDetails);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
