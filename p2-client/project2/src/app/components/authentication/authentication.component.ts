import { Component, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styles: [],
})
export class AuthenticationComponent implements OnInit {
  public isLoggedIn : string;

  constructor(public auth: AuthService, private cookieService: CookieService) {}

  ngOnInit(): void {
      this.isLoggedIn = this.cookieService.get('isLoggedIn');      
  }
}
