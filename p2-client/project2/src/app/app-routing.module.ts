import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LeaderboardsComponent } from './components/leaderboards/leaderboards.component';
import { PostQuestionComponent } from './components/post-question/post-question.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SearchQuestionsComponent } from './components/search-questions/search-questions.component';
import { ViewQuestionComponent } from './components/view-question/view-question.component';
import { AuthGuard } from '@auth0/auth0-angular';
import { CreateProfileComponent } from './components/create-profile/create-profile.component';
import { AnswerQuestionComponent } from './components/answer-question/answer-question.component';
import { AddCommentComponent } from './components/add-comment/add-comment.component';

const routes: Routes = [
  {path: "",component: DashboardComponent},
  {path: "post-question",component: PostQuestionComponent/*, canActivate: [AuthGuard]*/},
  {path: "view-question",component:ViewQuestionComponent},
  {path: "leaderboards", component:LeaderboardsComponent},
  {path: "search-questions",component: SearchQuestionsComponent},
  { path: 'create-profile', component: CreateProfileComponent },
  { path: 'profile', component: ProfileComponent },
  {path:'answer-question',component: AnswerQuestionComponent},
  {path:'add-comment',component: AddCommentComponent},
  {path: "*",component: DashboardComponent}, //We should create a 404 page
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
