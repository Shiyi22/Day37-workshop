import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../model/upload.result';
import { firstValueFrom } from 'rxjs'; 

@Injectable({
  providedIn: 'root'
})
export class CameraService {

  imageData = '';  

  constructor(private http: HttpClient) { }

  // the key must match server side controller @RequestPart
  upload(form: any, image: Blob): Promise<any> {
    const formData = new FormData()
    formData.set('title', form['title'])
    formData.set('complain', form['complain'])
    formData.set('file', image)

    // url is localhost:8080/upload based on proxy config file 
    return firstValueFrom( this.http.post<UploadResult>("/upload", formData) )
  }
}
