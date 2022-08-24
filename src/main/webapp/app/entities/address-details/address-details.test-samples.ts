import dayjs from 'dayjs/esm';

import { AddressType } from 'app/entities/enumerations/address-type.model';

import { IAddressDetails, NewAddressDetails } from './address-details.model';

export const sampleWithRequiredData: IAddressDetails = {
  id: 34354,
};

export const sampleWithPartialData: IAddressDetails = {
  id: 40133,
  type: AddressType['EMPLOYMENT_ADDRESS'],
  roadName: 'Incredible Loan Squares',
  pincode: 'bypassing',
  lastModifiedBy: 'Interactions scale',
  freeField2: 'Solomon bus Avon',
  freeField3: 'Flat Agent',
};

export const sampleWithFullData: IAddressDetails = {
  id: 77306,
  type: AddressType['CURRENT_ADDRESS'],
  houseNo: 'Sausages bypass',
  roadName: 'Dollar',
  landMark: 'Shoes wireless',
  pincode: 'Home Granite',
  lastModified: dayjs('2022-08-23T13:11'),
  lastModifiedBy: 'standardization Wooden',
  createdBy: 'Table Intelligent Baby',
  createdOn: dayjs('2022-08-23T17:30'),
  isDeleted: false,
  freeField1: 'Principal Refined JSON',
  freeField2: 'Gloves enterprise',
  freeField3: 'monitoring',
};

export const sampleWithNewData: NewAddressDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
