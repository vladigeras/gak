import {Component, OnInit} from '@angular/core';
import {ToastsManager} from 'ng2-toastr/ng2-toastr';
import {AuthService} from "../../security/auth.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  constructor (private toast: ToastsManager, private authService: AuthService) {};

  ngOnInit() {
  }

  enter() {

  }

  addUser() {

  }

  logout() {
    this.authService.logout().subscribe(
      data => { this.toast.warning("Вы вышли из аккаунта", "Внимание")},
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }
}
