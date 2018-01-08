import {Component, OnInit, ViewContainerRef} from '@angular/core';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import {UserAddModalComponent} from "../modal/user-add-modal/user-add-modal.component";
import {LoginModalComponent} from "../modal/login-modal/login-modal.component";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
  providers: [UserAddModalComponent, LoginModalComponent]
})
export class MenuComponent implements OnInit {

  constructor (private userAddModal: UserAddModalComponent) {
  };

  ngOnInit() {
  }

  enter() {

  }

  addUser() {

  }
}
