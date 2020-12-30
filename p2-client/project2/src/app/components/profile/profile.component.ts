import { Component, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { CookieService } from 'ngx-cookie-service';
import { ProfileToken} from 'src/app/models/profile-token'
import { User } from 'src/app/models/user';
import { UserSkill } from 'src/app/models/user-skill';
import { LoadUserService } from 'src/app/services/load-user';
import { LoadUserSkillsService } from 'src/app/services/load-user-skills.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  private pString : string;
  private pToken : ProfileToken;
  public user:User;
  public skills:Array<UserSkill>;

  constructor(public auth: AuthService, private loadUser:LoadUserService, private cookieService:CookieService,private loadSkills:LoadUserSkillsService) {}

  ngOnInit(): void {
    this.user = new User;
    this.auth.user$.subscribe(
      (profile) => {
        this.pString = JSON.stringify(profile, null, 2); 
        this.pToken = JSON.parse(this.pString);        
      });
    this.loadUser.loadUser(this.cookieService.get('email')).subscribe(user => {
      this.user.email = user.email;
      this.user.firstName = user.firstName;
      this.user.lastName = user.lastName;
      this.user.gitHubAccount = user.gitHubAccount;
      this.user.userID = user.userID;
      this.loadSkills.loadSkills(this.user.userID).subscribe(skills => this.skills = skills);
    });
  }
}