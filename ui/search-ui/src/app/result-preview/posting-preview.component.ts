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

  longText = `The Shiba Inu is the smallest of the six original and distinct spitz breeds of dog
  from Japan. A small, agile dog that copes very well with mountainous terrain, the Shiba Inu was
  originally bred for hunting.`;

  ngOnInit(): void {

  }

  cardClicked() {
    this.postingClicked
      .emit(this.posting);
  }
}
