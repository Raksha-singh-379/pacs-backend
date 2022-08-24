import dayjs from 'dayjs/esm';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';

export interface ISociety {
  id: number;
  societyName?: string | null;
  registrationNumber?: number | null;
  gstinNumber?: number | null;
  panNumber?: number | null;
  tanNumber?: number | null;
  phoneNumber?: number | null;
  emailAddress?: string | null;
  createdOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  description?: string | null;
  isActivate?: boolean | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  freeField1?: string | null;
  freeField2?: string | null;
  freeField3?: string | null;
  freeField4?: string | null;
  addressDetails?: Pick<IAddressDetails, 'id'> | null;
  society?: Pick<ISociety, 'id'> | null;
}

export type NewSociety = Omit<ISociety, 'id'> & { id: null };
