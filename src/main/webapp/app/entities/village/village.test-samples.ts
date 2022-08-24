import dayjs from 'dayjs/esm';

import { IVillage, NewVillage } from './village.model';

export const sampleWithRequiredData: IVillage = {
  id: 90840,
  villageName: 'Executive',
};

export const sampleWithPartialData: IVillage = {
  id: 34933,
  villageName: 'wireless online',
  deleted: false,
  lgdCode: 24099,
};

export const sampleWithFullData: IVillage = {
  id: 58212,
  villageName: 'portal Underpass back-end',
  deleted: false,
  lgdCode: 17108,
  lastModified: dayjs('2022-08-24T03:25'),
  lastModifiedBy: 'incentivize Branding',
};

export const sampleWithNewData: NewVillage = {
  villageName: 'Implementation seamless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
