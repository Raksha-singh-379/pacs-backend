import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISecurityRole, NewSecurityRole } from '../security-role.model';

export type PartialUpdateSecurityRole = Partial<ISecurityRole> & Pick<ISecurityRole, 'id'>;

type RestOf<T extends ISecurityRole | NewSecurityRole> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestSecurityRole = RestOf<ISecurityRole>;

export type NewRestSecurityRole = RestOf<NewSecurityRole>;

export type PartialUpdateRestSecurityRole = RestOf<PartialUpdateSecurityRole>;

export type EntityResponseType = HttpResponse<ISecurityRole>;
export type EntityArrayResponseType = HttpResponse<ISecurityRole[]>;

@Injectable({ providedIn: 'root' })
export class SecurityRoleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityRole: NewSecurityRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityRole);
    return this.http
      .post<RestSecurityRole>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(securityRole: ISecurityRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityRole);
    return this.http
      .put<RestSecurityRole>(`${this.resourceUrl}/${this.getSecurityRoleIdentifier(securityRole)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(securityRole: PartialUpdateSecurityRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(securityRole);
    return this.http
      .patch<RestSecurityRole>(`${this.resourceUrl}/${this.getSecurityRoleIdentifier(securityRole)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSecurityRole>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSecurityRole[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSecurityRoleIdentifier(securityRole: Pick<ISecurityRole, 'id'>): number {
    return securityRole.id;
  }

  compareSecurityRole(o1: Pick<ISecurityRole, 'id'> | null, o2: Pick<ISecurityRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getSecurityRoleIdentifier(o1) === this.getSecurityRoleIdentifier(o2) : o1 === o2;
  }

  addSecurityRoleToCollectionIfMissing<Type extends Pick<ISecurityRole, 'id'>>(
    securityRoleCollection: Type[],
    ...securityRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const securityRoles: Type[] = securityRolesToCheck.filter(isPresent);
    if (securityRoles.length > 0) {
      const securityRoleCollectionIdentifiers = securityRoleCollection.map(
        securityRoleItem => this.getSecurityRoleIdentifier(securityRoleItem)!
      );
      const securityRolesToAdd = securityRoles.filter(securityRoleItem => {
        const securityRoleIdentifier = this.getSecurityRoleIdentifier(securityRoleItem);
        if (securityRoleCollectionIdentifiers.includes(securityRoleIdentifier)) {
          return false;
        }
        securityRoleCollectionIdentifiers.push(securityRoleIdentifier);
        return true;
      });
      return [...securityRolesToAdd, ...securityRoleCollection];
    }
    return securityRoleCollection;
  }

  protected convertDateFromClient<T extends ISecurityRole | NewSecurityRole | PartialUpdateSecurityRole>(securityRole: T): RestOf<T> {
    return {
      ...securityRole,
      lastModified: securityRole.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSecurityRole: RestSecurityRole): ISecurityRole {
    return {
      ...restSecurityRole,
      lastModified: restSecurityRole.lastModified ? dayjs(restSecurityRole.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSecurityRole>): HttpResponse<ISecurityRole> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSecurityRole[]>): HttpResponse<ISecurityRole[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
