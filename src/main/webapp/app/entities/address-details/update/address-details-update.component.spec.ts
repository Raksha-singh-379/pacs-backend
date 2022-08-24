import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AddressDetailsFormService } from './address-details-form.service';
import { AddressDetailsService } from '../service/address-details.service';
import { IAddressDetails } from '../address-details.model';
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

import { AddressDetailsUpdateComponent } from './address-details-update.component';

describe('AddressDetails Management Update Component', () => {
  let comp: AddressDetailsUpdateComponent;
  let fixture: ComponentFixture<AddressDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let addressDetailsFormService: AddressDetailsFormService;
  let addressDetailsService: AddressDetailsService;
  let stateService: StateService;
  let districtService: DistrictService;
  let talukaService: TalukaService;
  let villageService: VillageService;
  let securityUserService: SecurityUserService;
  let memberService: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AddressDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AddressDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AddressDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    addressDetailsFormService = TestBed.inject(AddressDetailsFormService);
    addressDetailsService = TestBed.inject(AddressDetailsService);
    stateService = TestBed.inject(StateService);
    districtService = TestBed.inject(DistrictService);
    talukaService = TestBed.inject(TalukaService);
    villageService = TestBed.inject(VillageService);
    securityUserService = TestBed.inject(SecurityUserService);
    memberService = TestBed.inject(MemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call State query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const state: IState = { id: 12215 };
      addressDetails.state = state;

      const stateCollection: IState[] = [{ id: 68811 }];
      jest.spyOn(stateService, 'query').mockReturnValue(of(new HttpResponse({ body: stateCollection })));
      const additionalStates = [state];
      const expectedCollection: IState[] = [...additionalStates, ...stateCollection];
      jest.spyOn(stateService, 'addStateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(stateService.query).toHaveBeenCalled();
      expect(stateService.addStateToCollectionIfMissing).toHaveBeenCalledWith(
        stateCollection,
        ...additionalStates.map(expect.objectContaining)
      );
      expect(comp.statesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call District query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const district: IDistrict = { id: 89294 };
      addressDetails.district = district;

      const districtCollection: IDistrict[] = [{ id: 22708 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining)
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Taluka query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const taluka: ITaluka = { id: 20062 };
      addressDetails.taluka = taluka;

      const talukaCollection: ITaluka[] = [{ id: 72160 }];
      jest.spyOn(talukaService, 'query').mockReturnValue(of(new HttpResponse({ body: talukaCollection })));
      const additionalTalukas = [taluka];
      const expectedCollection: ITaluka[] = [...additionalTalukas, ...talukaCollection];
      jest.spyOn(talukaService, 'addTalukaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(talukaService.query).toHaveBeenCalled();
      expect(talukaService.addTalukaToCollectionIfMissing).toHaveBeenCalledWith(
        talukaCollection,
        ...additionalTalukas.map(expect.objectContaining)
      );
      expect(comp.talukasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Village query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const taluka: IVillage = { id: 90170 };
      addressDetails.taluka = taluka;

      const villageCollection: IVillage[] = [{ id: 30001 }];
      jest.spyOn(villageService, 'query').mockReturnValue(of(new HttpResponse({ body: villageCollection })));
      const additionalVillages = [taluka];
      const expectedCollection: IVillage[] = [...additionalVillages, ...villageCollection];
      jest.spyOn(villageService, 'addVillageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(villageService.query).toHaveBeenCalled();
      expect(villageService.addVillageToCollectionIfMissing).toHaveBeenCalledWith(
        villageCollection,
        ...additionalVillages.map(expect.objectContaining)
      );
      expect(comp.villagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityUser query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const securityUser: ISecurityUser = { id: 26956 };
      addressDetails.securityUser = securityUser;

      const securityUserCollection: ISecurityUser[] = [{ id: 9950 }];
      jest.spyOn(securityUserService, 'query').mockReturnValue(of(new HttpResponse({ body: securityUserCollection })));
      const additionalSecurityUsers = [securityUser];
      const expectedCollection: ISecurityUser[] = [...additionalSecurityUsers, ...securityUserCollection];
      jest.spyOn(securityUserService, 'addSecurityUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(securityUserService.query).toHaveBeenCalled();
      expect(securityUserService.addSecurityUserToCollectionIfMissing).toHaveBeenCalledWith(
        securityUserCollection,
        ...additionalSecurityUsers.map(expect.objectContaining)
      );
      expect(comp.securityUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const member: IMember = { id: 33852 };
      addressDetails.member = member;

      const memberCollection: IMember[] = [{ id: 10384 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining)
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const addressDetails: IAddressDetails = { id: 456 };
      const state: IState = { id: 56220 };
      addressDetails.state = state;
      const district: IDistrict = { id: 41295 };
      addressDetails.district = district;
      const taluka: ITaluka = { id: 38691 };
      addressDetails.taluka = taluka;
      const taluka: IVillage = { id: 34309 };
      addressDetails.taluka = taluka;
      const securityUser: ISecurityUser = { id: 77047 };
      addressDetails.securityUser = securityUser;
      const member: IMember = { id: 86994 };
      addressDetails.member = member;

      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      expect(comp.statesSharedCollection).toContain(state);
      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.talukasSharedCollection).toContain(taluka);
      expect(comp.villagesSharedCollection).toContain(taluka);
      expect(comp.securityUsersSharedCollection).toContain(securityUser);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.addressDetails).toEqual(addressDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddressDetails>>();
      const addressDetails = { id: 123 };
      jest.spyOn(addressDetailsFormService, 'getAddressDetails').mockReturnValue(addressDetails);
      jest.spyOn(addressDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: addressDetails }));
      saveSubject.complete();

      // THEN
      expect(addressDetailsFormService.getAddressDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(addressDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(addressDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddressDetails>>();
      const addressDetails = { id: 123 };
      jest.spyOn(addressDetailsFormService, 'getAddressDetails').mockReturnValue({ id: null });
      jest.spyOn(addressDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ addressDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: addressDetails }));
      saveSubject.complete();

      // THEN
      expect(addressDetailsFormService.getAddressDetails).toHaveBeenCalled();
      expect(addressDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddressDetails>>();
      const addressDetails = { id: 123 };
      jest.spyOn(addressDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ addressDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(addressDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareState', () => {
      it('Should forward to stateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stateService, 'compareState');
        comp.compareState(entity, entity2);
        expect(stateService.compareState).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTaluka', () => {
      it('Should forward to talukaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(talukaService, 'compareTaluka');
        comp.compareTaluka(entity, entity2);
        expect(talukaService.compareTaluka).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVillage', () => {
      it('Should forward to villageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(villageService, 'compareVillage');
        comp.compareVillage(entity, entity2);
        expect(villageService.compareVillage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSecurityUser', () => {
      it('Should forward to securityUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(securityUserService, 'compareSecurityUser');
        comp.compareSecurityUser(entity, entity2);
        expect(securityUserService.compareSecurityUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMember', () => {
      it('Should forward to memberService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(memberService, 'compareMember');
        comp.compareMember(entity, entity2);
        expect(memberService.compareMember).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
