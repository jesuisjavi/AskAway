import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { Question } from '../models/question';

@Injectable({
  providedIn: 'root'
})
export class LoadQuestionService {
  private url: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.url = 'http://localhost:8080/p2/api/questions/';
  }

  ngOnInit(): void {
  }

  public loadQuestion(questionId:string): Observable<Question> {
    //let params = new HttpParams();
    //params = params.append('questionId', questionId.toString());
    console.log(this.url+questionId)
    return this.http.get<Question>(this.url+questionId/*, { params: params }*/ );       
  }
}
