import {PageInfo} from "./common";

export interface Completions {
  partial: string;
  completions: string[];
  pageInfo?: PageInfo;
}

export interface TemporaryCompletions {
  query: string;
  completions: string[];
}
