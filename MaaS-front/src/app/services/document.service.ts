import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DocumentService {
  private base = 'http://localhost:8080/api/documents';

  constructor(private http: HttpClient) {}

  uploadDocument(file: File, documentType: string): Observable<any> {
    const fd = new FormData();
    fd.append('file', file, file.name);
    fd.append('documentType', documentType);

    const token = localStorage.getItem('auth_token');
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : undefined;

    return this.http.post(`${this.base}/upload`, fd, { responseType: 'text' });
  }
}

