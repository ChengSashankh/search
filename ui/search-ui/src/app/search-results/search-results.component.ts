import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {SearchService} from "../services/search.service";
import {PageInfo, Posting, SearchResults} from "../model/responses";
import {Optional} from "../model/optional";
import {PageEvent} from "@angular/material/paginator";
import {VectorSearchResults} from "../model/responses/search-results";

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.scss']
})
export class SearchResultsComponent implements OnInit {

  search: string = '';
  postings: Posting[];
  searchResults: Optional<VectorSearchResults>;
  pageInfo: PageInfo = {itemsPerPage: 10, pageNum: 0};

  @ViewChild("resultRef") resultRef!: ElementRef;

  loading: boolean = false;
  error: boolean = false;

  constructor(private route: ActivatedRoute,
              private searchService: SearchService) {
    this.postings = [];
    this.searchResults = new Optional<SearchResults>();
  }

  ngOnInit(): void {
    this.search = this.route.snapshot.queryParamMap.has('q') ? this.route.snapshot.queryParamMap.get('q') as string : '';
    this.getSearchResults(this.search);
  }

  getSearchResults($event: any): void {
    this.search = $event;
    this.loading = true;
    this.searchService.getSearchResults(this.search, this.pageInfo)
      .subscribe({
        next: (searchResults: VectorSearchResults) => {
          this.searchResults.set(searchResults);
          this.postings = searchResults.results.map(r => {
            let words = r.content?.split(" ");
            return {title: r.title, summary: words?.splice(0, 100).join(" ") + ((words && words.length > 100) ? "..." : ""), href: `https://en.wikipedia.org/wiki/${r.title.replace(" ", "_")}`} as Posting;
          });
          // this.pageInfo = searchResults.pageInfo;
          this.loading = false;
          this.error = false;
          this.resultRef.nativeElement.scrollTo(0, 0);
        },
        error: (err: any) => {
          this.loading = false;
          this.error = true;
        }
      })

  }

  openResultExternal(posting: Posting) {
    window.open(posting.href, "_blank");
  }

  onPaginationChanged($event: PageEvent) {
    // this.pageInfo = {
    //   pageNum: $event.pageIndex,
    //   itemsPerPage: $event.pageSize
    // };

    this.getSearchResults(this.search);
  }
}
