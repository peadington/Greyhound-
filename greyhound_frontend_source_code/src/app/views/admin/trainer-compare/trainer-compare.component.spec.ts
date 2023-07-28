import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainerCompareComponent } from './trainer-compare.component';

describe('TrainerCompareComponent', () => {
  let component: TrainerCompareComponent;
  let fixture: ComponentFixture<TrainerCompareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrainerCompareComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrainerCompareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
