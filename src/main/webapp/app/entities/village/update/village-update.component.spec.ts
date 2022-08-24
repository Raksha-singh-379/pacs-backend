import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VillageFormService } from './village-form.service';
import { VillageService } from '../service/village.service';
import { IVillage } from '../village.model';

import { VillageUpdateComponent } from './village-update.component';

describe('Village Management Update Component', () => {
  let comp: VillageUpdateComponent;
  let fixture: ComponentFixture<VillageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let villageFormService: VillageFormService;
  let villageService: VillageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VillageUpdateComponent],
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
      .overrideTemplate(VillageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VillageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    villageFormService = TestBed.inject(VillageFormService);
    villageService = TestBed.inject(VillageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const village: IVillage = { id: 456 };

      activatedRoute.data = of({ village });
      comp.ngOnInit();

      expect(comp.village).toEqual(village);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVillage>>();
      const village = { id: 123 };
      jest.spyOn(villageFormService, 'getVillage').mockReturnValue(village);
      jest.spyOn(villageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: village }));
      saveSubject.complete();

      // THEN
      expect(villageFormService.getVillage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(villageService.update).toHaveBeenCalledWith(expect.objectContaining(village));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVillage>>();
      const village = { id: 123 };
      jest.spyOn(villageFormService, 'getVillage').mockReturnValue({ id: null });
      jest.spyOn(villageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: village }));
      saveSubject.complete();

      // THEN
      expect(villageFormService.getVillage).toHaveBeenCalled();
      expect(villageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVillage>>();
      const village = { id: 123 };
      jest.spyOn(villageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(villageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
