import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Question } from 'src/app/models/question';
import { AddQuestionService } from 'src/app/services/add-question.service';
import { DatePipe } from '@angular/common';
import { PostQuestion } from 'src/app/models/post-question';
import {FormsModule} from '@angular/forms';
import { GetSkillsService } from 'src/app/services/get-skills.service';
import { LoadUserService } from 'src/app/services/load-user';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-post-question',
  templateUrl: './post-question.component.html',
  styleUrls: ['./post-question.component.css']
})
export class PostQuestionComponent implements OnInit {
  public model: PostQuestion;
  public skills: string[];

  constructor(private router: Router, private addQuestion : AddQuestionService, private cookieService: CookieService, private skillsService: GetSkillsService,private loadUser:LoadUserService) { }

  ngOnInit(): void {
    this.skills = this.getSkills();
    this.model = new PostQuestion();
    this.model.userID = this.cookieService.get('userId');
  }

  getSkills(): string[]{
    this.skillsService.getSkills().subscribe(data =>
      {
        this.skills = data.map(e => e.name);
        console.log(this.skills);
        return this.skills;
      });
      return [];
  }

  onSubmit() {
    console.log("Submitting");
    this.addQuestion.addQuestion(this.model).subscribe(result => console.log(result));    
    this.router.navigate(['']);
  }
}
