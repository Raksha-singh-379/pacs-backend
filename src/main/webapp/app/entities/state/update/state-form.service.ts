import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IState, NewState } from '../state.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IState for edit and NewStateFormGroupInput for create.
 */
type StateFormGroupInput = IState | PartialWithRequiredKeyOf<NewState>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IState | NewState> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type StateFormRawValue = FormValueOf<IState>;

type NewStateFormRawValue = FormValueOf<NewState>;

type StateFormDefaults = Pick<NewState, 'id' | 'deleted' | 'lastModified'>;

type StateFormGroupContent = {
  id: FormControl<StateFormRawValue['id'] | NewState['id']>;
  stateName: FormControl<StateFormRawValue['stateName']>;
  deleted: FormControl<StateFormRawValue['deleted']>;
  lgdCode: FormControl<StateFormRawValue['lgdCode']>;
  lastModified: FormControl<StateFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<StateFormRawValue['lastModifiedBy']>;
};

export type StateFormGroup = FormGroup<StateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StateFormService {
  createStateFormGroup(state: StateFormGroupInput = { id: null }): StateFormGroup {
    const stateRawValue = this.convertStateToStateRawValue({
      ...this.getFormDefaults(),
      ...state,
    });
    return new FormGroup<StateFormGroupContent>({
      id: new FormControl(
        { value: stateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      stateName: new FormControl(stateRawValue.stateName, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(stateRawValue.deleted),
      lgdCode: new FormControl(stateRawValue.lgdCode),
      lastModified: new FormControl(stateRawValue.lastModified),
      lastModifiedBy: new FormControl(stateRawValue.lastModifiedBy),
    });
  }

  getState(form: StateFormGroup): IState | NewState {
    return this.convertStateRawValueToState(form.getRawValue() as StateFormRawValue | NewStateFormRawValue);
  }

  resetForm(form: StateFormGroup, state: StateFormGroupInput): void {
    const stateRawValue = this.convertStateToStateRawValue({ ...this.getFormDefaults(), ...state });
    form.reset(
      {
        ...stateRawValue,
        id: { value: stateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deleted: false,
      lastModified: currentTime,
    };
  }

  private convertStateRawValueToState(rawState: StateFormRawValue | NewStateFormRawValue): IState | NewState {
    return {
      ...rawState,
      lastModified: dayjs(rawState.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertStateToStateRawValue(
    state: IState | (Partial<NewState> & StateFormDefaults)
  ): StateFormRawValue | PartialWithRequiredKeyOf<NewStateFormRawValue> {
    return {
      ...state,
      lastModified: state.lastModified ? state.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
