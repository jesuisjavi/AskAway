import { TestBed } from '@angular/core/testing';

import { LoadLeaderboardService } from './load-leaderboard.service';

describe('LoadLeaderboardService', () => {
  let service: LoadLeaderboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadLeaderboardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
