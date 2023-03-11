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
@NgModule({
  declarations: [
    AppComponent,
    SearchBoxComponent
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
    BrowserAnimationsModule
  ],
  providers: [
    ConfigurationService,
    AutoCompleteService,
    { provide: APP_INITIALIZER, useFactory: (config: ConfigurationService) => () => config.load(), deps: [ConfigurationService], multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
