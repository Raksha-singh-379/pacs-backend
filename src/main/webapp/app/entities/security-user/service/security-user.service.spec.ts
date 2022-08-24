import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISecurityUser } from '../security-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../security-user.test-samples';

import { SecurityUserService, RestSecurityUser } from './security-user.service';

const requireRestSample: RestSecurityUser = {
  ...sampleWithRequiredData,
  resetDate: sampleWithRequiredData.resetDate?.toJSON(),
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
};

describe('SecurityUser Service', () => {
  let service: SecurityUserService;
  let httpMock: HttpTestingController;
  let expectedResult: ISecurityUser | ISecurityUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityUserService);
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

    it('should create a SecurityUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const securityUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(securityUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityUser', () => {
      const securityUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(securityUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SecurityUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityUserToCollectionIfMissing', () => {
      it('should add a SecurityUser to an empty array', () => {
        const securityUser: ISecurityUser = sampleWithRequiredData;
        expectedResult = service.addSecurityUserToCollectionIfMissing([], securityUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityUser);
      });

      it('should not add a SecurityUser to an array that contains it', () => {
        const securityUser: ISecurityUser = sampleWithRequiredData;
        const securityUserCollection: ISecurityUser[] = [
          {
            ...securityUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSecurityUserToCollectionIfMissing(securityUserCollection, securityUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityUser to an array that doesn't contain it", () => {
        const securityUser: ISecurityUser = sampleWithRequiredData;
        const securityUserCollection: ISecurityUser[] = [sampleWithPartialData];
        expectedResult = service.addSecurityUserToCollectionIfMissing(securityUserCollection, securityUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityUser);
      });

      it('should add only unique SecurityUser to an array', () => {
        const securityUserArray: ISecurityUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const securityUserCollection: ISecurityUser[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityUserToCollectionIfMissing(securityUserCollection, ...securityUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityUser: ISecurityUser = sampleWithRequiredData;
        const securityUser2: ISecurityUser = sampleWithPartialData;
        expectedResult = service.addSecurityUserToCollectionIfMissing([], securityUser, securityUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityUser);
        expect(expectedResult).toContain(securityUser2);
      });

      it('should accept null and undefined values', () => {
        const securityUser: ISecurityUser = sampleWithRequiredData;
        expectedResult = service.addSecurityUserToCollectionIfMissing([], null, securityUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityUser);
      });

      it('should return initial array if no SecurityUser is added', () => {
        const securityUserCollection: ISecurityUser[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityUserToCollectionIfMissing(securityUserCollection, undefined, null);
        expect(expectedResult).toEqual(securityUserCollection);
      });
    });

    describe('compareSecurityUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSecurityUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSecurityUser(entity1, entity2);
        const compareResult2 = service.compareSecurityUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSecurityUser(entity1, entity2);
        const compareResult2 = service.compareSecurityUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSecurityUser(entity1, entity2);
        const compareResult2 = service.compareSecurityUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
