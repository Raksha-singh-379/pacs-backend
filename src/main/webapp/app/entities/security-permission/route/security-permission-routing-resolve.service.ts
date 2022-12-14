import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISecurityPermission } from '../security-permission.model';
import { SecurityPermissionService } from '../service/security-permission.service';

@Injectable({ providedIn: 'root' })
export class SecurityPermissionRoutingResolveService implements Resolve<ISecurityPermission | null> {
  constructor(protected service: SecurityPermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISecurityPermission | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((securityPermission: HttpResponse<ISecurityPermission>) => {
          if (securityPermission.body) {
            return of(securityPermission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
