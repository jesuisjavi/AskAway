import { Component, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { CookieService } from 'ngx-cookie-service';  
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user';
import { LoadUserService } from 'src/app/services/load-user';
import { Router } from '@angular/router';
import { LoadRecentQuestionsService } from 'src/app/services/load-recent-questions.service';
import { Question } from 'src/app/models/question';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  public pString: string = "";
  private pToken : any;
  public user : User;
  public recent:Array<Question> = new Array;
  public count: number;
  public question : string;
  
  constructor(public auth: AuthService, private cookieService: CookieService, private loadUser : LoadUserService, private router : Router, private loadRecentQuestionsService:LoadRecentQuestionsService) {}

  ngOnInit(): void {
    if (!(this.cookieService.get('isLoggedIn') == 'true')){
      this.auth.user$.subscribe(
        (profile) => {        
          console.log(profile);
          this.pString = JSON.stringify(profile, null, 2); 
          console.log(this.pString);
          this.pToken = JSON.parse(this.pString);
          console.log(this.pToken.email);
          console.log(this.cookieService);
          this.cookieService.set('isLoggedIn', "true");
          this.cookieService.set('email', this.pToken.email);
          this.cookieService.set('name', this.pToken.given_name);
          this.ValidateUser(this.pToken.email);
        });        
    }
    
    this.count=0;
    this.loadRecentQuestionsService.loadQuestions().subscribe(
      question =>{
        if(question.length>4){
          for(let i = 0; i<4; i++){
            this.recent.push(question[i]);
          }
        }
        else{
          this.recent = question;
        }
      }
    )

  } 

  public ValidateUser(email:string): void {    
    this.loadUser.loadUser(email).subscribe(data =>
      {
        console.log(data);
          if (data == null){
            this.router.navigate(['create-profile']);
          }
          else{
              this.user = data;
              this.cookieService.set('userId', this.user.userID.toString());
          }
      });
  }  

  public RedirectToSearch() : void{
    this.router.navigate(['search-questions'], {queryParams : {question : this.question}});
  }

}
