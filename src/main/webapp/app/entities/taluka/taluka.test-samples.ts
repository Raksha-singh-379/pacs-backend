import dayjs from 'dayjs/esm';

import { ITaluka, NewTaluka } from './taluka.model';

export const sampleWithRequiredData: ITaluka = {
  id: 3499,
  talukaName: 'Handmade quantify web-readiness',
};

export const sampleWithPartialData: ITaluka = {
  id: 56140,
  talukaName: 'Reactive engage synergize',
  deleted: false,
  lgdCode: 36326,
  lastModified: dayjs('2022-08-23T19:25'),
  lastModifiedBy: 'collaborative Missouri',
};

export const sampleWithFullData: ITaluka = {
  id: 65445,
  talukaName: 'Focused generating',
  deleted: false,
  lgdCode: 87017,
  lastModified: dayjs('2022-08-23T19:51'),
  lastModifiedBy: 'Granite mobile',
};

export const sampleWithNewData: NewTaluka = {
  talukaName: 'primary Keyboard',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
