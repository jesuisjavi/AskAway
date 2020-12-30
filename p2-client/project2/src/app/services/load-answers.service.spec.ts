import { TestBed } from '@angular/core/testing';

import { LoadAnswersService } from './load-answers.service';

describe('LoadAnswersService', () => {
  let service: LoadAnswersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadAnswersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
