import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionsTableComponent } from './commissions-table.component';

describe('CommissionsTableComponent', () => {
  let component: CommissionsTableComponent;
  let fixture: ComponentFixture<CommissionsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommissionsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
