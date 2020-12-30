import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Question } from '../models/question';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { PostQuestion } from '../models/post-question';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AddQuestionService {
  private usersUrl: string;

  constructor(private http: HttpClient, public auth: AuthService, private cookieService: CookieService) {
    this.usersUrl = 'http://localhost:8080/p2/api/questions/addQuestion';
  }

  ngOnInit(): void {
  }

  public addQuestion(question:PostQuestion): Observable<PostQuestion> {
    return this.http.post<PostQuestion>(this.usersUrl, question);
  }
}
