import { TestBed } from '@angular/core/testing';

import { LoadQuestionService } from './load-question.service';

describe('LoadQuestionService', () => {
  let service: LoadQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadQuestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
