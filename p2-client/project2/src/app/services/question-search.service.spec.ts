import { TestBed } from '@angular/core/testing';

import { QuestionSearchService } from './question-search.service';

describe('QuestionSearchService', () => {
  let service: QuestionSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
