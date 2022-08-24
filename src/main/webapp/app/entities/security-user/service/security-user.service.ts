import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISecurityUser, NewSecurityUser } from '../security-user.model';

export type PartialUpdateSecurityUser = Partial<ISecurityUser> & Pick<ISecurityUser, 'id'>;

type RestOf<T extends ISecurityUser | NewSecurityUser> = Omit<T, 'resetDate' | 'createdOn'> & {
  resetDate?: string | null;
  createdOn?: string | null;
};

export type RestSecurityUser = RestOf<ISecurityUser>;

export type NewRestSecurityUser = RestOf<NewSecurityUser>;

export type PartialUpdateRestSecurityUser = RestOf<PartialUpdateSecurityUser>;

export type EntityResponseType = HttpResponse<ISecurityUser>;
export type EntityArrayResponseType = HttpResponse<ISecurityUser[]>;

@Injectable({ providedIn: 'root' })
export class SecurityUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityUser: NewSecurityUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityUser);
    return this.http
      .post<RestSecurityUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(securityUser: ISecurityUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityUser);
    return this.http
      .put<RestSecurityUser>(`${this.resourceUrl}/${this.getSecurityUserIdentifier(securityUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(securityUser: PartialUpdateSecurityUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityUser);
    return this.http
      .patch<RestSecurityUser>(`${this.resourceUrl}/${this.getSecurityUserIdentifier(securityUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSecurityUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSecurityUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSecurityUserIdentifier(securityUser: Pick<ISecurityUser, 'id'>): number {
    return securityUser.id;
  }

  compareSecurityUser(o1: Pick<ISecurityUser, 'id'> | null, o2: Pick<ISecurityUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getSecurityUserIdentifier(o1) === this.getSecurityUserIdentifier(o2) : o1 === o2;
  }

  addSecurityUserToCollectionIfMissing<Type extends Pick<ISecurityUser, 'id'>>(
    securityUserCollection: Type[],
    ...securityUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const securityUsers: Type[] = securityUsersToCheck.filter(isPresent);
    if (securityUsers.length > 0) {
      const securityUserCollectionIdentifiers = securityUserCollection.map(
        securityUserItem => this.getSecurityUserIdentifier(securityUserItem)!
      );
      const securityUsersToAdd = securityUsers.filter(securityUserItem => {
        const securityUserIdentifier = this.getSecurityUserIdentifier(securityUserItem);
        if (securityUserCollectionIdentifiers.includes(securityUserIdentifier)) {
          return false;
        }
        securityUserCollectionIdentifiers.push(securityUserIdentifier);
        return true;
      });
      return [...securityUsersToAdd, ...securityUserCollection];
    }
    return securityUserCollection;
  }

  protected convertDateFromClient<T extends ISecurityUser | NewSecurityUser | PartialUpdateSecurityUser>(securityUser: T): RestOf<T> {
    return {
      ...securityUser,
      resetDate: securityUser.resetDate?.toJSON() ?? null,
      createdOn: securityUser.createdOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSecurityUser: RestSecurityUser): ISecurityUser {
    return {
      ...restSecurityUser,
      resetDate: restSecurityUser.resetDate ? dayjs(restSecurityUser.resetDate) : undefined,
      createdOn: restSecurityUser.createdOn ? dayjs(restSecurityUser.createdOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSecurityUser>): HttpResponse<ISecurityUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSecurityUser[]>): HttpResponse<ISecurityUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
