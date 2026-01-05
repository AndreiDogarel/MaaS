import { Component } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { firstValueFrom } from 'rxjs';
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
  
  async upload() {
    console.log("Upload initiated");
    this.message = '';

    const tasks: { file: File; type: string; label: string }[] = [];
    if (this.identityFile) tasks.push({ file: this.identityFile, type: 'IDENTITY_CARD', label: 'Identity card' });
    if (this.drivingFile) tasks.push({ file: this.drivingFile, type: 'DRIVING_LICENCE', label: 'Driving licence' });

    console.log(tasks.length + " files to upload");
    if (tasks.length === 0) {
      this.message = 'Please select at least one file (Identity card or Driving licence) to upload.';
      return;
    }

    this.uploading = true;

    const successes: string[] = [];
    const failures: string[] = [];

    try {
      for (const t of tasks) {
        try {
          await firstValueFrom(this.docService.uploadDocument(t.file, t.type));
          successes.push(`${t.label} (${t.file.name}) uploaded`);
        } catch (err: any) {
          failures.push(`${t.label} (${t.file.name}) failed`);
          console.error('Upload error', t.type, err);
        }
      }
      console.log("Upload process completed");
      console.log("Successes:", successes);
      console.log("Failures:", failures);
      if (failures.length === 0) {
        alert('All documents were uploaded successfully!');
      }
      

      const msgParts: string[] = [];
      if (successes.length) msgParts.push(successes.join(' and ') + '.');
      if (failures.length) msgParts.push('Some uploads failed: ' + failures.join(', ') + '.');
     
      this.message = msgParts.join(' ');
    } finally {
      this.uploading = false;
      
    }
  }
}
