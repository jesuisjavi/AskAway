import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Answer } from '../models/answer';
import { AnswerQuestion } from '../models/answer-question';

@Injectable({
  providedIn: 'root'
})
export class AnswerQuestionService {
  private usersUrl: string;

  constructor(private http: HttpClient) { 
    this.usersUrl = 'http://localhost:8080/p2/api/answers/addAnswer';
  }

  public addAnswer(answer:AnswerQuestion): Observable<AnswerQuestion> {
    return this.http.post<AnswerQuestion>(this.usersUrl, answer);
  }
}
