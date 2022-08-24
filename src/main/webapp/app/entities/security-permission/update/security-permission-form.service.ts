import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISecurityPermission, NewSecurityPermission } from '../security-permission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISecurityPermission for edit and NewSecurityPermissionFormGroupInput for create.
 */
type SecurityPermissionFormGroupInput = ISecurityPermission | PartialWithRequiredKeyOf<NewSecurityPermission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISecurityPermission | NewSecurityPermission> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type SecurityPermissionFormRawValue = FormValueOf<ISecurityPermission>;

type NewSecurityPermissionFormRawValue = FormValueOf<NewSecurityPermission>;

type SecurityPermissionFormDefaults = Pick<NewSecurityPermission, 'id' | 'lastModified' | 'securityRoles' | 'securityUsers'>;

type SecurityPermissionFormGroupContent = {
  id: FormControl<SecurityPermissionFormRawValue['id'] | NewSecurityPermission['id']>;
  permissionName: FormControl<SecurityPermissionFormRawValue['permissionName']>;
  description: FormControl<SecurityPermissionFormRawValue['description']>;
  lastModified: FormControl<SecurityPermissionFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<SecurityPermissionFormRawValue['lastModifiedBy']>;
  securityRoles: FormControl<SecurityPermissionFormRawValue['securityRoles']>;
  securityUsers: FormControl<SecurityPermissionFormRawValue['securityUsers']>;
};

export type SecurityPermissionFormGroup = FormGroup<SecurityPermissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SecurityPermissionFormService {
  createSecurityPermissionFormGroup(securityPermission: SecurityPermissionFormGroupInput = { id: null }): SecurityPermissionFormGroup {
    const securityPermissionRawValue = this.convertSecurityPermissionToSecurityPermissionRawValue({
      ...this.getFormDefaults(),
      ...securityPermission,
    });
    return new FormGroup<SecurityPermissionFormGroupContent>({
      id: new FormControl(
        { value: securityPermissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      permissionName: new FormControl(securityPermissionRawValue.permissionName, {
        validators: [Validators.required],
      }),
      description: new FormControl(securityPermissionRawValue.description),
      lastModified: new FormControl(securityPermissionRawValue.lastModified),
      lastModifiedBy: new FormControl(securityPermissionRawValue.lastModifiedBy),
      securityRoles: new FormControl(securityPermissionRawValue.securityRoles ?? []),
      securityUsers: new FormControl(securityPermissionRawValue.securityUsers ?? []),
    });
  }

  getSecurityPermission(form: SecurityPermissionFormGroup): ISecurityPermission | NewSecurityPermission {
    return this.convertSecurityPermissionRawValueToSecurityPermission(
      form.getRawValue() as SecurityPermissionFormRawValue | NewSecurityPermissionFormRawValue
    );
  }

  resetForm(form: SecurityPermissionFormGroup, securityPermission: SecurityPermissionFormGroupInput): void {
    const securityPermissionRawValue = this.convertSecurityPermissionToSecurityPermissionRawValue({
      ...this.getFormDefaults(),
      ...securityPermission,
    });
    form.reset(
      {
        ...securityPermissionRawValue,
        id: { value: securityPermissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SecurityPermissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModified: currentTime,
      securityRoles: [],
      securityUsers: [],
    };
  }

  private convertSecurityPermissionRawValueToSecurityPermission(
    rawSecurityPermission: SecurityPermissionFormRawValue | NewSecurityPermissionFormRawValue
  ): ISecurityPermission | NewSecurityPermission {
    return {
      ...rawSecurityPermission,
      lastModified: dayjs(rawSecurityPermission.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertSecurityPermissionToSecurityPermissionRawValue(
    securityPermission: ISecurityPermission | (Partial<NewSecurityPermission> & SecurityPermissionFormDefaults)
  ): SecurityPermissionFormRawValue | PartialWithRequiredKeyOf<NewSecurityPermissionFormRawValue> {
    return {
      ...securityPermission,
      lastModified: securityPermission.lastModified ? securityPermission.lastModified.format(DATE_TIME_FORMAT) : undefined,
      securityRoles: securityPermission.securityRoles ?? [],
      securityUsers: securityPermission.securityUsers ?? [],
    };
  }
}
