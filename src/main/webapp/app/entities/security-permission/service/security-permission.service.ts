import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISecurityPermission, NewSecurityPermission } from '../security-permission.model';

export type PartialUpdateSecurityPermission = Partial<ISecurityPermission> & Pick<ISecurityPermission, 'id'>;

type RestOf<T extends ISecurityPermission | NewSecurityPermission> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestSecurityPermission = RestOf<ISecurityPermission>;

export type NewRestSecurityPermission = RestOf<NewSecurityPermission>;

export type PartialUpdateRestSecurityPermission = RestOf<PartialUpdateSecurityPermission>;

export type EntityResponseType = HttpResponse<ISecurityPermission>;
export type EntityArrayResponseType = HttpResponse<ISecurityPermission[]>;

@Injectable({ providedIn: 'root' })
export class SecurityPermissionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityPermission: NewSecurityPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityPermission);
    return this.http
      .post<RestSecurityPermission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(securityPermission: ISecurityPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityPermission);
    return this.http
      .put<RestSecurityPermission>(`${this.resourceUrl}/${this.getSecurityPermissionIdentifier(securityPermission)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(securityPermission: PartialUpdateSecurityPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityPermission);
    return this.http
      .patch<RestSecurityPermission>(`${this.resourceUrl}/${this.getSecurityPermissionIdentifier(securityPermission)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSecurityPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSecurityPermission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSecurityPermissionIdentifier(securityPermission: Pick<ISecurityPermission, 'id'>): number {
    return securityPermission.id;
  }

  compareSecurityPermission(o1: Pick<ISecurityPermission, 'id'> | null, o2: Pick<ISecurityPermission, 'id'> | null): boolean {
    return o1 && o2 ? this.getSecurityPermissionIdentifier(o1) === this.getSecurityPermissionIdentifier(o2) : o1 === o2;
  }

  addSecurityPermissionToCollectionIfMissing<Type extends Pick<ISecurityPermission, 'id'>>(
    securityPermissionCollection: Type[],
    ...securityPermissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const securityPermissions: Type[] = securityPermissionsToCheck.filter(isPresent);
    if (securityPermissions.length > 0) {
      const securityPermissionCollectionIdentifiers = securityPermissionCollection.map(
        securityPermissionItem => this.getSecurityPermissionIdentifier(securityPermissionItem)!
      );
      const securityPermissionsToAdd = securityPermissions.filter(securityPermissionItem => {
        const securityPermissionIdentifier = this.getSecurityPermissionIdentifier(securityPermissionItem);
        if (securityPermissionCollectionIdentifiers.includes(securityPermissionIdentifier)) {
          return false;
        }
        securityPermissionCollectionIdentifiers.push(securityPermissionIdentifier);
        return true;
      });
      return [...securityPermissionsToAdd, ...securityPermissionCollection];
    }
    return securityPermissionCollection;
  }

  protected convertDateFromClient<T extends ISecurityPermission | NewSecurityPermission | PartialUpdateSecurityPermission>(
    securityPermission: T
  ): RestOf<T> {
    return {
      ...securityPermission,
      lastModified: securityPermission.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSecurityPermission: RestSecurityPermission): ISecurityPermission {
    return {
      ...restSecurityPermission,
      lastModified: restSecurityPermission.lastModified ? dayjs(restSecurityPermission.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSecurityPermission>): HttpResponse<ISecurityPermission> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSecurityPermission[]>): HttpResponse<ISecurityPermission[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
