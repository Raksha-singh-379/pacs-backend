import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISecurityPermission } from '../security-permission.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../security-permission.test-samples';

import { SecurityPermissionService, RestSecurityPermission } from './security-permission.service';

const requireRestSample: RestSecurityPermission = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('SecurityPermission Service', () => {
  let service: SecurityPermissionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISecurityPermission | ISecurityPermission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityPermissionService);
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

    it('should create a SecurityPermission', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const securityPermission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(securityPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityPermission', () => {
      const securityPermission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(securityPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityPermission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityPermission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SecurityPermission', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityPermissionToCollectionIfMissing', () => {
      it('should add a SecurityPermission to an empty array', () => {
        const securityPermission: ISecurityPermission = sampleWithRequiredData;
        expectedResult = service.addSecurityPermissionToCollectionIfMissing([], securityPermission);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityPermission);
      });

      it('should not add a SecurityPermission to an array that contains it', () => {
        const securityPermission: ISecurityPermission = sampleWithRequiredData;
        const securityPermissionCollection: ISecurityPermission[] = [
          {
            ...securityPermission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSecurityPermissionToCollectionIfMissing(securityPermissionCollection, securityPermission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityPermission to an array that doesn't contain it", () => {
        const securityPermission: ISecurityPermission = sampleWithRequiredData;
        const securityPermissionCollection: ISecurityPermission[] = [sampleWithPartialData];
        expectedResult = service.addSecurityPermissionToCollectionIfMissing(securityPermissionCollection, securityPermission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityPermission);
      });

      it('should add only unique SecurityPermission to an array', () => {
        const securityPermissionArray: ISecurityPermission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const securityPermissionCollection: ISecurityPermission[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityPermissionToCollectionIfMissing(securityPermissionCollection, ...securityPermissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityPermission: ISecurityPermission = sampleWithRequiredData;
        const securityPermission2: ISecurityPermission = sampleWithPartialData;
        expectedResult = service.addSecurityPermissionToCollectionIfMissing([], securityPermission, securityPermission2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityPermission);
        expect(expectedResult).toContain(securityPermission2);
      });

      it('should accept null and undefined values', () => {
        const securityPermission: ISecurityPermission = sampleWithRequiredData;
        expectedResult = service.addSecurityPermissionToCollectionIfMissing([], null, securityPermission, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityPermission);
      });

      it('should return initial array if no SecurityPermission is added', () => {
        const securityPermissionCollection: ISecurityPermission[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityPermissionToCollectionIfMissing(securityPermissionCollection, undefined, null);
        expect(expectedResult).toEqual(securityPermissionCollection);
      });
    });

    describe('compareSecurityPermission', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSecurityPermission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSecurityPermission(entity1, entity2);
        const compareResult2 = service.compareSecurityPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSecurityPermission(entity1, entity2);
        const compareResult2 = service.compareSecurityPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSecurityPermission(entity1, entity2);
        const compareResult2 = service.compareSecurityPermission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
