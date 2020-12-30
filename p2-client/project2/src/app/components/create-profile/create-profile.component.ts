import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { SaveProfile } from 'src/app/models/save-profile';
import { User } from 'src/app/models/user';
import { LoadUserService } from 'src/app/services/load-user';
import { SaveProfileService } from 'src/app/services/save-profile.service';

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html',
  styleUrls: ['./create-profile.component.css']
})
export class CreateProfileComponent implements OnInit {

  public model:SaveProfile;
  constructor(private router: Router ,private cookeieService:CookieService,private saveProfile:SaveProfileService, private loadUser:LoadUserService) { }

  ngOnInit(): void {
    this.model = new SaveProfile();
    if(this.cookeieService.check('email')){
      this.loadUser.loadUser(this.cookeieService.get('email')).subscribe( data => {
        this.model.firstName = data.firstName;
        this.model.lastName = data.lastName;
        this.model.gitHub = data.gitHubAccount;
        this.model.email = data.email;
      });
    }
    console.log(this.model)
  }

  onSubmit() {
    console.log("Submitting");
    if(this.cookeieService.check('email')){
      this.model.email=this.cookeieService.get('email');
      this.saveProfile.updateUser(this.model).subscribe(result => console.log(result));
      this.router.navigate(['/profile']); 
    }
  }

}
