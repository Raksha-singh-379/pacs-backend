import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVillage, NewVillage } from '../village.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVillage for edit and NewVillageFormGroupInput for create.
 */
type VillageFormGroupInput = IVillage | PartialWithRequiredKeyOf<NewVillage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVillage | NewVillage> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type VillageFormRawValue = FormValueOf<IVillage>;

type NewVillageFormRawValue = FormValueOf<NewVillage>;

type VillageFormDefaults = Pick<NewVillage, 'id' | 'deleted' | 'lastModified'>;

type VillageFormGroupContent = {
  id: FormControl<VillageFormRawValue['id'] | NewVillage['id']>;
  villageName: FormControl<VillageFormRawValue['villageName']>;
  deleted: FormControl<VillageFormRawValue['deleted']>;
  lgdCode: FormControl<VillageFormRawValue['lgdCode']>;
  lastModified: FormControl<VillageFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<VillageFormRawValue['lastModifiedBy']>;
};

export type VillageFormGroup = FormGroup<VillageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VillageFormService {
  createVillageFormGroup(village: VillageFormGroupInput = { id: null }): VillageFormGroup {
    const villageRawValue = this.convertVillageToVillageRawValue({
      ...this.getFormDefaults(),
      ...village,
    });
    return new FormGroup<VillageFormGroupContent>({
      id: new FormControl(
        { value: villageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      villageName: new FormControl(villageRawValue.villageName, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(villageRawValue.deleted),
      lgdCode: new FormControl(villageRawValue.lgdCode),
      lastModified: new FormControl(villageRawValue.lastModified),
      lastModifiedBy: new FormControl(villageRawValue.lastModifiedBy),
    });
  }

  getVillage(form: VillageFormGroup): IVillage | NewVillage {
    return this.convertVillageRawValueToVillage(form.getRawValue() as VillageFormRawValue | NewVillageFormRawValue);
  }

  resetForm(form: VillageFormGroup, village: VillageFormGroupInput): void {
    const villageRawValue = this.convertVillageToVillageRawValue({ ...this.getFormDefaults(), ...village });
    form.reset(
      {
        ...villageRawValue,
        id: { value: villageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VillageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deleted: false,
      lastModified: currentTime,
    };
  }

  private convertVillageRawValueToVillage(rawVillage: VillageFormRawValue | NewVillageFormRawValue): IVillage | NewVillage {
    return {
      ...rawVillage,
      lastModified: dayjs(rawVillage.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertVillageToVillageRawValue(
    village: IVillage | (Partial<NewVillage> & VillageFormDefaults)
  ): VillageFormRawValue | PartialWithRequiredKeyOf<NewVillageFormRawValue> {
    return {
      ...village,
      lastModified: village.lastModified ? village.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
