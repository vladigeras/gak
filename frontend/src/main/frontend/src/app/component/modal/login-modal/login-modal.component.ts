import {Component} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {AuthService} from "../../../security/auth.service";

@Component({
  selector: 'login-modal',
  templateUrl: './login-modal.component.html'
})
export class LoginModalComponent {

  user = {login: null, password: null};

  constructor(private toast: ToastsManager, private authService: AuthService) {
  }

  enter() {
    this.authService.login(this.user).subscribe(
      data => {
        console.log(data);
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
  }

  clearWindow() {
    this.user = {login: null, password: null}
  }
}

