import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISecurityRole, NewSecurityRole } from '../security-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISecurityRole for edit and NewSecurityRoleFormGroupInput for create.
 */
type SecurityRoleFormGroupInput = ISecurityRole | PartialWithRequiredKeyOf<NewSecurityRole>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISecurityRole | NewSecurityRole> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type SecurityRoleFormRawValue = FormValueOf<ISecurityRole>;

type NewSecurityRoleFormRawValue = FormValueOf<NewSecurityRole>;

type SecurityRoleFormDefaults = Pick<NewSecurityRole, 'id' | 'lastModified' | 'securityPermissions' | 'securityUsers'>;

type SecurityRoleFormGroupContent = {
  id: FormControl<SecurityRoleFormRawValue['id'] | NewSecurityRole['id']>;
  roleName: FormControl<SecurityRoleFormRawValue['roleName']>;
  description: FormControl<SecurityRoleFormRawValue['description']>;
  lastModified: FormControl<SecurityRoleFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<SecurityRoleFormRawValue['lastModifiedBy']>;
  securityPermissions: FormControl<SecurityRoleFormRawValue['securityPermissions']>;
  securityUsers: FormControl<SecurityRoleFormRawValue['securityUsers']>;
};

export type SecurityRoleFormGroup = FormGroup<SecurityRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SecurityRoleFormService {
  createSecurityRoleFormGroup(securityRole: SecurityRoleFormGroupInput = { id: null }): SecurityRoleFormGroup {
    const securityRoleRawValue = this.convertSecurityRoleToSecurityRoleRawValue({
      ...this.getFormDefaults(),
      ...securityRole,
    });
    return new FormGroup<SecurityRoleFormGroupContent>({
      id: new FormControl(
        { value: securityRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      roleName: new FormControl(securityRoleRawValue.roleName, {
        validators: [Validators.required],
      }),
      description: new FormControl(securityRoleRawValue.description),
      lastModified: new FormControl(securityRoleRawValue.lastModified),
      lastModifiedBy: new FormControl(securityRoleRawValue.lastModifiedBy),
      securityPermissions: new FormControl(securityRoleRawValue.securityPermissions ?? []),
      securityUsers: new FormControl(securityRoleRawValue.securityUsers ?? []),
    });
  }

  getSecurityRole(form: SecurityRoleFormGroup): ISecurityRole | NewSecurityRole {
    return this.convertSecurityRoleRawValueToSecurityRole(form.getRawValue() as SecurityRoleFormRawValue | NewSecurityRoleFormRawValue);
  }

  resetForm(form: SecurityRoleFormGroup, securityRole: SecurityRoleFormGroupInput): void {
    const securityRoleRawValue = this.convertSecurityRoleToSecurityRoleRawValue({ ...this.getFormDefaults(), ...securityRole });
    form.reset(
      {
        ...securityRoleRawValue,
        id: { value: securityRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SecurityRoleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModified: currentTime,
      securityPermissions: [],
      securityUsers: [],
    };
  }

  private convertSecurityRoleRawValueToSecurityRole(
    rawSecurityRole: SecurityRoleFormRawValue | NewSecurityRoleFormRawValue
  ): ISecurityRole | NewSecurityRole {
    return {
      ...rawSecurityRole,
      lastModified: dayjs(rawSecurityRole.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertSecurityRoleToSecurityRoleRawValue(
    securityRole: ISecurityRole | (Partial<NewSecurityRole> & SecurityRoleFormDefaults)
  ): SecurityRoleFormRawValue | PartialWithRequiredKeyOf<NewSecurityRoleFormRawValue> {
    return {
      ...securityRole,
      lastModified: securityRole.lastModified ? securityRole.lastModified.format(DATE_TIME_FORMAT) : undefined,
      securityPermissions: securityRole.securityPermissions ?? [],
      securityUsers: securityRole.securityUsers ?? [],
    };
  }
}
