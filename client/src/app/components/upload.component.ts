import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { CameraService } from '../services/camera.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  
  imageData = "" 
  blob!: Blob
  form!: FormGroup

  constructor(private router: Router, private fb: FormBuilder, private camSvc: CameraService) {}

  ngOnInit(): void {
    // no image data
    if (!this.camSvc.imageData) {
      this.router.navigate(['/'])
      return;  
    }

    this.imageData = this.camSvc.imageData // passing data via service from child to child component 
    this.createForm(); 
    this.blob = this.dataURItoBlob(this.imageData) 
  }

  createForm() {
    this.form = this.fb.group({
      title: this.fb.control<string>(''), 
      complain: this.fb.control<string>('')
    })
  }

  // convert data URL to blob
  dataURItoBlob(dataURI: string) {
    var byteString = atob(dataURI.split(',')[1])
    let mimeString = dataURI.split(',')[0].split(';')[0]
    var ar = new ArrayBuffer(byteString.length)
    var ai = new Uint8Array(ar) 
    for (var i = 0; i < byteString.length; i++) {
      ai[i] = byteString.charCodeAt(i)
    } 
    return new Blob([ar], {type: mimeString}) 
  }

  upload() {
    const formVal = this.form.value
    this.camSvc.upload(formVal, this.blob).then(
      (result) => {
        this.router.navigate(['/'])
      }
    ).catch( (err) => {
      console.log(err)
    })
  }

}
