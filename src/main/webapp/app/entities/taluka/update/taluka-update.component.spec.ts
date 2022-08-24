import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TalukaFormService } from './taluka-form.service';
import { TalukaService } from '../service/taluka.service';
import { ITaluka } from '../taluka.model';

import { TalukaUpdateComponent } from './taluka-update.component';

describe('Taluka Management Update Component', () => {
  let comp: TalukaUpdateComponent;
  let fixture: ComponentFixture<TalukaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let talukaFormService: TalukaFormService;
  let talukaService: TalukaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TalukaUpdateComponent],
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
      .overrideTemplate(TalukaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TalukaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    talukaFormService = TestBed.inject(TalukaFormService);
    talukaService = TestBed.inject(TalukaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const taluka: ITaluka = { id: 456 };

      activatedRoute.data = of({ taluka });
      comp.ngOnInit();

      expect(comp.taluka).toEqual(taluka);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaluka>>();
      const taluka = { id: 123 };
      jest.spyOn(talukaFormService, 'getTaluka').mockReturnValue(taluka);
      jest.spyOn(talukaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taluka });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taluka }));
      saveSubject.complete();

      // THEN
      expect(talukaFormService.getTaluka).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(talukaService.update).toHaveBeenCalledWith(expect.objectContaining(taluka));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaluka>>();
      const taluka = { id: 123 };
      jest.spyOn(talukaFormService, 'getTaluka').mockReturnValue({ id: null });
      jest.spyOn(talukaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taluka: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taluka }));
      saveSubject.complete();

      // THEN
      expect(talukaFormService.getTaluka).toHaveBeenCalled();
      expect(talukaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaluka>>();
      const taluka = { id: 123 };
      jest.spyOn(talukaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taluka });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(talukaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
