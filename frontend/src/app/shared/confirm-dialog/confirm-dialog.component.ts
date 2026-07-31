import { Component, HostListener } from '@angular/core';
import { ConfirmDialogService } from '../../core/confirm-dialog.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css'
})
export class ConfirmDialogComponent {
  constructor(readonly dialog: ConfirmDialogService) {}

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.dialog.options()) {
      this.dialog.respond(false);
    }
  }
}
