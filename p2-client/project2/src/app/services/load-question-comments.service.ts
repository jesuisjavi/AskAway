import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { QuestionComment } from '../models/question-comment';

@Injectable({
  providedIn: 'root'
})
export class LoadQuestionCommentsService {
  private url: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.url = 'http://localhost:8080/p2/api/comments/questionComments/';
  }

  ngOnInit(): void {
  }

  public loadComments(questionID:number): Observable<Array<QuestionComment>> {
    return this.http.get<Array<QuestionComment>>(this.url+questionID);
  }
}
