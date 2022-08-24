import dayjs from 'dayjs/esm';

export interface ITaluka {
  id: number;
  talukaName?: string | null;
  deleted?: boolean | null;
  lgdCode?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewTaluka = Omit<ITaluka, 'id'> & { id: null };
