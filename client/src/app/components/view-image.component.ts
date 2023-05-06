import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { FileuploadService } from '../services/fileupload.service';

@Component({
  selector: 'app-view-image',
  templateUrl: './view-image.component.html',
  styleUrls: ['./view-image.component.css']
})
export class ViewImageComponent implements OnInit, OnDestroy {

  postId = ''
  param$!: Subscription
  imageData: any // by right it is a string

  constructor(private actroute: ActivatedRoute, private fileUpSvc: FileuploadService) {}


  ngOnInit(): void {
    this.actroute.params.subscribe(
      async (params) => {
        this.postId = params['postId']
        // this.fileUpSvc.getImage(this.postId).then( (result) => {
        //   this.imageData = result.image
        // })
        let r = await this.fileUpSvc.getImage(this.postId) // this is alternative to the then catch codes 
        console.log(r)
        this.imageData = r.image // base64 text is an image
      }
    )
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe() 
  }

}
