import { Component } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-upload-document',
  templateUrl: './upload-document.component.html',
  styleUrls: ['./upload-document.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class UploadDocumentComponent {
  identityFile?: File;
  drivingFile?: File;
  extraFile?: File; // optional, UI shows but not sent to backend as DOC3 isn't supported by backend service
  message = '';
  uploading = false;

  constructor(private docService: DocumentService) {}
  identityFileName = '';
  drivingFileName = '';
  extraFileName = '';

  onIdentitySelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.identityFile = input.files[0];
      if (this.identityFile) this.identityFileName = this.identityFile.name;
    } else {
      this.identityFile = undefined;
    }
  }




  onDrivingSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.drivingFile = input.files[0];
      if (this.drivingFile) this.drivingFileName = this.drivingFile.name;
    } else {
      this.drivingFile = undefined;
    }
  }

  onExtraSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.extraFile = input.files[0];
      if (this.extraFile) this.extraFileName = this.extraFile.name;
    } else {
      this.extraFile = undefined;
    }
  }

  upload() {
    console.log("Upload initiated");
    const requests = [];
    this.message = '';

    if (this.identityFile) {
      requests.push(this.docService.uploadDocument(this.identityFile, 'IDENTITY_CARD'));
    }
    if (this.drivingFile) {
      requests.push(this.docService.uploadDocument(this.drivingFile, 'DRIVING_LICENCE'));
    }

    console.log(requests.length + " files to upload");
    if (requests.length === 0) {
      this.message = 'Please select at least one file (Identity card or Driving licence) to upload.';
      return;
    }

    this.uploading = true;

    forkJoin(requests).subscribe({
      next: (res) => {
        // res is an array of responses matching the order of requests
        const parts: string[] = [];
        if (this.identityFile) parts.push(`Identity card (${this.identityFile.name}) uploaded`);
        if (this.drivingFile) parts.push(`Driving licence (${this.drivingFile.name}) uploaded`);
        this.message = parts.join(' and ') + '.';
        this.uploading = false;
      },
      error: (err) => {
        console.error('Upload error', err);
        // try to show more helpful message
        if (err && err.error) {
          this.message = 'Upload failed: ' + (err.error.message || JSON.stringify(err.error));
        } else if (err && err.status) {
          this.message = `Upload failed: server responded ${err.status} ${err.statusText}`;
        } else {
          this.message = 'Upload failed: unknown error';
        }
        this.uploading = false;
      }
    });
  }
}
