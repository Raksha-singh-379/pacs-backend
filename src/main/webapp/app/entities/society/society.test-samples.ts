import dayjs from 'dayjs/esm';

import { ISociety, NewSociety } from './society.model';

export const sampleWithRequiredData: ISociety = {
  id: 88772,
  societyName: 'cutting-edge Diverse',
};

export const sampleWithPartialData: ISociety = {
  id: 62212,
  societyName: 'Intuitive mesh',
  registrationNumber: 54222,
  gstinNumber: 34216,
  emailAddress: 'Hawaii SQL Table',
  createdBy: 'next-generation Operative',
  description: 'backing back',
  lastModified: dayjs('2022-08-24T05:20'),
  freeField2: 'Group plum Optimization',
};

export const sampleWithFullData: ISociety = {
  id: 80088,
  societyName: 'platforms open-source Ergonomic',
  registrationNumber: 79027,
  gstinNumber: 88482,
  panNumber: 86516,
  tanNumber: 39216,
  phoneNumber: 99623,
  emailAddress: 'violet e-business',
  createdOn: dayjs('2022-08-24T00:40'),
  createdBy: 'Practical',
  description: 'Rubber Mission',
  isActivate: true,
  lastModified: dayjs('2022-08-24T02:40'),
  lastModifiedBy: 'mobile',
  freeField1: 'applications Associate tertiary',
  freeField2: 'Cambridgeshire orange',
  freeField3: 'application',
  freeField4: 'maroon',
};

export const sampleWithNewData: NewSociety = {
  societyName: 'Wooden Avon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
