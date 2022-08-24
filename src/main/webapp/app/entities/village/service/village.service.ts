import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVillage, NewVillage } from '../village.model';

export type PartialUpdateVillage = Partial<IVillage> & Pick<IVillage, 'id'>;

type RestOf<T extends IVillage | NewVillage> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestVillage = RestOf<IVillage>;

export type NewRestVillage = RestOf<NewVillage>;

export type PartialUpdateRestVillage = RestOf<PartialUpdateVillage>;

export type EntityResponseType = HttpResponse<IVillage>;
export type EntityArrayResponseType = HttpResponse<IVillage[]>;

@Injectable({ providedIn: 'root' })
export class VillageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/villages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(village: NewVillage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(village);
    return this.http
      .post<RestVillage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(village: IVillage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(village);
    return this.http
      .put<RestVillage>(`${this.resourceUrl}/${this.getVillageIdentifier(village)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(village: PartialUpdateVillage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(village);
    return this.http
      .patch<RestVillage>(`${this.resourceUrl}/${this.getVillageIdentifier(village)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVillage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVillage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVillageIdentifier(village: Pick<IVillage, 'id'>): number {
    return village.id;
  }

  compareVillage(o1: Pick<IVillage, 'id'> | null, o2: Pick<IVillage, 'id'> | null): boolean {
    return o1 && o2 ? this.getVillageIdentifier(o1) === this.getVillageIdentifier(o2) : o1 === o2;
  }

  addVillageToCollectionIfMissing<Type extends Pick<IVillage, 'id'>>(
    villageCollection: Type[],
    ...villagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const villages: Type[] = villagesToCheck.filter(isPresent);
    if (villages.length > 0) {
      const villageCollectionIdentifiers = villageCollection.map(villageItem => this.getVillageIdentifier(villageItem)!);
      const villagesToAdd = villages.filter(villageItem => {
        const villageIdentifier = this.getVillageIdentifier(villageItem);
        if (villageCollectionIdentifiers.includes(villageIdentifier)) {
          return false;
        }
        villageCollectionIdentifiers.push(villageIdentifier);
        return true;
      });
      return [...villagesToAdd, ...villageCollection];
    }
    return villageCollection;
  }

  protected convertDateFromClient<T extends IVillage | NewVillage | PartialUpdateVillage>(village: T): RestOf<T> {
    return {
      ...village,
      lastModified: village.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVillage: RestVillage): IVillage {
    return {
      ...restVillage,
      lastModified: restVillage.lastModified ? dayjs(restVillage.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVillage>): HttpResponse<IVillage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVillage[]>): HttpResponse<IVillage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
