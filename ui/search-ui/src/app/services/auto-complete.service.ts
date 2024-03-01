import {Injectable} from "@angular/core";
import {map, Observable, of} from "rxjs";
import {ConfigurationService} from "./configuration.service";
import {AppConfiguration} from "../model/app-configuration";
import {HttpClient, HttpParams} from "@angular/common/http";
import {SecurityUtils} from "../helpers/security-utils";
import {Completions} from "../model/responses";
import {TemporaryCompletions} from "../model/responses/completions";

@Injectable()
export class AutoCompleteService {

  private appConfiguration: AppConfiguration;

  constructor(private configurationService: ConfigurationService,
              private httpClient: HttpClient) {
    this.appConfiguration = configurationService.appConfiguration.get();
  }

  public getCompletions(partial: string): Observable<TemporaryCompletions> {
    let httpParams = new HttpParams().set("query", partial);
    let secureParams = SecurityUtils.sanitizeHttpParams(httpParams);
    let url: string = new URL('complete', this.appConfiguration.autoCompleteServiceBase).href;
    return this.httpClient.get(url, {params: secureParams})
      .pipe(
        map(response => response as TemporaryCompletions)
      );
  }

  public getTrending(): Observable<string[]> {
    return of([]);
  }

}
