import { TestBed } from '@angular/core/testing';

import { LoadUserService } from './load-user';

describe('PersonalInfoService', () => {
  let service: LoadUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
