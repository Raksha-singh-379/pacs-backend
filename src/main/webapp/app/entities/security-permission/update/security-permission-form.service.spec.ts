import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../security-permission.test-samples';

import { SecurityPermissionFormService } from './security-permission-form.service';

describe('SecurityPermission Form Service', () => {
  let service: SecurityPermissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SecurityPermissionFormService);
  });

  describe('Service methods', () => {
    describe('createSecurityPermissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSecurityPermissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            permissionName: expect.any(Object),
            description: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            securityRoles: expect.any(Object),
            securityUsers: expect.any(Object),
          })
        );
      });

      it('passing ISecurityPermission should create a new form with FormGroup', () => {
        const formGroup = service.createSecurityPermissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            permissionName: expect.any(Object),
            description: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            securityRoles: expect.any(Object),
            securityUsers: expect.any(Object),
          })
        );
      });
    });

    describe('getSecurityPermission', () => {
      it('should return NewSecurityPermission for default SecurityPermission initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSecurityPermissionFormGroup(sampleWithNewData);

        const securityPermission = service.getSecurityPermission(formGroup) as any;

        expect(securityPermission).toMatchObject(sampleWithNewData);
      });

      it('should return NewSecurityPermission for empty SecurityPermission initial value', () => {
        const formGroup = service.createSecurityPermissionFormGroup();

        const securityPermission = service.getSecurityPermission(formGroup) as any;

        expect(securityPermission).toMatchObject({});
      });

      it('should return ISecurityPermission', () => {
        const formGroup = service.createSecurityPermissionFormGroup(sampleWithRequiredData);

        const securityPermission = service.getSecurityPermission(formGroup) as any;

        expect(securityPermission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISecurityPermission should not enable id FormControl', () => {
        const formGroup = service.createSecurityPermissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSecurityPermission should disable id FormControl', () => {
        const formGroup = service.createSecurityPermissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
