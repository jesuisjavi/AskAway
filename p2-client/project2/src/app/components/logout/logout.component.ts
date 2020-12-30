import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styles: [],
})
export class LogoutComponent implements OnInit {
  constructor(private cookieService: CookieService, 
    public auth: AuthService,
    @Inject(DOCUMENT) private doc: Document
  ) {}

  ngOnInit(): void {
    console.log("Logged in")
  }

  logout(): void {        
    this.cookieService.deleteAll('/');    
    this.auth.logout({ returnTo: "http://localhost:4200/" });
  }
}
