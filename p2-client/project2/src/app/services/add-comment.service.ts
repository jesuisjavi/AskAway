import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { AddComment } from '../models/add-comment';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class AddCommentService {
  private usersUrl: string;

  constructor(private http: HttpClient, public auth: AuthService, private cookieService: CookieService) {
    this.usersUrl = 'http://localhost:8080/p2/api/comments/addComment';
  }

  ngOnInit(): void {
  }

  public addComment(comment:AddComment): Observable<any> {
    return this.http.post<any>(this.usersUrl, comment);
  }
}
