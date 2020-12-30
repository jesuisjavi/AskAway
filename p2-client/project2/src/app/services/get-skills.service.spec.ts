import { TestBed } from '@angular/core/testing';

import { GetSkillsService } from './get-skills.service';

describe('GetSkillsService', () => {
  let service: GetSkillsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetSkillsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
