import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SecurityRoleFormService } from './security-role-form.service';
import { SecurityRoleService } from '../service/security-role.service';
import { ISecurityRole } from '../security-role.model';
import { ISecurityPermission } from 'app/entities/security-permission/security-permission.model';
import { SecurityPermissionService } from 'app/entities/security-permission/service/security-permission.service';

import { SecurityRoleUpdateComponent } from './security-role-update.component';

describe('SecurityRole Management Update Component', () => {
  let comp: SecurityRoleUpdateComponent;
  let fixture: ComponentFixture<SecurityRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityRoleFormService: SecurityRoleFormService;
  let securityRoleService: SecurityRoleService;
  let securityPermissionService: SecurityPermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SecurityRoleUpdateComponent],
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
      .overrideTemplate(SecurityRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityRoleFormService = TestBed.inject(SecurityRoleFormService);
    securityRoleService = TestBed.inject(SecurityRoleService);
    securityPermissionService = TestBed.inject(SecurityPermissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SecurityPermission query and add missing value', () => {
      const securityRole: ISecurityRole = { id: 456 };
      const securityPermissions: ISecurityPermission[] = [{ id: 40983 }];
      securityRole.securityPermissions = securityPermissions;

      const securityPermissionCollection: ISecurityPermission[] = [{ id: 9606 }];
      jest.spyOn(securityPermissionService, 'query').mockReturnValue(of(new HttpResponse({ body: securityPermissionCollection })));
      const additionalSecurityPermissions = [...securityPermissions];
      const expectedCollection: ISecurityPermission[] = [...additionalSecurityPermissions, ...securityPermissionCollection];
      jest.spyOn(securityPermissionService, 'addSecurityPermissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ securityRole });
      comp.ngOnInit();

      expect(securityPermissionService.query).toHaveBeenCalled();
      expect(securityPermissionService.addSecurityPermissionToCollectionIfMissing).toHaveBeenCalledWith(
        securityPermissionCollection,
        ...additionalSecurityPermissions.map(expect.objectContaining)
      );
      expect(comp.securityPermissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const securityRole: ISecurityRole = { id: 456 };
      const securityPermission: ISecurityPermission = { id: 56800 };
      securityRole.securityPermissions = [securityPermission];

      activatedRoute.data = of({ securityRole });
      comp.ngOnInit();

      expect(comp.securityPermissionsSharedCollection).toContain(securityPermission);
      expect(comp.securityRole).toEqual(securityRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityRole>>();
      const securityRole = { id: 123 };
      jest.spyOn(securityRoleFormService, 'getSecurityRole').mockReturnValue(securityRole);
      jest.spyOn(securityRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityRole }));
      saveSubject.complete();

      // THEN
      expect(securityRoleFormService.getSecurityRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityRoleService.update).toHaveBeenCalledWith(expect.objectContaining(securityRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityRole>>();
      const securityRole = { id: 123 };
      jest.spyOn(securityRoleFormService, 'getSecurityRole').mockReturnValue({ id: null });
      jest.spyOn(securityRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityRole }));
      saveSubject.complete();

      // THEN
      expect(securityRoleFormService.getSecurityRole).toHaveBeenCalled();
      expect(securityRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityRole>>();
      const securityRole = { id: 123 };
      jest.spyOn(securityRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSecurityPermission', () => {
      it('Should forward to securityPermissionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(securityPermissionService, 'compareSecurityPermission');
        comp.compareSecurityPermission(entity, entity2);
        expect(securityPermissionService.compareSecurityPermission).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
