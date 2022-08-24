import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISociety, NewSociety } from '../society.model';

export type PartialUpdateSociety = Partial<ISociety> & Pick<ISociety, 'id'>;

type RestOf<T extends ISociety | NewSociety> = Omit<T, 'createdOn' | 'lastModified'> & {
  createdOn?: string | null;
  lastModified?: string | null;
};

export type RestSociety = RestOf<ISociety>;

export type NewRestSociety = RestOf<NewSociety>;

export type PartialUpdateRestSociety = RestOf<PartialUpdateSociety>;

export type EntityResponseType = HttpResponse<ISociety>;
export type EntityArrayResponseType = HttpResponse<ISociety[]>;

@Injectable({ providedIn: 'root' })
export class SocietyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/societies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(society: NewSociety): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(society);
    return this.http
      .post<RestSociety>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(society: ISociety): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(society);
    return this.http
      .put<RestSociety>(`${this.resourceUrl}/${this.getSocietyIdentifier(society)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(society: PartialUpdateSociety): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(society);
    return this.http
      .patch<RestSociety>(`${this.resourceUrl}/${this.getSocietyIdentifier(society)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSociety>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSociety[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSocietyIdentifier(society: Pick<ISociety, 'id'>): number {
    return society.id;
  }

  compareSociety(o1: Pick<ISociety, 'id'> | null, o2: Pick<ISociety, 'id'> | null): boolean {
    return o1 && o2 ? this.getSocietyIdentifier(o1) === this.getSocietyIdentifier(o2) : o1 === o2;
  }

  addSocietyToCollectionIfMissing<Type extends Pick<ISociety, 'id'>>(
    societyCollection: Type[],
    ...societiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const societies: Type[] = societiesToCheck.filter(isPresent);
    if (societies.length > 0) {
      const societyCollectionIdentifiers = societyCollection.map(societyItem => this.getSocietyIdentifier(societyItem)!);
      const societiesToAdd = societies.filter(societyItem => {
        const societyIdentifier = this.getSocietyIdentifier(societyItem);
        if (societyCollectionIdentifiers.includes(societyIdentifier)) {
          return false;
        }
        societyCollectionIdentifiers.push(societyIdentifier);
        return true;
      });
      return [...societiesToAdd, ...societyCollection];
    }
    return societyCollection;
  }

  protected convertDateFromClient<T extends ISociety | NewSociety | PartialUpdateSociety>(society: T): RestOf<T> {
    return {
      ...society,
      createdOn: society.createdOn?.toJSON() ?? null,
      lastModified: society.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSociety: RestSociety): ISociety {
    return {
      ...restSociety,
      createdOn: restSociety.createdOn ? dayjs(restSociety.createdOn) : undefined,
      lastModified: restSociety.lastModified ? dayjs(restSociety.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSociety>): HttpResponse<ISociety> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSociety[]>): HttpResponse<ISociety[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
