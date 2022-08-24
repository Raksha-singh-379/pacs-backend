import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../security-role.test-samples';

import { SecurityRoleFormService } from './security-role-form.service';

describe('SecurityRole Form Service', () => {
  let service: SecurityRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SecurityRoleFormService);
  });

  describe('Service methods', () => {
    describe('createSecurityRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSecurityRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
            description: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            securityPermissions: expect.any(Object),
            securityUsers: expect.any(Object),
          })
        );
      });

      it('passing ISecurityRole should create a new form with FormGroup', () => {
        const formGroup = service.createSecurityRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
            description: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            securityPermissions: expect.any(Object),
            securityUsers: expect.any(Object),
          })
        );
      });
    });

    describe('getSecurityRole', () => {
      it('should return NewSecurityRole for default SecurityRole initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSecurityRoleFormGroup(sampleWithNewData);

        const securityRole = service.getSecurityRole(formGroup) as any;

        expect(securityRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewSecurityRole for empty SecurityRole initial value', () => {
        const formGroup = service.createSecurityRoleFormGroup();

        const securityRole = service.getSecurityRole(formGroup) as any;

        expect(securityRole).toMatchObject({});
      });

      it('should return ISecurityRole', () => {
        const formGroup = service.createSecurityRoleFormGroup(sampleWithRequiredData);

        const securityRole = service.getSecurityRole(formGroup) as any;

        expect(securityRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISecurityRole should not enable id FormControl', () => {
        const formGroup = service.createSecurityRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSecurityRole should disable id FormControl', () => {
        const formGroup = service.createSecurityRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
