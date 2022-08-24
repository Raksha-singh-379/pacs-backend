import dayjs from 'dayjs/esm';
import { IState } from 'app/entities/state/state.model';
import { IDistrict } from 'app/entities/district/district.model';
import { ITaluka } from 'app/entities/taluka/taluka.model';
import { IVillage } from 'app/entities/village/village.model';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { IMember } from 'app/entities/member/member.model';
import { AddressType } from 'app/entities/enumerations/address-type.model';

export interface IAddressDetails {
  id: number;
  type?: AddressType | null;
  houseNo?: string | null;
  roadName?: string | null;
  landMark?: string | null;
  pincode?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  createdBy?: string | null;
  createdOn?: dayjs.Dayjs | null;
  isDeleted?: boolean | null;
  freeField1?: string | null;
  freeField2?: string | null;
  freeField3?: string | null;
  state?: Pick<IState, 'id'> | null;
  district?: Pick<IDistrict, 'id'> | null;
  taluka?: Pick<ITaluka, 'id'> | null;
  taluka?: Pick<IVillage, 'id'> | null;
  securityUser?: Pick<ISecurityUser, 'id'> | null;
  member?: Pick<IMember, 'id'> | null;
}

export type NewAddressDetails = Omit<IAddressDetails, 'id'> & { id: null };
