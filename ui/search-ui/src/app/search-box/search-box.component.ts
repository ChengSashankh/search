import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {debounceTime, distinctUntilChanged, map, Observable, of} from "rxjs";
import {AutoCompleteService} from "../services/auto-complete.service";
import {Completions} from "../model/responses";
import {MatAutocompleteActivatedEvent} from "@angular/material/autocomplete";
import {Optional} from "../model/optional";
import {Router} from "@angular/router";

@Component({
  selector: 'app-search-box',
  templateUrl: './search-box.component.html',
  styleUrls: ['./search-box.component.scss']
})
export class SearchBoxComponent implements OnInit {
  @Input() initialSearch: string = '';
  @Output() searched: EventEmitter<string> = new EventEmitter<string>();

  form: FormGroup;
  suggestions: Observable<string[]>;
  overriddenSearchTerm = new Optional<string>();
  suggestionActive: boolean = false;

  constructor(private autoCompleteService: AutoCompleteService,
              private router: Router) {
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
        debounceTime(200),
        distinctUntilChanged()
      )
      .subscribe((searchTerm: string) => {
        this.searched.emit(searchTerm);
        this.overriddenSearchTerm.set(searchTerm);
        this.suggestionActive = false;
        if (searchTerm.trim() === '') {
          this.suggestions = of([]);
        } else {
          this.suggestions = this.getAutoComplete(searchTerm.trim().toLowerCase());
        }
      });
    this.form.get('searchControl')?.setValue(this.initialSearch);
  }

  getTrendingSearches(): Observable<string[]> {
    return of(['Trending Search 1', 'Trending Search 2']);
  }

  getAutoComplete(searchTerm: string): Observable<string[]> {
    return this.autoCompleteService.getCompletions(searchTerm)
      .pipe(
        map((completions: Completions) => completions.completions)
      );
  }

  search(form: FormGroup) {
    this.router.navigate(['results'], { queryParams: { "q": form.get('searchControl')?.value }})
  }

  suggestionActivated($event: MatAutocompleteActivatedEvent) {
    let selectedOption = $event.option?.value;
    let control = this.form.get('searchControl');
    control?.patchValue(selectedOption, {emitEvent: false});
    this.suggestionActive = true;
  }

  suggestionsClosed() {
    this.form.get('searchControl')?.patchValue(this.overriddenSearchTerm.getOrElse(''), {emitEvent: false});
    this.suggestionActive = false;
  }
}
