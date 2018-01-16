import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentSpeechByDateComponent } from './student-speech-by-date.component';

describe('StudentSpeechByDateComponent', () => {
  let component: StudentSpeechByDateComponent;
  let fixture: ComponentFixture<StudentSpeechByDateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentSpeechByDateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentSpeechByDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
