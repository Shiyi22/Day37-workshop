import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CameraService } from '../services/camera.service';
import { Router } from '@angular/router';
import { WebcamComponent, WebcamImage } from 'ngx-webcam';
import { Subject, Subscription } from 'rxjs';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.css']
})
export class CameraComponent implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild(WebcamComponent) webcam!: WebcamComponent;

  width=400
  height=400
  pics: string[] = []
  sub$!: Subscription
  trigger = new Subject<void>

  constructor(private router: Router, private camSvc: CameraService) { }

  // during loading page 
  ngOnInit(): void {

  }
  ngOnDestroy(): void {
    this.sub$.unsubscribe() 
  }

  // after view is completely rendered(finish loading), this will be initialized
  ngAfterViewInit(): void {
    this.webcam.trigger = this.trigger; 
    // bind instead of => function to keep listening to it using anonymous function
    this.sub$ = this.webcam.imageCapture.subscribe(
      this.snapshot.bind(this)
    ) 
  }

  snap() {
    this.trigger.next()
  }

  // this is an anonymous function
  snapshot(webcamImg: WebcamImage) {
    this.camSvc.imageData = webcamImg.imageAsDataUrl // storing latest snapshot on the service
    this.pics.push(this.camSvc.imageData)
  }

}
