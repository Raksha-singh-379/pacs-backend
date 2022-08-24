import dayjs from 'dayjs/esm';

import { IState, NewState } from './state.model';

export const sampleWithRequiredData: IState = {
  id: 97627,
  stateName: 'Games microchip',
};

export const sampleWithPartialData: IState = {
  id: 35175,
  stateName: 'Cambodia synergies Architect',
  deleted: false,
  lastModifiedBy: 'compressing Mill',
};

export const sampleWithFullData: IState = {
  id: 35522,
  stateName: 'Plastic',
  deleted: false,
  lgdCode: 88604,
  lastModified: dayjs('2022-08-23T11:30'),
  lastModifiedBy: 'channels Meadows Distributed',
};

export const sampleWithNewData: NewState = {
  stateName: 'e-commerce Sleek',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
