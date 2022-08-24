import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AddressDetailsFormService, AddressDetailsFormGroup } from './address-details-form.service';
import { IAddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';
import { IState } from 'app/entities/state/state.model';
import { StateService } from 'app/entities/state/service/state.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { ITaluka } from 'app/entities/taluka/taluka.model';
import { TalukaService } from 'app/entities/taluka/service/taluka.service';
import { IVillage } from 'app/entities/village/village.model';
import { VillageService } from 'app/entities/village/service/village.service';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { AddressType } from 'app/entities/enumerations/address-type.model';

@Component({
  selector: 'jhi-address-details-update',
  templateUrl: './address-details-update.component.html',
})
export class AddressDetailsUpdateComponent implements OnInit {
  isSaving = false;
  addressDetails: IAddressDetails | null = null;
  addressTypeValues = Object.keys(AddressType);

  statesSharedCollection: IState[] = [];
  districtsSharedCollection: IDistrict[] = [];
  talukasSharedCollection: ITaluka[] = [];
  villagesSharedCollection: IVillage[] = [];
  securityUsersSharedCollection: ISecurityUser[] = [];
  membersSharedCollection: IMember[] = [];

  editForm: AddressDetailsFormGroup = this.addressDetailsFormService.createAddressDetailsFormGroup();

  constructor(
    protected addressDetailsService: AddressDetailsService,
    protected addressDetailsFormService: AddressDetailsFormService,
    protected stateService: StateService,
    protected districtService: DistrictService,
    protected talukaService: TalukaService,
    protected villageService: VillageService,
    protected securityUserService: SecurityUserService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareState = (o1: IState | null, o2: IState | null): boolean => this.stateService.compareState(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  compareTaluka = (o1: ITaluka | null, o2: ITaluka | null): boolean => this.talukaService.compareTaluka(o1, o2);

  compareVillage = (o1: IVillage | null, o2: IVillage | null): boolean => this.villageService.compareVillage(o1, o2);

  compareSecurityUser = (o1: ISecurityUser | null, o2: ISecurityUser | null): boolean =>
    this.securityUserService.compareSecurityUser(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ addressDetails }) => {
      this.addressDetails = addressDetails;
      if (addressDetails) {
        this.updateForm(addressDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const addressDetails = this.addressDetailsFormService.getAddressDetails(this.editForm);
    if (addressDetails.id !== null) {
      this.subscribeToSaveResponse(this.addressDetailsService.update(addressDetails));
    } else {
      this.subscribeToSaveResponse(this.addressDetailsService.create(addressDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddressDetails>>): void {
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

  protected updateForm(addressDetails: IAddressDetails): void {
    this.addressDetails = addressDetails;
    this.addressDetailsFormService.resetForm(this.editForm, addressDetails);

    this.statesSharedCollection = this.stateService.addStateToCollectionIfMissing<IState>(
      this.statesSharedCollection,
      addressDetails.state
    );
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      addressDetails.district
    );
    this.talukasSharedCollection = this.talukaService.addTalukaToCollectionIfMissing<ITaluka>(
      this.talukasSharedCollection,
      addressDetails.taluka
    );
    this.villagesSharedCollection = this.villageService.addVillageToCollectionIfMissing<IVillage>(
      this.villagesSharedCollection,
      addressDetails.taluka
    );
    this.securityUsersSharedCollection = this.securityUserService.addSecurityUserToCollectionIfMissing<ISecurityUser>(
      this.securityUsersSharedCollection,
      addressDetails.securityUser
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      addressDetails.member
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stateService
      .query()
      .pipe(map((res: HttpResponse<IState[]>) => res.body ?? []))
      .pipe(map((states: IState[]) => this.stateService.addStateToCollectionIfMissing<IState>(states, this.addressDetails?.state)))
      .subscribe((states: IState[]) => (this.statesSharedCollection = states));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.addressDetails?.district)
        )
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));

    this.talukaService
      .query()
      .pipe(map((res: HttpResponse<ITaluka[]>) => res.body ?? []))
      .pipe(map((talukas: ITaluka[]) => this.talukaService.addTalukaToCollectionIfMissing<ITaluka>(talukas, this.addressDetails?.taluka)))
      .subscribe((talukas: ITaluka[]) => (this.talukasSharedCollection = talukas));

    this.villageService
      .query()
      .pipe(map((res: HttpResponse<IVillage[]>) => res.body ?? []))
      .pipe(
        map((villages: IVillage[]) => this.villageService.addVillageToCollectionIfMissing<IVillage>(villages, this.addressDetails?.taluka))
      )
      .subscribe((villages: IVillage[]) => (this.villagesSharedCollection = villages));

    this.securityUserService
      .query()
      .pipe(map((res: HttpResponse<ISecurityUser[]>) => res.body ?? []))
      .pipe(
        map((securityUsers: ISecurityUser[]) =>
          this.securityUserService.addSecurityUserToCollectionIfMissing<ISecurityUser>(securityUsers, this.addressDetails?.securityUser)
        )
      )
      .subscribe((securityUsers: ISecurityUser[]) => (this.securityUsersSharedCollection = securityUsers));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.addressDetails?.member)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));
  }
}
