import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SocietyFormService } from './society-form.service';
import { SocietyService } from '../service/society.service';
import { ISociety } from '../society.model';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';
import { AddressDetailsService } from 'app/entities/address-details/service/address-details.service';

import { SocietyUpdateComponent } from './society-update.component';

describe('Society Management Update Component', () => {
  let comp: SocietyUpdateComponent;
  let fixture: ComponentFixture<SocietyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let societyFormService: SocietyFormService;
  let societyService: SocietyService;
  let addressDetailsService: AddressDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SocietyUpdateComponent],
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
      .overrideTemplate(SocietyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocietyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    societyFormService = TestBed.inject(SocietyFormService);
    societyService = TestBed.inject(SocietyService);
    addressDetailsService = TestBed.inject(AddressDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AddressDetails query and add missing value', () => {
      const society: ISociety = { id: 456 };
      const addressDetails: IAddressDetails = { id: 71732 };
      society.addressDetails = addressDetails;

      const addressDetailsCollection: IAddressDetails[] = [{ id: 32158 }];
      jest.spyOn(addressDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: addressDetailsCollection })));
      const additionalAddressDetails = [addressDetails];
      const expectedCollection: IAddressDetails[] = [...additionalAddressDetails, ...addressDetailsCollection];
      jest.spyOn(addressDetailsService, 'addAddressDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ society });
      comp.ngOnInit();

      expect(addressDetailsService.query).toHaveBeenCalled();
      expect(addressDetailsService.addAddressDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        addressDetailsCollection,
        ...additionalAddressDetails.map(expect.objectContaining)
      );
      expect(comp.addressDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Society query and add missing value', () => {
      const society: ISociety = { id: 456 };
      const society: ISociety = { id: 41577 };
      society.society = society;

      const societyCollection: ISociety[] = [{ id: 83249 }];
      jest.spyOn(societyService, 'query').mockReturnValue(of(new HttpResponse({ body: societyCollection })));
      const additionalSocieties = [society];
      const expectedCollection: ISociety[] = [...additionalSocieties, ...societyCollection];
      jest.spyOn(societyService, 'addSocietyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ society });
      comp.ngOnInit();

      expect(societyService.query).toHaveBeenCalled();
      expect(societyService.addSocietyToCollectionIfMissing).toHaveBeenCalledWith(
        societyCollection,
        ...additionalSocieties.map(expect.objectContaining)
      );
      expect(comp.societiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const society: ISociety = { id: 456 };
      const addressDetails: IAddressDetails = { id: 49429 };
      society.addressDetails = addressDetails;
      const society: ISociety = { id: 20492 };
      society.society = society;

      activatedRoute.data = of({ society });
      comp.ngOnInit();

      expect(comp.addressDetailsSharedCollection).toContain(addressDetails);
      expect(comp.societiesSharedCollection).toContain(society);
      expect(comp.society).toEqual(society);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISociety>>();
      const society = { id: 123 };
      jest.spyOn(societyFormService, 'getSociety').mockReturnValue(society);
      jest.spyOn(societyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ society });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: society }));
      saveSubject.complete();

      // THEN
      expect(societyFormService.getSociety).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(societyService.update).toHaveBeenCalledWith(expect.objectContaining(society));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISociety>>();
      const society = { id: 123 };
      jest.spyOn(societyFormService, 'getSociety').mockReturnValue({ id: null });
      jest.spyOn(societyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ society: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: society }));
      saveSubject.complete();

      // THEN
      expect(societyFormService.getSociety).toHaveBeenCalled();
      expect(societyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISociety>>();
      const society = { id: 123 };
      jest.spyOn(societyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ society });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(societyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAddressDetails', () => {
      it('Should forward to addressDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(addressDetailsService, 'compareAddressDetails');
        comp.compareAddressDetails(entity, entity2);
        expect(addressDetailsService.compareAddressDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSociety', () => {
      it('Should forward to societyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(societyService, 'compareSociety');
        comp.compareSociety(entity, entity2);
        expect(societyService.compareSociety).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
