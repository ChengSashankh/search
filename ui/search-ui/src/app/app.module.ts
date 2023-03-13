import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatToolbarModule} from "@angular/material/toolbar";
import { SearchBoxComponent } from './search-box/search-box.component';
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {ConfigurationService} from "./model";
import {AutoCompleteService} from "./services/auto-complete.service";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { SearchResultsComponent } from './search-results/search-results.component';
import { SearchHomeComponent } from './search-home/search-home.component';
import {RouterModule} from "@angular/router";
import {SearchService} from "./services/search.service";
import { PostingPreviewComponent } from './result-preview/posting-preview.component';
import {MatCardModule} from "@angular/material/card";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatListModule} from "@angular/material/list";
import {MatChipsModule} from "@angular/material/chips";
import {MatPaginatorModule} from "@angular/material/paginator";
@NgModule({
  declarations: [
    AppComponent,
    SearchBoxComponent,
    SearchResultsComponent,
    SearchHomeComponent,
    PostingPreviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatToolbarModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatAutocompleteModule,
    HttpClientModule,
    BrowserAnimationsModule,
    BrowserModule,
    RouterModule.forRoot([
      {path: '', component: SearchHomeComponent},
      {path: 'results', component: SearchResultsComponent}
    ]),
    MatCardModule,
    MatProgressBarModule,
    MatListModule,
    MatChipsModule,
    MatPaginatorModule
  ],
  providers: [
    ConfigurationService,
    AutoCompleteService,
    SearchService,
    { provide: APP_INITIALIZER, useFactory: (config: ConfigurationService) => () => config.load(), deps: [ConfigurationService], multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
