
export class AppConfiguration {
  autoCompleteServiceBase: string;
  searchServiceBase: string;
  trackingServiceBase: string;

  constructor(autoCompleteServiceBase: string, searchServiceBase: string, trackingServiceBase: string) {
    this.autoCompleteServiceBase = autoCompleteServiceBase;
    this.searchServiceBase = searchServiceBase;
    this.trackingServiceBase = trackingServiceBase;
  }
}
