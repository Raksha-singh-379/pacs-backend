import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SecurityRoleFormService, SecurityRoleFormGroup } from './security-role-form.service';
import { ISecurityRole } from '../security-role.model';
import { SecurityRoleService } from '../service/security-role.service';
import { ISecurityPermission } from 'app/entities/security-permission/security-permission.model';
import { SecurityPermissionService } from 'app/entities/security-permission/service/security-permission.service';

@Component({
  selector: 'jhi-security-role-update',
  templateUrl: './security-role-update.component.html',
})
export class SecurityRoleUpdateComponent implements OnInit {
  isSaving = false;
  securityRole: ISecurityRole | null = null;

  securityPermissionsSharedCollection: ISecurityPermission[] = [];

  editForm: SecurityRoleFormGroup = this.securityRoleFormService.createSecurityRoleFormGroup();

  constructor(
    protected securityRoleService: SecurityRoleService,
    protected securityRoleFormService: SecurityRoleFormService,
    protected securityPermissionService: SecurityPermissionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSecurityPermission = (o1: ISecurityPermission | null, o2: ISecurityPermission | null): boolean =>
    this.securityPermissionService.compareSecurityPermission(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityRole }) => {
      this.securityRole = securityRole;
      if (securityRole) {
        this.updateForm(securityRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityRole = this.securityRoleFormService.getSecurityRole(this.editForm);
    if (securityRole.id !== null) {
      this.subscribeToSaveResponse(this.securityRoleService.update(securityRole));
    } else {
      this.subscribeToSaveResponse(this.securityRoleService.create(securityRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityRole>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(securityRole: ISecurityRole): void {
    this.securityRole = securityRole;
    this.securityRoleFormService.resetForm(this.editForm, securityRole);

    this.securityPermissionsSharedCollection =
      this.securityPermissionService.addSecurityPermissionToCollectionIfMissing<ISecurityPermission>(
        this.securityPermissionsSharedCollection,
        ...(securityRole.securityPermissions ?? [])
      );
  }

  protected loadRelationshipsOptions(): void {
    this.securityPermissionService
      .query()
      .pipe(map((res: HttpResponse<ISecurityPermission[]>) => res.body ?? []))
      .pipe(
        map((securityPermissions: ISecurityPermission[]) =>
          this.securityPermissionService.addSecurityPermissionToCollectionIfMissing<ISecurityPermission>(
            securityPermissions,
            ...(this.securityRole?.securityPermissions ?? [])
          )
        )
      )
      .subscribe((securityPermissions: ISecurityPermission[]) => (this.securityPermissionsSharedCollection = securityPermissions));
  }
}
