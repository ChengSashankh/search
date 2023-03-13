import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostingPreviewComponent } from './posting-preview.component';

describe('ResultPreviewComponent', () => {
  let component: PostingPreviewComponent;
  let fixture: ComponentFixture<PostingPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostingPreviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostingPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
