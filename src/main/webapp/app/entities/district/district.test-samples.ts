import dayjs from 'dayjs/esm';

import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 85653,
  districtName: 'Soap invoice Dong',
};

export const sampleWithPartialData: IDistrict = {
  id: 83275,
  districtName: 'Glens',
  lgdCode: 31911,
  lastModifiedBy: 'navigating',
};

export const sampleWithFullData: IDistrict = {
  id: 67829,
  districtName: 'executive tan Virtual',
  deleted: true,
  lgdCode: 22238,
  lastModified: dayjs('2022-08-23T13:57'),
  lastModifiedBy: 'Rubber partnerships overriding',
};

export const sampleWithNewData: NewDistrict = {
  districtName: 'override',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
