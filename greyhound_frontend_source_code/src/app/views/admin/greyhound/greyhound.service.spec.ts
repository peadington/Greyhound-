import { TestBed } from '@angular/core/testing';

import { GreyhoundService } from './greyhound.service';

describe('GreyhoundService', () => {
  let service: GreyhoundService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GreyhoundService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
