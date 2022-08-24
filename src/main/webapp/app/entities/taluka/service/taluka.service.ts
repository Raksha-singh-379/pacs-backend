import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITaluka, NewTaluka } from '../taluka.model';

export type PartialUpdateTaluka = Partial<ITaluka> & Pick<ITaluka, 'id'>;

type RestOf<T extends ITaluka | NewTaluka> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestTaluka = RestOf<ITaluka>;

export type NewRestTaluka = RestOf<NewTaluka>;

export type PartialUpdateRestTaluka = RestOf<PartialUpdateTaluka>;

export type EntityResponseType = HttpResponse<ITaluka>;
export type EntityArrayResponseType = HttpResponse<ITaluka[]>;

@Injectable({ providedIn: 'root' })
export class TalukaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/talukas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taluka: NewTaluka): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taluka);
    return this.http
      .post<RestTaluka>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(taluka: ITaluka): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taluka);
    return this.http
      .put<RestTaluka>(`${this.resourceUrl}/${this.getTalukaIdentifier(taluka)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(taluka: PartialUpdateTaluka): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taluka);
    return this.http
      .patch<RestTaluka>(`${this.resourceUrl}/${this.getTalukaIdentifier(taluka)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTaluka>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTaluka[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTalukaIdentifier(taluka: Pick<ITaluka, 'id'>): number {
    return taluka.id;
  }

  compareTaluka(o1: Pick<ITaluka, 'id'> | null, o2: Pick<ITaluka, 'id'> | null): boolean {
    return o1 && o2 ? this.getTalukaIdentifier(o1) === this.getTalukaIdentifier(o2) : o1 === o2;
  }

  addTalukaToCollectionIfMissing<Type extends Pick<ITaluka, 'id'>>(
    talukaCollection: Type[],
    ...talukasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const talukas: Type[] = talukasToCheck.filter(isPresent);
    if (talukas.length > 0) {
      const talukaCollectionIdentifiers = talukaCollection.map(talukaItem => this.getTalukaIdentifier(talukaItem)!);
      const talukasToAdd = talukas.filter(talukaItem => {
        const talukaIdentifier = this.getTalukaIdentifier(talukaItem);
        if (talukaCollectionIdentifiers.includes(talukaIdentifier)) {
          return false;
        }
        talukaCollectionIdentifiers.push(talukaIdentifier);
        return true;
      });
      return [...talukasToAdd, ...talukaCollection];
    }
    return talukaCollection;
  }

  protected convertDateFromClient<T extends ITaluka | NewTaluka | PartialUpdateTaluka>(taluka: T): RestOf<T> {
    return {
      ...taluka,
      lastModified: taluka.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTaluka: RestTaluka): ITaluka {
    return {
      ...restTaluka,
      lastModified: restTaluka.lastModified ? dayjs(restTaluka.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTaluka>): HttpResponse<ITaluka> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTaluka[]>): HttpResponse<ITaluka[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
