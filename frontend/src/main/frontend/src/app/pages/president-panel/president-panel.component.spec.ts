import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PresidentPanelComponent } from './president-panel.component';

describe('PresidentPanelComponent', () => {
  let component: PresidentPanelComponent;
  let fixture: ComponentFixture<PresidentPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PresidentPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PresidentPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
