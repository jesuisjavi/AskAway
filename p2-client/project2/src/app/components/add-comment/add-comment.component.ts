import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AddComment } from 'src/app/models/add-comment';
import { Question } from 'src/app/models/question';
import { AddCommentService } from 'src/app/services/add-comment.service';
import { LoadQuestionService } from 'src/app/services/load-question.service';

@Component({
  selector: 'app-add-comment',
  templateUrl: './add-comment.component.html',
  styleUrls: ['./add-comment.component.css']
})
export class AddCommentComponent implements OnInit {
  public question : Question;
  public comment : AddComment;
  
  constructor(private router : Router, private route:ActivatedRoute, private addComment : AddCommentService, private cookieService: CookieService, private loadQuestionService:LoadQuestionService) { }

  ngOnInit(): void {
    this.comment = new AddComment;
    this.question = new Question;
    this.comment.userID = String(this.cookieService.get('userId'));
    this.route
    .queryParams
    .subscribe(params => {
      this.comment.questionID = params['questionID'];
      console.log(this.comment.questionID)
    });
    this.loadQuestionService.loadQuestion(this.comment.questionID).subscribe(
      q => {
        console.log(q)
        this.question.title = q.title;
        this.question.details = q.details;
        this.question.user = q.user;
        this.question.skills = q.skills;
      });
  }

  onSubmit(): void{
    this.addComment.addComment(this.comment).subscribe(result => {
      console.log(result);
    });  
    this.RedirectToViewQuestion();
  }

  public RedirectToViewQuestion() : void {
    this.router.navigate(['/view-question'], { queryParams : { id : this.comment.questionID } });
  }
}
