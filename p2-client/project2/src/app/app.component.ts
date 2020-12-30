import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Ask Away';

  constructor(private cookerService: CookieService){}

  ngOnInit(){
    if(!this.cookerService.check('isLoggedIn')){
      this.cookerService.set('isLoggedIn','false');
    }
  }
}
