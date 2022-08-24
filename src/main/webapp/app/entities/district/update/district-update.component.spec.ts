import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DistrictFormService } from './district-form.service';
import { DistrictService } from '../service/district.service';
import { IDistrict } from '../district.model';

import { DistrictUpdateComponent } from './district-update.component';

describe('District Management Update Component', () => {
  let comp: DistrictUpdateComponent;
  let fixture: ComponentFixture<DistrictUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let districtFormService: DistrictFormService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DistrictUpdateComponent],
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
      .overrideTemplate(DistrictUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DistrictUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    districtFormService = TestBed.inject(DistrictFormService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const district: IDistrict = { id: 456 };

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(comp.district).toEqual(district);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue(district);
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(districtService.update).toHaveBeenCalledWith(expect.objectContaining(district));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue({ id: null });
      jest.spyOn(districtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(districtService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(districtService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
