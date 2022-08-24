import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVillage } from '../village.model';
import { VillageService } from '../service/village.service';

@Injectable({ providedIn: 'root' })
export class VillageRoutingResolveService implements Resolve<IVillage | null> {
  constructor(protected service: VillageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVillage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((village: HttpResponse<IVillage>) => {
          if (village.body) {
            return of(village.body);
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
