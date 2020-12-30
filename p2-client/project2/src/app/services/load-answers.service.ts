import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Answer } from '../models/answer';

@Injectable({
  providedIn: 'root'
})
export class LoadAnswersService {
  private url: string;

  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080/p2/api/answers/';
  }

  ngOnInit(): void {
  }

  public loadAnswers(questionID:number): Observable<Array<Answer>> {
    return this.http.get<Array<Answer>>(this.url+questionID);
  }
}
