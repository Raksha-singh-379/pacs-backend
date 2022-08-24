import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SecurityPermissionFormService } from './security-permission-form.service';
import { SecurityPermissionService } from '../service/security-permission.service';
import { ISecurityPermission } from '../security-permission.model';

import { SecurityPermissionUpdateComponent } from './security-permission-update.component';

describe('SecurityPermission Management Update Component', () => {
  let comp: SecurityPermissionUpdateComponent;
  let fixture: ComponentFixture<SecurityPermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityPermissionFormService: SecurityPermissionFormService;
  let securityPermissionService: SecurityPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SecurityPermissionUpdateComponent],
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
      .overrideTemplate(SecurityPermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityPermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityPermissionFormService = TestBed.inject(SecurityPermissionFormService);
    securityPermissionService = TestBed.inject(SecurityPermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const securityPermission: ISecurityPermission = { id: 456 };

      activatedRoute.data = of({ securityPermission });
      comp.ngOnInit();

      expect(comp.securityPermission).toEqual(securityPermission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityPermission>>();
      const securityPermission = { id: 123 };
      jest.spyOn(securityPermissionFormService, 'getSecurityPermission').mockReturnValue(securityPermission);
      jest.spyOn(securityPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityPermission }));
      saveSubject.complete();

      // THEN
      expect(securityPermissionFormService.getSecurityPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityPermissionService.update).toHaveBeenCalledWith(expect.objectContaining(securityPermission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityPermission>>();
      const securityPermission = { id: 123 };
      jest.spyOn(securityPermissionFormService, 'getSecurityPermission').mockReturnValue({ id: null });
      jest.spyOn(securityPermissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityPermission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityPermission }));
      saveSubject.complete();

      // THEN
      expect(securityPermissionFormService.getSecurityPermission).toHaveBeenCalled();
      expect(securityPermissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityPermission>>();
      const securityPermission = { id: 123 };
      jest.spyOn(securityPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityPermissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
