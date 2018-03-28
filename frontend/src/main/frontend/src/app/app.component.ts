import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {AuthService, currentPrincipal} from "./security/auth.service";
import {SocketService} from "./service/socket.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "./app.module";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  @BlockUI() blockUI: NgBlockUI;

  constructor(private titleService: Title,
              private toast: ToastsManager, private vcr: ViewContainerRef, private authService: AuthService,
              private socketService: SocketService) {
    this.toast.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this.titleService.setTitle("GAK");
    this.authService.getCurrentPrincipal();
    this.authService.principalReady.subscribe(
      () => {
        if (currentPrincipal.roles.length > 0) {
          this.blockUI.start(waitString);
          this.socketService.connect();
          this.socketService.socketReady.subscribe((stomp) => this.blockUI.stop());
        }
      });
  }
}




