import dayjs from 'dayjs/esm';
import { ISecurityRole } from 'app/entities/security-role/security-role.model';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';

export interface ISecurityPermission {
  id: number;
  permissionName?: string | null;
  description?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  securityRoles?: Pick<ISecurityRole, 'id' | 'roleName'>[] | null;
  securityUsers?: Pick<ISecurityUser, 'id' | 'username'>[] | null;
}

export type NewSecurityPermission = Omit<ISecurityPermission, 'id'> & { id: null };
