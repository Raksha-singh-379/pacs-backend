import dayjs from 'dayjs/esm';

export interface IVillage {
  id: number;
  villageName?: string | null;
  deleted?: boolean | null;
  lgdCode?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewVillage = Omit<IVillage, 'id'> & { id: null };
