import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberPanelComponent } from './member-panel.component';

describe('MemberPanelComponent', () => {
  let component: MemberPanelComponent;
  let fixture: ComponentFixture<MemberPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemberPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
