import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpeakersStudentTableComponent } from './speakers-student-table.component';

describe('SpeakersStudentTableComponent', () => {
  let component: SpeakersStudentTableComponent;
  let fixture: ComponentFixture<SpeakersStudentTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpeakersStudentTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpeakersStudentTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
