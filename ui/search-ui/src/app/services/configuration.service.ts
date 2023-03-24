import {Injectable} from "@angular/core";
import {catchError, map, Observable, of, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AppConfiguration} from "../model/app-configuration";
import {Optional} from "../model/optional";

@Injectable()
export class ConfigurationService {

  private _appConfiguration: Optional<AppConfiguration> = new Optional<AppConfiguration>();

  constructor(private httpClient: HttpClient) {}

  load(): Observable<any> {
    return this.httpClient.get('assets/config.json')
      .pipe(
        map((response: any) => {
          return this.wrapWithOptional(response);
        }),
        catchError(() => {
          console.error("Unable to fetch configuration");
          return of(new Optional<AppConfiguration>());
        })
      );
  }

  private wrapWithOptional(response: any): Optional<AppConfiguration> {
    this._appConfiguration.set(response as AppConfiguration);
    return this._appConfiguration;
  }

  get appConfiguration(): Optional<AppConfiguration> {
    return this._appConfiguration;
  }
}
