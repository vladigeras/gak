import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {AuthService} from "./security/auth.service";
import {CURRENT_PRINCIPAL} from "./security/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  principal = CURRENT_PRINCIPAL;

  constructor(private toast: ToastsManager, private vcr: ViewContainerRef, private authService: AuthService) {
    this.toast.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this.authService.getCurrentPrincipal();
  }
}



