import { TestBed } from '@angular/core/testing';

import { RentalContractService } from './rental-contract.service';

describe('RentalContractService', () => {
  let service: RentalContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentalContractService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
