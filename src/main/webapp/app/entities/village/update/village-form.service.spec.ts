import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../village.test-samples';

import { VillageFormService } from './village-form.service';

describe('Village Form Service', () => {
  let service: VillageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VillageFormService);
  });

  describe('Service methods', () => {
    describe('createVillageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVillageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            villageName: expect.any(Object),
            deleted: expect.any(Object),
            lgdCode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IVillage should create a new form with FormGroup', () => {
        const formGroup = service.createVillageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            villageName: expect.any(Object),
            deleted: expect.any(Object),
            lgdCode: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getVillage', () => {
      it('should return NewVillage for default Village initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVillageFormGroup(sampleWithNewData);

        const village = service.getVillage(formGroup) as any;

        expect(village).toMatchObject(sampleWithNewData);
      });

      it('should return NewVillage for empty Village initial value', () => {
        const formGroup = service.createVillageFormGroup();

        const village = service.getVillage(formGroup) as any;

        expect(village).toMatchObject({});
      });

      it('should return IVillage', () => {
        const formGroup = service.createVillageFormGroup(sampleWithRequiredData);

        const village = service.getVillage(formGroup) as any;

        expect(village).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVillage should not enable id FormControl', () => {
        const formGroup = service.createVillageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVillage should disable id FormControl', () => {
        const formGroup = service.createVillageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
