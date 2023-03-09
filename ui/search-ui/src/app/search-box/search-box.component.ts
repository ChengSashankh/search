import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {debounce, debounceTime, distinctUntilChanged, filter, map, Observable, of, startWith} from "rxjs";

@Component({
  selector: 'app-search-box',
  templateUrl: './search-box.component.html',
  styleUrls: ['./search-box.component.scss']
})
export class SearchBoxComponent implements OnInit {
  form: FormGroup;
  suggestions: Observable<string[]>;

  constructor() {
    this.suggestions = of([]);
    this.form = new FormGroup({
      searchControl: new FormControl<string>({
        value: '',
        disabled: false
      })
    });
  }

  ngOnInit(): void {
    this.form.get('searchControl')?.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      )
      .subscribe((searchTerm: string) => {
        if (searchTerm.trim() === '') {
          this.suggestions = this.getTrendingSearches();
        } else {
          this.suggestions = this.getAutoComplete(searchTerm.trim().toLowerCase());
        }
      });
    this.form.get('searchControl')?.setValue('');
  }

  getTrendingSearches(): Observable<string[]> {
    return of(['Trending Search 1', 'Trending Search 2']);
  }

  getAutoComplete(searchTerm: string): Observable<string[]> {
    return of(['first', 'second', 'third'])
      .pipe(
        map(suggestions => suggestions.filter(suggestion => suggestion.toLowerCase().startsWith(searchTerm)))
      );
  }

  search(form: FormGroup) {
    window.alert("Searched")
  }
}
