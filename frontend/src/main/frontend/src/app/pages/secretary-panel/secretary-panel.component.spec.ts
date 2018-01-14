import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecretaryPanelComponent } from './secretary-panel.component';

describe('SecretaryPanelComponent', () => {
  let component: SecretaryPanelComponent;
  let fixture: ComponentFixture<SecretaryPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecretaryPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecretaryPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
