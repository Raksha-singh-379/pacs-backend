import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaluka } from '../taluka.model';
import { TalukaService } from '../service/taluka.service';

@Injectable({ providedIn: 'root' })
export class TalukaRoutingResolveService implements Resolve<ITaluka | null> {
  constructor(protected service: TalukaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaluka | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taluka: HttpResponse<ITaluka>) => {
          if (taluka.body) {
            return of(taluka.body);
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
