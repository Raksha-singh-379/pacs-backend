import dayjs from 'dayjs/esm';

import { ISecurityPermission, NewSecurityPermission } from './security-permission.model';

export const sampleWithRequiredData: ISecurityPermission = {
  id: 9587,
  permissionName: 'real-time Louisiana',
};

export const sampleWithPartialData: ISecurityPermission = {
  id: 19640,
  permissionName: 'vortals parallelism',
};

export const sampleWithFullData: ISecurityPermission = {
  id: 20561,
  permissionName: 'invoice THX clicks-and-mortar',
  description: 'U.S. AGP Quality-focused',
  lastModified: dayjs('2022-08-23T23:02'),
  lastModifiedBy: 'Route',
};

export const sampleWithNewData: NewSecurityPermission = {
  permissionName: 'Avon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
