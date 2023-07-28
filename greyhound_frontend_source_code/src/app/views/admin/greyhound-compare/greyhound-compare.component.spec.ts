import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GreyhoundCompareComponent } from './greyhound-compare.component';

describe('GreyhoundCompareComponent', () => {
  let component: GreyhoundCompareComponent;
  let fixture: ComponentFixture<GreyhoundCompareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GreyhoundCompareComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GreyhoundCompareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
