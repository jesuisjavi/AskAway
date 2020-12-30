import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { Question } from "src/app/models/question";

@Injectable({
  providedIn: 'root'
})
export class QuestionSearchService {

  private usersUrl: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.usersUrl = 'http://localhost:8080/p2/api/questions/query';
  }

  ngOnInit(): void {
  }

  public loadQuestions(query:string): Observable<Question[]> {
    let params = new HttpParams();
    params = params.append('query',query);
    return this.http.get<Question[]>(this.usersUrl, { params: params } );    
  }
}