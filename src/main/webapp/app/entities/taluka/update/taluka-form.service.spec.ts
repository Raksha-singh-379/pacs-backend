import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../taluka.test-samples';

import { TalukaFormService } from './taluka-form.service';

describe('Taluka Form Service', () => {
  let service: TalukaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TalukaFormService);
  });

  describe('Service methods', () => {
    describe('createTalukaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTalukaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            talukaName: expect.any(Object),
            deleted: expect.any(Object),
            lgdCode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing ITaluka should create a new form with FormGroup', () => {
        const formGroup = service.createTalukaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            talukaName: expect.any(Object),
            deleted: expect.any(Object),
            lgdCode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getTaluka', () => {
      it('should return NewTaluka for default Taluka initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTalukaFormGroup(sampleWithNewData);

        const taluka = service.getTaluka(formGroup) as any;

        expect(taluka).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaluka for empty Taluka initial value', () => {
        const formGroup = service.createTalukaFormGroup();

        const taluka = service.getTaluka(formGroup) as any;

        expect(taluka).toMatchObject({});
      });

      it('should return ITaluka', () => {
        const formGroup = service.createTalukaFormGroup(sampleWithRequiredData);

        const taluka = service.getTaluka(formGroup) as any;

        expect(taluka).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaluka should not enable id FormControl', () => {
        const formGroup = service.createTalukaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaluka should disable id FormControl', () => {
        const formGroup = service.createTalukaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
