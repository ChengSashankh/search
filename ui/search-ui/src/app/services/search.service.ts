import {Injectable} from "@angular/core";
import {ConfigurationService} from "./configuration.service";
import {AppConfiguration} from "../model/app-configuration";
import {map, Observable} from "rxjs";
import {Completions, PageInfo, SearchResults} from "../model/responses";
import {HttpClient, HttpParams} from "@angular/common/http";
import {SecurityUtils} from "../helpers/security-utils";
import {VectorSearchResults} from "../model/responses/search-results";

@Injectable()
export class SearchService {

  private appConfiguration: AppConfiguration;

  constructor(private configurationService: ConfigurationService,
              private httpClient: HttpClient) {
    this.appConfiguration = configurationService.appConfiguration.get();
  }

  public getSearchResults(query: string, pageInfo: PageInfo): Observable<VectorSearchResults> {
    let httpParams = new HttpParams().set("q", query.replace(" ", ","))
      // .set("pageNum", pageInfo.pageNum)
      // .set("pageSize", pageInfo.itemsPerPage);
    let secureParams = SecurityUtils.sanitizeHttpParams(httpParams);
    let url: string = new URL('search', this.appConfiguration.searchServiceBase).href;

    return this.httpClient.get(url, {params: secureParams})
      .pipe(map(response => response as VectorSearchResults));
  }

}
