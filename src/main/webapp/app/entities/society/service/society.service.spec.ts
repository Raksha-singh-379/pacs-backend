import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISociety } from '../society.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../society.test-samples';

import { SocietyService, RestSociety } from './society.service';

const requireRestSample: RestSociety = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('Society Service', () => {
  let service: SocietyService;
  let httpMock: HttpTestingController;
  let expectedResult: ISociety | ISociety[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SocietyService);
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

    it('should create a Society', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const society = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(society).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Society', () => {
      const society = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(society).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Society', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Society', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Society', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSocietyToCollectionIfMissing', () => {
      it('should add a Society to an empty array', () => {
        const society: ISociety = sampleWithRequiredData;
        expectedResult = service.addSocietyToCollectionIfMissing([], society);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(society);
      });

      it('should not add a Society to an array that contains it', () => {
        const society: ISociety = sampleWithRequiredData;
        const societyCollection: ISociety[] = [
          {
            ...society,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSocietyToCollectionIfMissing(societyCollection, society);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Society to an array that doesn't contain it", () => {
        const society: ISociety = sampleWithRequiredData;
        const societyCollection: ISociety[] = [sampleWithPartialData];
        expectedResult = service.addSocietyToCollectionIfMissing(societyCollection, society);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(society);
      });

      it('should add only unique Society to an array', () => {
        const societyArray: ISociety[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const societyCollection: ISociety[] = [sampleWithRequiredData];
        expectedResult = service.addSocietyToCollectionIfMissing(societyCollection, ...societyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const society: ISociety = sampleWithRequiredData;
        const society2: ISociety = sampleWithPartialData;
        expectedResult = service.addSocietyToCollectionIfMissing([], society, society2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(society);
        expect(expectedResult).toContain(society2);
      });

      it('should accept null and undefined values', () => {
        const society: ISociety = sampleWithRequiredData;
        expectedResult = service.addSocietyToCollectionIfMissing([], null, society, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(society);
      });

      it('should return initial array if no Society is added', () => {
        const societyCollection: ISociety[] = [sampleWithRequiredData];
        expectedResult = service.addSocietyToCollectionIfMissing(societyCollection, undefined, null);
        expect(expectedResult).toEqual(societyCollection);
      });
    });

    describe('compareSociety', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSociety(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSociety(entity1, entity2);
        const compareResult2 = service.compareSociety(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSociety(entity1, entity2);
        const compareResult2 = service.compareSociety(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSociety(entity1, entity2);
        const compareResult2 = service.compareSociety(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
