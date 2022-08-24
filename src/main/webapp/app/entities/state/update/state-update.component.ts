import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { StateFormService, StateFormGroup } from './state-form.service';
import { IState } from '../state.model';
import { StateService } from '../service/state.service';

@Component({
  selector: 'jhi-state-update',
  templateUrl: './state-update.component.html',
})
export class StateUpdateComponent implements OnInit {
  isSaving = false;
  state: IState | null = null;

  editForm: StateFormGroup = this.stateFormService.createStateFormGroup();

  constructor(
    protected stateService: StateService,
    protected stateFormService: StateFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ state }) => {
      this.state = state;
      if (state) {
        this.updateForm(state);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const state = this.stateFormService.getState(this.editForm);
    if (state.id !== null) {
      this.subscribeToSaveResponse(this.stateService.update(state));
    } else {
      this.subscribeToSaveResponse(this.stateService.create(state));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IState>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(state: IState): void {
    this.state = state;
    this.stateFormService.resetForm(this.editForm, state);
  }
}
