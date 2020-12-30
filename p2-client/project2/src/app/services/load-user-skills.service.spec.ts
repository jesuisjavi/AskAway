import { TestBed } from '@angular/core/testing';

import { LoadUserSkillsService } from './load-user-skills.service';

describe('LoadUserSkillsService', () => {
  let service: LoadUserSkillsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadUserSkillsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
