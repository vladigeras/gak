import {Component} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {AuthService} from "../../../security/auth.service";
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import {WAIT_STRING} from "../../../app.module";

@Component({
  selector: 'login-modal',
  templateUrl: './login-modal.component.html'
})
export class LoginModalComponent {
  @BlockUI() blockUI: NgBlockUI;

  user = {login: null, password: null};

  constructor(private toast: ToastsManager, private authService: AuthService) {}

  enter() {
    this.blockUI.start(WAIT_STRING);
    this.authService.login(this.user).subscribe(
      data => {
        this.toast.success("Вы вошли в систему", "Успешно");
        setTimeout(function () {
          location.reload()
        }, 400);
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop();
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
  }

  clearWindow() {
    this.user = {login: null, password: null}
  }
}

