import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { SaveProfile } from '../models/save-profile';

@Injectable({
  providedIn: 'root'
})
export class SaveProfileService {
  private url: string;

  constructor(private http: HttpClient, public auth: AuthService, private cookieService: CookieService) {
    this.url = 'http://localhost:8080/p2/api/users/updateUser';
  }

  ngOnInit(): void {
  }

  public updateUser(profile:SaveProfile): Observable<String> {
    return this.http.post<String>(this.url, profile);
  }
}
