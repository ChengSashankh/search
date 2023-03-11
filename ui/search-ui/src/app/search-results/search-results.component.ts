import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {SearchService} from "../services/search.service";
import {Posting, SearchResults} from "../model/responses";
import {Optional} from "../model/optional";

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.scss']
})
export class SearchResultsComponent implements OnInit {

  search: string = '';
  postings: Posting[];
  searchResults: Optional<SearchResults>;

  constructor(private route: ActivatedRoute, private searchService: SearchService) {
    this.postings = [];
    this.searchResults = new Optional<SearchResults>();
  }

  ngOnInit(): void {
    this.search = this.route.snapshot.queryParamMap.has('q') ? this.route.snapshot.queryParamMap.get('q') as string : '';
    this.getSearchResults();
  }

  getSearchResults(): void {
    this.searchService.getSearchResults(this.search)
      .subscribe({
        next: (searchResults: SearchResults) => {
          this.searchResults.set(searchResults);
          this.postings = searchResults.results;
        },
        error: (err: any) => console.log(err)
      })

  }
}
