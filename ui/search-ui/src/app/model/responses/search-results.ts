import {PageInfo} from "./common";

export interface VectorSearchResults {
  results: Doc[];
}

export interface Doc {
  content?: string;
  title: string;
}

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

export interface LeanPostings {
  results: number[];
}
