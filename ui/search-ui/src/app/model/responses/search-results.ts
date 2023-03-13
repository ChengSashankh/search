import {PageInfo} from "./common";

export interface SearchResults {
  query: string;
  results: Posting[];
  pageInfo: PageInfo;
}

export interface Posting {
  title: string;
  summary: string;
  matchingSubstring: string[];
  href: string;
  rank?: number;
}
