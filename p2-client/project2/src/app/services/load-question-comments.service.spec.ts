import { TestBed } from '@angular/core/testing';

import { LoadQuestionCommentsService } from './load-question-comments.service';

describe('LoadQuestionCommentsService', () => {
  let service: LoadQuestionCommentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadQuestionCommentsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
