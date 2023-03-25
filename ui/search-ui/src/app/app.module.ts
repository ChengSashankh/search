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
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
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
import {AuthGuard, AuthHttpInterceptor, AuthModule} from "@auth0/auth0-angular";
import { LoginComponent } from './login/login.component';
import { environment } from './environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    SearchBoxComponent,
    SearchResultsComponent,
    SearchHomeComponent,
    PostingPreviewComponent,
    LoginComponent
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
      {path: '', component: LoginComponent},
      {path: 'search', component: SearchHomeComponent, canActivate: [ AuthGuard ]},
      {path: 'results', component: SearchResultsComponent, canActivate: [ AuthGuard ]}
    ]),
    MatCardModule,
    MatProgressBarModule,
    MatListModule,
    MatChipsModule,
    MatPaginatorModule,
    AuthModule.forRoot({
      domain: environment.authDomain,
      clientId: environment.clientId,
      authorizationParams: {
        redirect_uri: `${window.location.origin}/search`,
        audience: environment.audience,
        scope: environment.scope
      },
      httpInterceptor: {
        allowedList: [
          {
            uri: environment.interceptorAllowedUri,
            tokenOptions: {
              authorizationParams: {
                audience: environment.audience,
                scope: environment.scope
              }
            }
          }
        ]
      }
    })
  ],
  providers: [
    ConfigurationService,
    AutoCompleteService,
    SearchService,
    { provide: APP_INITIALIZER, useFactory: (config: ConfigurationService) => () => config.load(), deps: [ConfigurationService], multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
