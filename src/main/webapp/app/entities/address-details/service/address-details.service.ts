import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAddressDetails, NewAddressDetails } from '../address-details.model';

export type PartialUpdateAddressDetails = Partial<IAddressDetails> & Pick<IAddressDetails, 'id'>;

type RestOf<T extends IAddressDetails | NewAddressDetails> = Omit<T, 'lastModified' | 'createdOn'> & {
  lastModified?: string | null;
  createdOn?: string | null;
};

export type RestAddressDetails = RestOf<IAddressDetails>;

export type NewRestAddressDetails = RestOf<NewAddressDetails>;

export type PartialUpdateRestAddressDetails = RestOf<PartialUpdateAddressDetails>;

export type EntityResponseType = HttpResponse<IAddressDetails>;
export type EntityArrayResponseType = HttpResponse<IAddressDetails[]>;

@Injectable({ providedIn: 'root' })
export class AddressDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/address-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(addressDetails: NewAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .post<RestAddressDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(addressDetails: IAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .put<RestAddressDetails>(`${this.resourceUrl}/${this.getAddressDetailsIdentifier(addressDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(addressDetails: PartialUpdateAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .patch<RestAddressDetails>(`${this.resourceUrl}/${this.getAddressDetailsIdentifier(addressDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAddressDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAddressDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAddressDetailsIdentifier(addressDetails: Pick<IAddressDetails, 'id'>): number {
    return addressDetails.id;
  }

  compareAddressDetails(o1: Pick<IAddressDetails, 'id'> | null, o2: Pick<IAddressDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getAddressDetailsIdentifier(o1) === this.getAddressDetailsIdentifier(o2) : o1 === o2;
  }

  addAddressDetailsToCollectionIfMissing<Type extends Pick<IAddressDetails, 'id'>>(
    addressDetailsCollection: Type[],
    ...addressDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const addressDetails: Type[] = addressDetailsToCheck.filter(isPresent);
    if (addressDetails.length > 0) {
      const addressDetailsCollectionIdentifiers = addressDetailsCollection.map(
        addressDetailsItem => this.getAddressDetailsIdentifier(addressDetailsItem)!
      );
      const addressDetailsToAdd = addressDetails.filter(addressDetailsItem => {
        const addressDetailsIdentifier = this.getAddressDetailsIdentifier(addressDetailsItem);
        if (addressDetailsCollectionIdentifiers.includes(addressDetailsIdentifier)) {
          return false;
        }
        addressDetailsCollectionIdentifiers.push(addressDetailsIdentifier);
        return true;
      });
      return [...addressDetailsToAdd, ...addressDetailsCollection];
    }
    return addressDetailsCollection;
  }

  protected convertDateFromClient<T extends IAddressDetails | NewAddressDetails | PartialUpdateAddressDetails>(
    addressDetails: T
  ): RestOf<T> {
    return {
      ...addressDetails,
      lastModified: addressDetails.lastModified?.toJSON() ?? null,
      createdOn: addressDetails.createdOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAddressDetails: RestAddressDetails): IAddressDetails {
    return {
      ...restAddressDetails,
      lastModified: restAddressDetails.lastModified ? dayjs(restAddressDetails.lastModified) : undefined,
      createdOn: restAddressDetails.createdOn ? dayjs(restAddressDetails.createdOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAddressDetails>): HttpResponse<IAddressDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAddressDetails[]>): HttpResponse<IAddressDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
