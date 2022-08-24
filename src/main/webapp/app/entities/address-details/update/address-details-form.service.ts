import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAddressDetails, NewAddressDetails } from '../address-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddressDetails for edit and NewAddressDetailsFormGroupInput for create.
 */
type AddressDetailsFormGroupInput = IAddressDetails | PartialWithRequiredKeyOf<NewAddressDetails>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAddressDetails | NewAddressDetails> = Omit<T, 'lastModified' | 'createdOn'> & {
  lastModified?: string | null;
  createdOn?: string | null;
};

type AddressDetailsFormRawValue = FormValueOf<IAddressDetails>;

type NewAddressDetailsFormRawValue = FormValueOf<NewAddressDetails>;

type AddressDetailsFormDefaults = Pick<NewAddressDetails, 'id' | 'lastModified' | 'createdOn' | 'isDeleted'>;

type AddressDetailsFormGroupContent = {
  id: FormControl<AddressDetailsFormRawValue['id'] | NewAddressDetails['id']>;
  type: FormControl<AddressDetailsFormRawValue['type']>;
  houseNo: FormControl<AddressDetailsFormRawValue['houseNo']>;
  roadName: FormControl<AddressDetailsFormRawValue['roadName']>;
  landMark: FormControl<AddressDetailsFormRawValue['landMark']>;
  pincode: FormControl<AddressDetailsFormRawValue['pincode']>;
  lastModified: FormControl<AddressDetailsFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<AddressDetailsFormRawValue['lastModifiedBy']>;
  createdBy: FormControl<AddressDetailsFormRawValue['createdBy']>;
  createdOn: FormControl<AddressDetailsFormRawValue['createdOn']>;
  isDeleted: FormControl<AddressDetailsFormRawValue['isDeleted']>;
  freeField1: FormControl<AddressDetailsFormRawValue['freeField1']>;
  freeField2: FormControl<AddressDetailsFormRawValue['freeField2']>;
  freeField3: FormControl<AddressDetailsFormRawValue['freeField3']>;
  state: FormControl<AddressDetailsFormRawValue['state']>;
  district: FormControl<AddressDetailsFormRawValue['district']>;
  taluka: FormControl<AddressDetailsFormRawValue['taluka']>;
  taluka: FormControl<AddressDetailsFormRawValue['taluka']>;
  securityUser: FormControl<AddressDetailsFormRawValue['securityUser']>;
  member: FormControl<AddressDetailsFormRawValue['member']>;
};

export type AddressDetailsFormGroup = FormGroup<AddressDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressDetailsFormService {
  createAddressDetailsFormGroup(addressDetails: AddressDetailsFormGroupInput = { id: null }): AddressDetailsFormGroup {
    const addressDetailsRawValue = this.convertAddressDetailsToAddressDetailsRawValue({
      ...this.getFormDefaults(),
      ...addressDetails,
    });
    return new FormGroup<AddressDetailsFormGroupContent>({
      id: new FormControl(
        { value: addressDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(addressDetailsRawValue.type),
      houseNo: new FormControl(addressDetailsRawValue.houseNo),
      roadName: new FormControl(addressDetailsRawValue.roadName),
      landMark: new FormControl(addressDetailsRawValue.landMark),
      pincode: new FormControl(addressDetailsRawValue.pincode),
      lastModified: new FormControl(addressDetailsRawValue.lastModified),
      lastModifiedBy: new FormControl(addressDetailsRawValue.lastModifiedBy),
      createdBy: new FormControl(addressDetailsRawValue.createdBy),
      createdOn: new FormControl(addressDetailsRawValue.createdOn),
      isDeleted: new FormControl(addressDetailsRawValue.isDeleted),
      freeField1: new FormControl(addressDetailsRawValue.freeField1),
      freeField2: new FormControl(addressDetailsRawValue.freeField2),
      freeField3: new FormControl(addressDetailsRawValue.freeField3),
      state: new FormControl(addressDetailsRawValue.state),
      district: new FormControl(addressDetailsRawValue.district),
      taluka: new FormControl(addressDetailsRawValue.taluka),
      taluka: new FormControl(addressDetailsRawValue.taluka),
      securityUser: new FormControl(addressDetailsRawValue.securityUser),
      member: new FormControl(addressDetailsRawValue.member),
    });
  }

  getAddressDetails(form: AddressDetailsFormGroup): IAddressDetails | NewAddressDetails {
    return this.convertAddressDetailsRawValueToAddressDetails(
      form.getRawValue() as AddressDetailsFormRawValue | NewAddressDetailsFormRawValue
    );
  }

  resetForm(form: AddressDetailsFormGroup, addressDetails: AddressDetailsFormGroupInput): void {
    const addressDetailsRawValue = this.convertAddressDetailsToAddressDetailsRawValue({ ...this.getFormDefaults(), ...addressDetails });
    form.reset(
      {
        ...addressDetailsRawValue,
        id: { value: addressDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressDetailsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModified: currentTime,
      createdOn: currentTime,
      isDeleted: false,
    };
  }

  private convertAddressDetailsRawValueToAddressDetails(
    rawAddressDetails: AddressDetailsFormRawValue | NewAddressDetailsFormRawValue
  ): IAddressDetails | NewAddressDetails {
    return {
      ...rawAddressDetails,
      lastModified: dayjs(rawAddressDetails.lastModified, DATE_TIME_FORMAT),
      createdOn: dayjs(rawAddressDetails.createdOn, DATE_TIME_FORMAT),
    };
  }

  private convertAddressDetailsToAddressDetailsRawValue(
    addressDetails: IAddressDetails | (Partial<NewAddressDetails> & AddressDetailsFormDefaults)
  ): AddressDetailsFormRawValue | PartialWithRequiredKeyOf<NewAddressDetailsFormRawValue> {
    return {
      ...addressDetails,
      lastModified: addressDetails.lastModified ? addressDetails.lastModified.format(DATE_TIME_FORMAT) : undefined,
      createdOn: addressDetails.createdOn ? addressDetails.createdOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
