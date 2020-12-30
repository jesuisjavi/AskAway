import { TestBed } from '@angular/core/testing';

import { PostAnswerService } from './post-answer.service';

describe('PostAnswerService', () => {
  let service: PostAnswerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostAnswerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
