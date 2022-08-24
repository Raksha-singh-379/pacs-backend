import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './address-details-delete-dialog.component.html',
})
export class AddressDetailsDeleteDialogComponent {
  addressDetails?: IAddressDetails;

  constructor(protected addressDetailsService: AddressDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.addressDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
