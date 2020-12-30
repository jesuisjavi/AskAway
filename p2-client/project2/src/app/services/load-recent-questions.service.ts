import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs/internal/Observable';
import { Question } from '../models/question';

@Injectable({
  providedIn: 'root'
})
export class LoadRecentQuestionsService {
  private usersUrl: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.usersUrl = 'http://localhost:8080/p2/api/questions/recent';
  }

  ngOnInit(): void {
  }

  public loadQuestions(): Observable<Array<Question>> {
    return this.http.get<Array<Question>>(this.usersUrl);
  }
}
