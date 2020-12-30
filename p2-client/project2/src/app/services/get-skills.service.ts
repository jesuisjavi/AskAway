import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { Skill } from '../models/skill';

@Injectable({
  providedIn: 'root'
})
export class GetSkillsService {
  private skillsUrl: string;

  constructor(private http: HttpClient) {
    this.skillsUrl = 'http://localhost:8080/p2/api/skills';
  }

  ngOnInit(): void {
  }

  public getSkills(): Observable<Skill[]> {
    return this.http.get<Skill[]>(this.skillsUrl);
    // Send email to p2-server and determine from response whether or not they exist
    // How to send email back to service? How to redirect of they don't exist?
  }
}
