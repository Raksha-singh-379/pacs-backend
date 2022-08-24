import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISecurityRole } from '../security-role.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../security-role.test-samples';

import { SecurityRoleService, RestSecurityRole } from './security-role.service';

const requireRestSample: RestSecurityRole = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('SecurityRole Service', () => {
  let service: SecurityRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISecurityRole | ISecurityRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityRoleService);
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

    it('should create a SecurityRole', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const securityRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(securityRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityRole', () => {
      const securityRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(securityRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SecurityRole', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityRoleToCollectionIfMissing', () => {
      it('should add a SecurityRole to an empty array', () => {
        const securityRole: ISecurityRole = sampleWithRequiredData;
        expectedResult = service.addSecurityRoleToCollectionIfMissing([], securityRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityRole);
      });

      it('should not add a SecurityRole to an array that contains it', () => {
        const securityRole: ISecurityRole = sampleWithRequiredData;
        const securityRoleCollection: ISecurityRole[] = [
          {
            ...securityRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSecurityRoleToCollectionIfMissing(securityRoleCollection, securityRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityRole to an array that doesn't contain it", () => {
        const securityRole: ISecurityRole = sampleWithRequiredData;
        const securityRoleCollection: ISecurityRole[] = [sampleWithPartialData];
        expectedResult = service.addSecurityRoleToCollectionIfMissing(securityRoleCollection, securityRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityRole);
      });

      it('should add only unique SecurityRole to an array', () => {
        const securityRoleArray: ISecurityRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const securityRoleCollection: ISecurityRole[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityRoleToCollectionIfMissing(securityRoleCollection, ...securityRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityRole: ISecurityRole = sampleWithRequiredData;
        const securityRole2: ISecurityRole = sampleWithPartialData;
        expectedResult = service.addSecurityRoleToCollectionIfMissing([], securityRole, securityRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityRole);
        expect(expectedResult).toContain(securityRole2);
      });

      it('should accept null and undefined values', () => {
        const securityRole: ISecurityRole = sampleWithRequiredData;
        expectedResult = service.addSecurityRoleToCollectionIfMissing([], null, securityRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityRole);
      });

      it('should return initial array if no SecurityRole is added', () => {
        const securityRoleCollection: ISecurityRole[] = [sampleWithRequiredData];
        expectedResult = service.addSecurityRoleToCollectionIfMissing(securityRoleCollection, undefined, null);
        expect(expectedResult).toEqual(securityRoleCollection);
      });
    });

    describe('compareSecurityRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSecurityRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSecurityRole(entity1, entity2);
        const compareResult2 = service.compareSecurityRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSecurityRole(entity1, entity2);
        const compareResult2 = service.compareSecurityRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSecurityRole(entity1, entity2);
        const compareResult2 = service.compareSecurityRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
