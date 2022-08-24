import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SocietyFormService, SocietyFormGroup } from './society-form.service';
import { ISociety } from '../society.model';
import { SocietyService } from '../service/society.service';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';
import { AddressDetailsService } from 'app/entities/address-details/service/address-details.service';

@Component({
  selector: 'jhi-society-update',
  templateUrl: './society-update.component.html',
})
export class SocietyUpdateComponent implements OnInit {
  isSaving = false;
  society: ISociety | null = null;

  addressDetailsSharedCollection: IAddressDetails[] = [];
  societiesSharedCollection: ISociety[] = [];

  editForm: SocietyFormGroup = this.societyFormService.createSocietyFormGroup();

  constructor(
    protected societyService: SocietyService,
    protected societyFormService: SocietyFormService,
    protected addressDetailsService: AddressDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAddressDetails = (o1: IAddressDetails | null, o2: IAddressDetails | null): boolean =>
    this.addressDetailsService.compareAddressDetails(o1, o2);

  compareSociety = (o1: ISociety | null, o2: ISociety | null): boolean => this.societyService.compareSociety(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ society }) => {
      this.society = society;
      if (society) {
        this.updateForm(society);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const society = this.societyFormService.getSociety(this.editForm);
    if (society.id !== null) {
      this.subscribeToSaveResponse(this.societyService.update(society));
    } else {
      this.subscribeToSaveResponse(this.societyService.create(society));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISociety>>): void {
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

  protected updateForm(society: ISociety): void {
    this.society = society;
    this.societyFormService.resetForm(this.editForm, society);

    this.addressDetailsSharedCollection = this.addressDetailsService.addAddressDetailsToCollectionIfMissing<IAddressDetails>(
      this.addressDetailsSharedCollection,
      society.addressDetails
    );
    this.societiesSharedCollection = this.societyService.addSocietyToCollectionIfMissing<ISociety>(
      this.societiesSharedCollection,
      society.society
    );
  }

  protected loadRelationshipsOptions(): void {
    this.addressDetailsService
      .query()
      .pipe(map((res: HttpResponse<IAddressDetails[]>) => res.body ?? []))
      .pipe(
        map((addressDetails: IAddressDetails[]) =>
          this.addressDetailsService.addAddressDetailsToCollectionIfMissing<IAddressDetails>(addressDetails, this.society?.addressDetails)
        )
      )
      .subscribe((addressDetails: IAddressDetails[]) => (this.addressDetailsSharedCollection = addressDetails));

    this.societyService
      .query()
      .pipe(map((res: HttpResponse<ISociety[]>) => res.body ?? []))
      .pipe(map((societies: ISociety[]) => this.societyService.addSocietyToCollectionIfMissing<ISociety>(societies, this.society?.society)))
      .subscribe((societies: ISociety[]) => (this.societiesSharedCollection = societies));
  }
}
