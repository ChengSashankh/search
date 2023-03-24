import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-search-home',
  templateUrl: './search-home.component.html',
  styleUrls: ['./search-home.component.scss']
})
export class SearchHomeComponent  {

  constructor(private router: Router) {
  }

  searchTriggered($event: any) {
    this.router.navigate(['results'], { queryParams: { "q": $event }})
  }
}
