import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SecurityPermissionFormService, SecurityPermissionFormGroup } from './security-permission-form.service';
import { ISecurityPermission } from '../security-permission.model';
import { SecurityPermissionService } from '../service/security-permission.service';

@Component({
  selector: 'jhi-security-permission-update',
  templateUrl: './security-permission-update.component.html',
})
export class SecurityPermissionUpdateComponent implements OnInit {
  isSaving = false;
  securityPermission: ISecurityPermission | null = null;

  editForm: SecurityPermissionFormGroup = this.securityPermissionFormService.createSecurityPermissionFormGroup();

  constructor(
    protected securityPermissionService: SecurityPermissionService,
    protected securityPermissionFormService: SecurityPermissionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityPermission }) => {
      this.securityPermission = securityPermission;
      if (securityPermission) {
        this.updateForm(securityPermission);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityPermission = this.securityPermissionFormService.getSecurityPermission(this.editForm);
    if (securityPermission.id !== null) {
      this.subscribeToSaveResponse(this.securityPermissionService.update(securityPermission));
    } else {
      this.subscribeToSaveResponse(this.securityPermissionService.create(securityPermission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityPermission>>): void {
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

  protected updateForm(securityPermission: ISecurityPermission): void {
    this.securityPermission = securityPermission;
    this.securityPermissionFormService.resetForm(this.editForm, securityPermission);
  }
}
