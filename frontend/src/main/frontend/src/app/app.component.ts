import {Component, ViewContainerRef} from '@angular/core';
import {ToastsManager} from "ng2-toastr";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private toast: ToastsManager, private vcr: ViewContainerRef ) {
    this.toast.setRootViewContainerRef(vcr);
  }
}



