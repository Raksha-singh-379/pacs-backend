import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../security-user.test-samples';

import { SecurityUserFormService } from './security-user-form.service';

describe('SecurityUser Form Service', () => {
  let service: SecurityUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SecurityUserFormService);
  });

  describe('Service methods', () => {
    describe('createSecurityUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSecurityUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            designation: expect.any(Object),
            username: expect.any(Object),
            passwordHash: expect.any(Object),
            email: expect.any(Object),
            description: expect.any(Object),
            department: expect.any(Object),
            imageUrl: expect.any(Object),
            activated: expect.any(Object),
            langKey: expect.any(Object),
            activationKey: expect.any(Object),
            resetKey: expect.any(Object),
            resetDate: expect.any(Object),
            mobileNo: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            society: expect.any(Object),
            securityPermissions: expect.any(Object),
            securityRoles: expect.any(Object),
          })
        );
      });

      it('passing ISecurityUser should create a new form with FormGroup', () => {
        const formGroup = service.createSecurityUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            designation: expect.any(Object),
            username: expect.any(Object),
            passwordHash: expect.any(Object),
            email: expect.any(Object),
            description: expect.any(Object),
            department: expect.any(Object),
            imageUrl: expect.any(Object),
            activated: expect.any(Object),
            langKey: expect.any(Object),
            activationKey: expect.any(Object),
            resetKey: expect.any(Object),
            resetDate: expect.any(Object),
            mobileNo: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            society: expect.any(Object),
            securityPermissions: expect.any(Object),
            securityRoles: expect.any(Object),
          })
        );
      });
    });

    describe('getSecurityUser', () => {
      it('should return NewSecurityUser for default SecurityUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSecurityUserFormGroup(sampleWithNewData);

        const securityUser = service.getSecurityUser(formGroup) as any;

        expect(securityUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewSecurityUser for empty SecurityUser initial value', () => {
        const formGroup = service.createSecurityUserFormGroup();

        const securityUser = service.getSecurityUser(formGroup) as any;

        expect(securityUser).toMatchObject({});
      });

      it('should return ISecurityUser', () => {
        const formGroup = service.createSecurityUserFormGroup(sampleWithRequiredData);

        const securityUser = service.getSecurityUser(formGroup) as any;

        expect(securityUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISecurityUser should not enable id FormControl', () => {
        const formGroup = service.createSecurityUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSecurityUser should disable id FormControl', () => {
        const formGroup = service.createSecurityUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
