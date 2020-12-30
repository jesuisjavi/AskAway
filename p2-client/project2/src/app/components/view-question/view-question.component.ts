import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from 'src/app/models/question';
import { QuestionComment } from 'src/app/models/question-comment';
import { LoadAnswersService } from 'src/app/services/load-answers.service';
import { LoadQuestionCommentsService } from 'src/app/services/load-question-comments.service';
import { LoadQuestionService } from 'src/app/services/load-question.service';

@Component({
  selector: 'app-view-question',
  templateUrl: './view-question.component.html',
  styleUrls: ['./view-question.component.css']
})
export class ViewQuestionComponent implements OnInit {
  public id:number;
  public model:Question;
  public comments:Array<QuestionComment>;

  constructor(private route: ActivatedRoute, private router : Router, private loadQuestionService:LoadQuestionService, private loadQuestionCommentsService:LoadQuestionCommentsService,private loadAnswersService:LoadAnswersService) {
    console.log('Called Constructor');
    this.route.queryParams.subscribe(params =>{
      this.id = params['id'];
    })
   }

  ngOnInit(): void {
    this.loadQuestionService.loadQuestion(this.id.toString()).subscribe(data => {
      this.model = data;
      console.log(this.model);
      if(this.model.questionID!=undefined){
        let id = this.model.questionID;
        this.loadQuestionCommentsService.loadComments(id).subscribe(comments => {
          this.comments = comments;
          console.log("Comments"+comments);
        })
        this.loadAnswersService.loadAnswers(id).subscribe(answers => {
          this.model.answers = answers;
          console.log("Answers "+answers);
        })
      }
      }
    )
  }

  public AnswerQuestion() : void{    
      this.router.navigate(['answer-question'], { queryParams : { questionID:this.model.questionID } });    
  }

  public AddComment() : void {
      this.router.navigate(['add-comment'], { queryParams : { questionID:this.model.questionID  } });
  }
}
