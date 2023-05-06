import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../model/upload.result';
import { firstValueFrom } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor(private http: HttpClient) { }

  getImage(postId: string) {
    // HttpParams params = new HttpParams(); 
    return firstValueFrom( this.http.get<UploadResult>('/get-image/' + postId) ) // by right should use httparams 
  }

  getCount() {
    return firstValueFrom ( this.http.get<number>('/get-image'))
  }
}
