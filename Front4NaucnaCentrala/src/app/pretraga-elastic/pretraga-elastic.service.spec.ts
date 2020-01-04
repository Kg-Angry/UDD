import { TestBed } from '@angular/core/testing';

import { PretragaElasticService } from './pretraga-elastic.service';

describe('PretragaElasticService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PretragaElasticService = TestBed.get(PretragaElasticService);
    expect(service).toBeTruthy();
  });
});
