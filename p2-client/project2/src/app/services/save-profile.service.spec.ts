import { TestBed } from '@angular/core/testing';

import { SaveProfileService } from './save-profile.service';

describe('SaveProfileService', () => {
  let service: SaveProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SaveProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
