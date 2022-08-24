import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IState, NewState } from '../state.model';

export type PartialUpdateState = Partial<IState> & Pick<IState, 'id'>;

type RestOf<T extends IState | NewState> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestState = RestOf<IState>;

export type NewRestState = RestOf<NewState>;

export type PartialUpdateRestState = RestOf<PartialUpdateState>;

export type EntityResponseType = HttpResponse<IState>;
export type EntityArrayResponseType = HttpResponse<IState[]>;

@Injectable({ providedIn: 'root' })
export class StateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/states');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(state: NewState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(state);
    return this.http.post<RestState>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(state: IState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(state);
    return this.http
      .put<RestState>(`${this.resourceUrl}/${this.getStateIdentifier(state)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(state: PartialUpdateState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(state);
    return this.http
      .patch<RestState>(`${this.resourceUrl}/${this.getStateIdentifier(state)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestState>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestState[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStateIdentifier(state: Pick<IState, 'id'>): number {
    return state.id;
  }

  compareState(o1: Pick<IState, 'id'> | null, o2: Pick<IState, 'id'> | null): boolean {
    return o1 && o2 ? this.getStateIdentifier(o1) === this.getStateIdentifier(o2) : o1 === o2;
  }

  addStateToCollectionIfMissing<Type extends Pick<IState, 'id'>>(
    stateCollection: Type[],
    ...statesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const states: Type[] = statesToCheck.filter(isPresent);
    if (states.length > 0) {
      const stateCollectionIdentifiers = stateCollection.map(stateItem => this.getStateIdentifier(stateItem)!);
      const statesToAdd = states.filter(stateItem => {
        const stateIdentifier = this.getStateIdentifier(stateItem);
        if (stateCollectionIdentifiers.includes(stateIdentifier)) {
          return false;
        }
        stateCollectionIdentifiers.push(stateIdentifier);
        return true;
      });
      return [...statesToAdd, ...stateCollection];
    }
    return stateCollection;
  }

  protected convertDateFromClient<T extends IState | NewState | PartialUpdateState>(state: T): RestOf<T> {
    return {
      ...state,
      lastModified: state.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restState: RestState): IState {
    return {
      ...restState,
      lastModified: restState.lastModified ? dayjs(restState.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestState>): HttpResponse<IState> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestState[]>): HttpResponse<IState[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
