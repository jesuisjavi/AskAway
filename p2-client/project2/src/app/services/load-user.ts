import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { AuthService } from '@auth0/auth0-angular';

//Loads a User with their email
@Injectable({
  providedIn: 'root'
})
export class LoadUserService {
  private usersUrl: string;

  constructor(private http: HttpClient, public auth: AuthService) {
    this.usersUrl = 'http://localhost:8080/p2/api/users/loadUser';
  }

  ngOnInit(): void {
  }

  public loadUser(email:string): Observable<User> {
    return this.http.post<User>(this.usersUrl, email);
  }
}
