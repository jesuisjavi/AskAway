import { TestBed } from '@angular/core/testing';

import { LoadRecentQuestionsService } from './load-recent-questions.service';

describe('LoadRecentQuestionsService', () => {
  let service: LoadRecentQuestionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadRecentQuestionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
