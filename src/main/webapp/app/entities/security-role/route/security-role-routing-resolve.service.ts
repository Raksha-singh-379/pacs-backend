import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISecurityRole } from '../security-role.model';
import { SecurityRoleService } from '../service/security-role.service';

@Injectable({ providedIn: 'root' })
export class SecurityRoleRoutingResolveService implements Resolve<ISecurityRole | null> {
  constructor(protected service: SecurityRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISecurityRole | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((securityRole: HttpResponse<ISecurityRole>) => {
          if (securityRole.body) {
            return of(securityRole.body);
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
