import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElkComponent } from './elk.component';

describe('ElkComponent', () => {
  let component: ElkComponent;
  let fixture: ComponentFixture<ElkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
