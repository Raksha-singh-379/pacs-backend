import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDistrict, NewDistrict } from '../district.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDistrict for edit and NewDistrictFormGroupInput for create.
 */
type DistrictFormGroupInput = IDistrict | PartialWithRequiredKeyOf<NewDistrict>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDistrict | NewDistrict> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type DistrictFormRawValue = FormValueOf<IDistrict>;

type NewDistrictFormRawValue = FormValueOf<NewDistrict>;

type DistrictFormDefaults = Pick<NewDistrict, 'id' | 'deleted' | 'lastModified'>;

type DistrictFormGroupContent = {
  id: FormControl<DistrictFormRawValue['id'] | NewDistrict['id']>;
  districtName: FormControl<DistrictFormRawValue['districtName']>;
  deleted: FormControl<DistrictFormRawValue['deleted']>;
  lgdCode: FormControl<DistrictFormRawValue['lgdCode']>;
  lastModified: FormControl<DistrictFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<DistrictFormRawValue['lastModifiedBy']>;
};

export type DistrictFormGroup = FormGroup<DistrictFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DistrictFormService {
  createDistrictFormGroup(district: DistrictFormGroupInput = { id: null }): DistrictFormGroup {
    const districtRawValue = this.convertDistrictToDistrictRawValue({
      ...this.getFormDefaults(),
      ...district,
    });
    return new FormGroup<DistrictFormGroupContent>({
      id: new FormControl(
        { value: districtRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      districtName: new FormControl(districtRawValue.districtName, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(districtRawValue.deleted),
      lgdCode: new FormControl(districtRawValue.lgdCode),
      lastModified: new FormControl(districtRawValue.lastModified),
      lastModifiedBy: new FormControl(districtRawValue.lastModifiedBy),
    });
  }

  getDistrict(form: DistrictFormGroup): IDistrict | NewDistrict {
    return this.convertDistrictRawValueToDistrict(form.getRawValue() as DistrictFormRawValue | NewDistrictFormRawValue);
  }

  resetForm(form: DistrictFormGroup, district: DistrictFormGroupInput): void {
    const districtRawValue = this.convertDistrictToDistrictRawValue({ ...this.getFormDefaults(), ...district });
    form.reset(
      {
        ...districtRawValue,
        id: { value: districtRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DistrictFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deleted: false,
      lastModified: currentTime,
    };
  }

  private convertDistrictRawValueToDistrict(rawDistrict: DistrictFormRawValue | NewDistrictFormRawValue): IDistrict | NewDistrict {
    return {
      ...rawDistrict,
      lastModified: dayjs(rawDistrict.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertDistrictToDistrictRawValue(
    district: IDistrict | (Partial<NewDistrict> & DistrictFormDefaults)
  ): DistrictFormRawValue | PartialWithRequiredKeyOf<NewDistrictFormRawValue> {
    return {
      ...district,
      lastModified: district.lastModified ? district.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
