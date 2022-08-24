import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITaluka } from '../taluka.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../taluka.test-samples';

import { TalukaService, RestTaluka } from './taluka.service';

const requireRestSample: RestTaluka = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('Taluka Service', () => {
  let service: TalukaService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaluka | ITaluka[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TalukaService);
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

    it('should create a Taluka', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const taluka = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taluka).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Taluka', () => {
      const taluka = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taluka).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Taluka', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Taluka', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Taluka', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTalukaToCollectionIfMissing', () => {
      it('should add a Taluka to an empty array', () => {
        const taluka: ITaluka = sampleWithRequiredData;
        expectedResult = service.addTalukaToCollectionIfMissing([], taluka);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taluka);
      });

      it('should not add a Taluka to an array that contains it', () => {
        const taluka: ITaluka = sampleWithRequiredData;
        const talukaCollection: ITaluka[] = [
          {
            ...taluka,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTalukaToCollectionIfMissing(talukaCollection, taluka);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Taluka to an array that doesn't contain it", () => {
        const taluka: ITaluka = sampleWithRequiredData;
        const talukaCollection: ITaluka[] = [sampleWithPartialData];
        expectedResult = service.addTalukaToCollectionIfMissing(talukaCollection, taluka);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taluka);
      });

      it('should add only unique Taluka to an array', () => {
        const talukaArray: ITaluka[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const talukaCollection: ITaluka[] = [sampleWithRequiredData];
        expectedResult = service.addTalukaToCollectionIfMissing(talukaCollection, ...talukaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taluka: ITaluka = sampleWithRequiredData;
        const taluka2: ITaluka = sampleWithPartialData;
        expectedResult = service.addTalukaToCollectionIfMissing([], taluka, taluka2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taluka);
        expect(expectedResult).toContain(taluka2);
      });

      it('should accept null and undefined values', () => {
        const taluka: ITaluka = sampleWithRequiredData;
        expectedResult = service.addTalukaToCollectionIfMissing([], null, taluka, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taluka);
      });

      it('should return initial array if no Taluka is added', () => {
        const talukaCollection: ITaluka[] = [sampleWithRequiredData];
        expectedResult = service.addTalukaToCollectionIfMissing(talukaCollection, undefined, null);
        expect(expectedResult).toEqual(talukaCollection);
      });
    });

    describe('compareTaluka', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaluka(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTaluka(entity1, entity2);
        const compareResult2 = service.compareTaluka(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTaluka(entity1, entity2);
        const compareResult2 = service.compareTaluka(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTaluka(entity1, entity2);
        const compareResult2 = service.compareTaluka(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
