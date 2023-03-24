import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Posting} from "../model/responses";

@Component({
  selector: 'app-result-preview',
  templateUrl: './posting-preview.component.html',
  styleUrls: ['./posting-preview.component.scss']
})
export class PostingPreviewComponent implements OnInit {

  @Input() posting: Posting = {href: "", matchingSubstring: [], summary: "", title: ""};
  @Output() postingClicked = new EventEmitter<Posting>();

  ngOnInit(): void {

  }

  cardClicked() {
    this.postingClicked
      .emit(this.posting);
  }
}
