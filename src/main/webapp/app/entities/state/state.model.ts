import dayjs from 'dayjs/esm';

export interface IState {
  id: number;
  stateName?: string | null;
  deleted?: boolean | null;
  lgdCode?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewState = Omit<IState, 'id'> & { id: null };
