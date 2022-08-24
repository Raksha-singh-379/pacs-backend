import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISecurityUser, NewSecurityUser } from '../security-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISecurityUser for edit and NewSecurityUserFormGroupInput for create.
 */
type SecurityUserFormGroupInput = ISecurityUser | PartialWithRequiredKeyOf<NewSecurityUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISecurityUser | NewSecurityUser> = Omit<T, 'resetDate' | 'createdOn'> & {
  resetDate?: string | null;
  createdOn?: string | null;
};

type SecurityUserFormRawValue = FormValueOf<ISecurityUser>;

type NewSecurityUserFormRawValue = FormValueOf<NewSecurityUser>;

type SecurityUserFormDefaults = Pick<
  NewSecurityUser,
  'id' | 'activated' | 'resetDate' | 'createdOn' | 'securityPermissions' | 'securityRoles'
>;

type SecurityUserFormGroupContent = {
  id: FormControl<SecurityUserFormRawValue['id'] | NewSecurityUser['id']>;
  firstName: FormControl<SecurityUserFormRawValue['firstName']>;
  lastName: FormControl<SecurityUserFormRawValue['lastName']>;
  designation: FormControl<SecurityUserFormRawValue['designation']>;
  username: FormControl<SecurityUserFormRawValue['username']>;
  passwordHash: FormControl<SecurityUserFormRawValue['passwordHash']>;
  email: FormControl<SecurityUserFormRawValue['email']>;
  description: FormControl<SecurityUserFormRawValue['description']>;
  department: FormControl<SecurityUserFormRawValue['department']>;
  imageUrl: FormControl<SecurityUserFormRawValue['imageUrl']>;
  activated: FormControl<SecurityUserFormRawValue['activated']>;
  langKey: FormControl<SecurityUserFormRawValue['langKey']>;
  activationKey: FormControl<SecurityUserFormRawValue['activationKey']>;
  resetKey: FormControl<SecurityUserFormRawValue['resetKey']>;
  resetDate: FormControl<SecurityUserFormRawValue['resetDate']>;
  mobileNo: FormControl<SecurityUserFormRawValue['mobileNo']>;
  createdBy: FormControl<SecurityUserFormRawValue['createdBy']>;
  createdOn: FormControl<SecurityUserFormRawValue['createdOn']>;
  society: FormControl<SecurityUserFormRawValue['society']>;
  securityPermissions: FormControl<SecurityUserFormRawValue['securityPermissions']>;
  securityRoles: FormControl<SecurityUserFormRawValue['securityRoles']>;
};

export type SecurityUserFormGroup = FormGroup<SecurityUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SecurityUserFormService {
  createSecurityUserFormGroup(securityUser: SecurityUserFormGroupInput = { id: null }): SecurityUserFormGroup {
    const securityUserRawValue = this.convertSecurityUserToSecurityUserRawValue({
      ...this.getFormDefaults(),
      ...securityUser,
    });
    return new FormGroup<SecurityUserFormGroupContent>({
      id: new FormControl(
        { value: securityUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(securityUserRawValue.firstName),
      lastName: new FormControl(securityUserRawValue.lastName),
      designation: new FormControl(securityUserRawValue.designation),
      username: new FormControl(securityUserRawValue.username, {
        validators: [Validators.required],
      }),
      passwordHash: new FormControl(securityUserRawValue.passwordHash, {
        validators: [Validators.required],
      }),
      email: new FormControl(securityUserRawValue.email),
      description: new FormControl(securityUserRawValue.description),
      department: new FormControl(securityUserRawValue.department),
      imageUrl: new FormControl(securityUserRawValue.imageUrl),
      activated: new FormControl(securityUserRawValue.activated),
      langKey: new FormControl(securityUserRawValue.langKey),
      activationKey: new FormControl(securityUserRawValue.activationKey),
      resetKey: new FormControl(securityUserRawValue.resetKey),
      resetDate: new FormControl(securityUserRawValue.resetDate),
      mobileNo: new FormControl(securityUserRawValue.mobileNo),
      createdBy: new FormControl(securityUserRawValue.createdBy),
      createdOn: new FormControl(securityUserRawValue.createdOn),
      society: new FormControl(securityUserRawValue.society),
      securityPermissions: new FormControl(securityUserRawValue.securityPermissions ?? []),
      securityRoles: new FormControl(securityUserRawValue.securityRoles ?? []),
    });
  }

  getSecurityUser(form: SecurityUserFormGroup): ISecurityUser | NewSecurityUser {
    return this.convertSecurityUserRawValueToSecurityUser(form.getRawValue() as SecurityUserFormRawValue | NewSecurityUserFormRawValue);
  }

  resetForm(form: SecurityUserFormGroup, securityUser: SecurityUserFormGroupInput): void {
    const securityUserRawValue = this.convertSecurityUserToSecurityUserRawValue({ ...this.getFormDefaults(), ...securityUser });
    form.reset(
      {
        ...securityUserRawValue,
        id: { value: securityUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SecurityUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      activated: false,
      resetDate: currentTime,
      createdOn: currentTime,
      securityPermissions: [],
      securityRoles: [],
    };
  }

  private convertSecurityUserRawValueToSecurityUser(
    rawSecurityUser: SecurityUserFormRawValue | NewSecurityUserFormRawValue
  ): ISecurityUser | NewSecurityUser {
    return {
      ...rawSecurityUser,
      resetDate: dayjs(rawSecurityUser.resetDate, DATE_TIME_FORMAT),
      createdOn: dayjs(rawSecurityUser.createdOn, DATE_TIME_FORMAT),
    };
  }

  private convertSecurityUserToSecurityUserRawValue(
    securityUser: ISecurityUser | (Partial<NewSecurityUser> & SecurityUserFormDefaults)
  ): SecurityUserFormRawValue | PartialWithRequiredKeyOf<NewSecurityUserFormRawValue> {
    return {
      ...securityUser,
      resetDate: securityUser.resetDate ? securityUser.resetDate.format(DATE_TIME_FORMAT) : undefined,
      createdOn: securityUser.createdOn ? securityUser.createdOn.format(DATE_TIME_FORMAT) : undefined,
      securityPermissions: securityUser.securityPermissions ?? [],
      securityRoles: securityUser.securityRoles ?? [],
    };
  }
}
