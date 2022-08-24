import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../address-details.test-samples';

import { AddressDetailsFormService } from './address-details-form.service';

describe('AddressDetails Form Service', () => {
  let service: AddressDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddressDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createAddressDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAddressDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            houseNo: expect.any(Object),
            roadName: expect.any(Object),
            landMark: expect.any(Object),
            pincode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            isDeleted: expect.any(Object),
            freeField1: expect.any(Object),
            freeField2: expect.any(Object),
            freeField3: expect.any(Object),
            state: expect.any(Object),
            district: expect.any(Object),
            taluka: expect.any(Object),
            taluka: expect.any(Object),
            securityUser: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });

      it('passing IAddressDetails should create a new form with FormGroup', () => {
        const formGroup = service.createAddressDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            houseNo: expect.any(Object),
            roadName: expect.any(Object),
            landMark: expect.any(Object),
            pincode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            isDeleted: expect.any(Object),
            freeField1: expect.any(Object),
            freeField2: expect.any(Object),
            freeField3: expect.any(Object),
            state: expect.any(Object),
            district: expect.any(Object),
            taluka: expect.any(Object),
            taluka: expect.any(Object),
            securityUser: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });
    });

    describe('getAddressDetails', () => {
      it('should return NewAddressDetails for default AddressDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAddressDetailsFormGroup(sampleWithNewData);

        const addressDetails = service.getAddressDetails(formGroup) as any;

        expect(addressDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewAddressDetails for empty AddressDetails initial value', () => {
        const formGroup = service.createAddressDetailsFormGroup();

        const addressDetails = service.getAddressDetails(formGroup) as any;

        expect(addressDetails).toMatchObject({});
      });

      it('should return IAddressDetails', () => {
        const formGroup = service.createAddressDetailsFormGroup(sampleWithRequiredData);

        const addressDetails = service.getAddressDetails(formGroup) as any;

        expect(addressDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAddressDetails should not enable id FormControl', () => {
        const formGroup = service.createAddressDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAddressDetails should disable id FormControl', () => {
        const formGroup = service.createAddressDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
