import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Answer } from 'src/app/models/answer';
import { AnswerQuestion } from 'src/app/models/answer-question';
import { Question } from 'src/app/models/question';
import { AnswerQuestionService } from 'src/app/services/answer-question.service';
import { LoadQuestionService } from 'src/app/services/load-question.service';

@Component({
  selector: 'app-answer-question',
  templateUrl: './answer-question.component.html',
  styleUrls: ['./answer-question.component.css']
})
export class AnswerQuestionComponent implements OnInit {
  public details:string;
  public answer:AnswerQuestion;
  public question:Question;

  constructor(private router : Router,private route:ActivatedRoute,private answerQuestion : AnswerQuestionService, private cookieService: CookieService,private loadQuestionService:LoadQuestionService) { }

  ngOnInit(): void {
    this.answer = new AnswerQuestion;
    this.question = new Question;
    this.answer.userID = this.cookieService.get('userId');
    console.log(this.cookieService.get('userId'));
    this.route
    .queryParams
    .subscribe(params => {
      this.answer.questionID = params['questionID'];
      console.log(this.answer.questionID)
    });
    this.loadQuestionService.loadQuestion(this.answer.questionID).subscribe(
      q => {
        console.log(q)
        this.question.title = q.title;
        this.question.details = q.details;
        this.question.user = q.user;
        this.question.skills = q.skills;
      });
    console.log(this.answer);
    console.log(this.question);

  }

  onSubmit() {
    console.log("Submitting");
    this.answer.details = this.details;
    this.answerQuestion.addAnswer(this.answer).subscribe(result => console.log(result));    
    this.RedirectToViewQuestion(); 
  }

  public RedirectToViewQuestion() : void {
    this.router.navigate(['view-question'], { queryParams : { id : this.answer.questionID } });
  }

}
