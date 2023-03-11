import {Injectable} from "@angular/core";
import {map, Observable, of} from "rxjs";
import {ConfigurationService} from "./configuration.service";
import {AppConfiguration} from "../model/app-configuration";
import {HttpClient, HttpParams} from "@angular/common/http";
import {SecurityUtils} from "../helpers/security-utils";
import {Completions} from "../model/responses";

@Injectable()
export class AutoCompleteService {

  private appConfiguration: AppConfiguration;

  constructor(private configurationService: ConfigurationService,
              private httpClient: HttpClient) {
    this.appConfiguration = configurationService.appConfiguration.get();
  }

  public getCompletions(partial: string): Observable<Completions> {
    let httpParams = new HttpParams().set("partial", partial);
    let secureParams = SecurityUtils.sanitizeHttpParams(httpParams);
    let url: string = new URL('completions', this.appConfiguration.autoCompleteServiceBase).href;
    return this.httpClient.get(url, {params: secureParams})
      .pipe(
        map(response => response as Completions)
      );
  }

  public getTrending(): Observable<string[]> {
    return of([]);
  }

}
