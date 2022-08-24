import dayjs from 'dayjs/esm';
import { ISociety } from 'app/entities/society/society.model';
import { ISecurityPermission } from 'app/entities/security-permission/security-permission.model';
import { ISecurityRole } from 'app/entities/security-role/security-role.model';

export interface ISecurityUser {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  designation?: string | null;
  username?: string | null;
  passwordHash?: string | null;
  email?: string | null;
  description?: string | null;
  department?: string | null;
  imageUrl?: string | null;
  activated?: boolean | null;
  langKey?: string | null;
  activationKey?: string | null;
  resetKey?: string | null;
  resetDate?: dayjs.Dayjs | null;
  mobileNo?: string | null;
  createdBy?: string | null;
  createdOn?: dayjs.Dayjs | null;
  society?: Pick<ISociety, 'id'> | null;
  securityPermissions?: Pick<ISecurityPermission, 'id' | 'permissionName'>[] | null;
  securityRoles?: Pick<ISecurityRole, 'id' | 'roleName'>[] | null;
}

export type NewSecurityUser = Omit<ISecurityUser, 'id'> & { id: null };
