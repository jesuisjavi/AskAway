import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PostQuestionComponent } from './components/post-question/post-question.component';
import { ViewQuestionComponent } from './components/view-question/view-question.component';
import { LeaderboardsComponent } from './components/leaderboards/leaderboards.component';
import { SearchQuestionsComponent } from './components/search-questions/search-questions.component';
import { AuthModule } from '@auth0/auth0-angular';
import { environment as env } from '../environments/environment';
import {LoginComponent} from './components/login/login.component'
import { LogoutComponent } from './components/logout/logout.component';
import { AuthenticationComponent } from './components/authentication/authentication.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CreateProfileComponent } from './components/create-profile/create-profile.component';
import {HttpClientModule} from '@angular/common/http';
import { AnswerQuestionComponent } from './components/answer-question/answer-question.component';
import { AddCommentComponent } from './components/add-comment/add-comment.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    PostQuestionComponent,
    ViewQuestionComponent,
    SearchQuestionsComponent,
    LoginComponent,
    LeaderboardsComponent,
    LogoutComponent,
    AuthenticationComponent,
    ProfileComponent,
    CreateProfileComponent,
    AnswerQuestionComponent,
    AddCommentComponent
  ],
  imports: [
    HttpClientModule,
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    AuthModule.forRoot({
      ...env.auth,
    }),
  ],
  providers: [HttpClientModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
