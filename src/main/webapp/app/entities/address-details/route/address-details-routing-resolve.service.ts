import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';

@Injectable({ providedIn: 'root' })
export class AddressDetailsRoutingResolveService implements Resolve<IAddressDetails | null> {
  constructor(protected service: AddressDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAddressDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((addressDetails: HttpResponse<IAddressDetails>) => {
          if (addressDetails.body) {
            return of(addressDetails.body);
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
