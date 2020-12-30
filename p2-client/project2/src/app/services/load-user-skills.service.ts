import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { UserSkill } from '../models/user-skill';

@Injectable({
  providedIn: 'root'
})
export class LoadUserSkillsService {
  private url: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.url = 'http://localhost:8080/p2/api/users/userskills/';
  }

  ngOnInit(): void {
  }

  public loadSkills(userID:number): Observable<Array<UserSkill>> {
    return this.http.get<Array<UserSkill>>(this.url+userID);
  }
}
