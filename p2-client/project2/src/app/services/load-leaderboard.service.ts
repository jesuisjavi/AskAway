import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { Leader } from '../models/leader';

@Injectable({
  providedIn: 'root'
})
export class LoadLeaderboardService {
  private url: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.url = 'http://localhost:8080/p2/api/users/leaders';
  }

  ngOnInit(): void {
  }

  public loadLeaders(): Observable<Array<Leader>> {
    return this.http.get<Array<Leader>>(this.url);
  }
}
