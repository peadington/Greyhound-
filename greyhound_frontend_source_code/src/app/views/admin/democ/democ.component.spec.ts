import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemocComponent } from './democ.component';

describe('DemocComponent', () => {
  let component: DemocComponent;
  let fixture: ComponentFixture<DemocComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemocComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
