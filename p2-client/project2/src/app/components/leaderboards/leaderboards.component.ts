import { Component, OnInit } from '@angular/core';
import { Leader } from 'src/app/models/leader';
import { LoadLeaderboardService } from 'src/app/services/load-leaderboard.service';

@Component({
  selector: 'app-leaderboards',
  templateUrl: './leaderboards.component.html',
  styleUrls: ['./leaderboards.component.css']
})
export class LeaderboardsComponent implements OnInit {
  public leaders:Array<Leader>;

  constructor(private loadLeader:LoadLeaderboardService) { 
    
  }

  ngOnInit(): void {
    this.leaders = new Array<Leader>();
    this.loadLeader.loadLeaders().subscribe(
      data => {
        this.leaders = data;
      });
  }

  ngOnChanges(): void{
    console.log("Updating leaders")
    this.loadLeader.loadLeaders().subscribe(
      data => {
        this.leaders = data;
      });
  }
}
