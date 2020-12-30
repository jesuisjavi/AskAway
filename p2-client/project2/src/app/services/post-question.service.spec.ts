import { TestBed } from '@angular/core/testing';

import { PostQuestionService } from './post-question.service';

describe('PostQuestionService', () => {
  let service: PostQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostQuestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
