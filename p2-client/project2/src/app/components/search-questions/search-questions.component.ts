import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from 'src/app/models/question';
import { LoadRecentQuestionsService } from 'src/app/services/load-recent-questions.service';
import { QuestionSearchService } from 'src/app/services/question-search.service';

@Component({
  selector: 'app-search-questions',
  templateUrl: './search-questions.component.html',
  styleUrls: ['./search-questions.component.css']
})
export class SearchQuestionsComponent implements OnInit {
  public search : string;
  public results:Array<Question>;
  
  constructor(private route: ActivatedRoute,private router : Router, private searchQuestions : QuestionSearchService, private loadRecent:LoadRecentQuestionsService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => { this.search = params['question']});

    this.loadRecent.loadQuestions().subscribe(data =>
      {
        console.log(data);
        this.results = data;
      });    
  }

  submitSearch():void{
    console.log("Searching for "+this.search);
    this.searchQuestions.loadQuestions(this.search).subscribe(data => this.results = data)
  }
}