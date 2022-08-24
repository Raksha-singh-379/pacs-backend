import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VillageFormService, VillageFormGroup } from './village-form.service';
import { IVillage } from '../village.model';
import { VillageService } from '../service/village.service';

@Component({
  selector: 'jhi-village-update',
  templateUrl: './village-update.component.html',
})
export class VillageUpdateComponent implements OnInit {
  isSaving = false;
  village: IVillage | null = null;

  editForm: VillageFormGroup = this.villageFormService.createVillageFormGroup();

  constructor(
    protected villageService: VillageService,
    protected villageFormService: VillageFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ village }) => {
      this.village = village;
      if (village) {
        this.updateForm(village);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const village = this.villageFormService.getVillage(this.editForm);
    if (village.id !== null) {
      this.subscribeToSaveResponse(this.villageService.update(village));
    } else {
      this.subscribeToSaveResponse(this.villageService.create(village));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVillage>>): void {
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

  protected updateForm(village: IVillage): void {
    this.village = village;
    this.villageFormService.resetForm(this.editForm, village);
  }
}
