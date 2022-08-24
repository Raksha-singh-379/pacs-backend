import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVillage } from '../village.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../village.test-samples';

import { VillageService, RestVillage } from './village.service';

const requireRestSample: RestVillage = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('Village Service', () => {
  let service: VillageService;
  let httpMock: HttpTestingController;
  let expectedResult: IVillage | IVillage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VillageService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Village', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const village = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(village).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Village', () => {
      const village = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(village).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Village', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Village', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Village', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVillageToCollectionIfMissing', () => {
      it('should add a Village to an empty array', () => {
        const village: IVillage = sampleWithRequiredData;
        expectedResult = service.addVillageToCollectionIfMissing([], village);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(village);
      });

      it('should not add a Village to an array that contains it', () => {
        const village: IVillage = sampleWithRequiredData;
        const villageCollection: IVillage[] = [
          {
            ...village,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVillageToCollectionIfMissing(villageCollection, village);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Village to an array that doesn't contain it", () => {
        const village: IVillage = sampleWithRequiredData;
        const villageCollection: IVillage[] = [sampleWithPartialData];
        expectedResult = service.addVillageToCollectionIfMissing(villageCollection, village);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(village);
      });

      it('should add only unique Village to an array', () => {
        const villageArray: IVillage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const villageCollection: IVillage[] = [sampleWithRequiredData];
        expectedResult = service.addVillageToCollectionIfMissing(villageCollection, ...villageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const village: IVillage = sampleWithRequiredData;
        const village2: IVillage = sampleWithPartialData;
        expectedResult = service.addVillageToCollectionIfMissing([], village, village2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(village);
        expect(expectedResult).toContain(village2);
      });

      it('should accept null and undefined values', () => {
        const village: IVillage = sampleWithRequiredData;
        expectedResult = service.addVillageToCollectionIfMissing([], null, village, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(village);
      });

      it('should return initial array if no Village is added', () => {
        const villageCollection: IVillage[] = [sampleWithRequiredData];
        expectedResult = service.addVillageToCollectionIfMissing(villageCollection, undefined, null);
        expect(expectedResult).toEqual(villageCollection);
      });
    });

    describe('compareVillage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVillage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVillage(entity1, entity2);
        const compareResult2 = service.compareVillage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVillage(entity1, entity2);
        const compareResult2 = service.compareVillage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVillage(entity1, entity2);
        const compareResult2 = service.compareVillage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
