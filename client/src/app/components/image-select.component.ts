import { Component, OnInit } from '@angular/core';
import { FileuploadService } from '../services/fileupload.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-image-select',
  templateUrl: './image-select.component.html',
  styleUrls: ['./image-select.component.css']
})
export class ImageSelectComponent implements OnInit {

  count!: number

  constructor(private fileuploadsvc: FileuploadService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.count = await this.fileuploadsvc.getCount(); 
  }

  range(count: number): number[] {
    return Array.from({length: count}, (_, i) => i + 1);
  }
  
  getImage(postId: number) {
    this.router.navigate(['/image', postId]);
  }


}
