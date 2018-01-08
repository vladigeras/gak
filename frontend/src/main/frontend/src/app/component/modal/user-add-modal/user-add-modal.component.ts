import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../service/user.service";
import {ToastsManager} from "ng2-toastr";
import {HelperService} from "../../../service/helper.service";

@Component({
  selector: 'user-add-modal',
  templateUrl: './user-add-modal.component.html',
})
export class UserAddModalComponent implements OnInit {
  user = {firstname: null, middlename: null, lastname: null, login: null, password: null, roles: []};
  selectedRoles = [];
  availableRoles = [];

  roleSelectDropdownSettings = {
    singleSelection: false,
    enableCheckAll: false,
    classes: "selectRole",
    text: "Выберите роль"
  };

  constructor(private toast: ToastsManager, private userService: UserService) {}

  ngOnInit() {
    this.userService.getAvailableRoles().subscribe(
      (data: string[]) => {
        data.forEach(role => {
          this.availableRoles.push({
            id: this.availableRoles.length + 1,
            original: role,
            itemName: HelperService.convertRole(role)
          });
        })
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка")
      }
    );
  }

  add() {
    let roles = [];
    this.selectedRoles.forEach(object => roles.push(object.original));
    this.user.roles = roles;
    this.userService.addUser(this.user).subscribe(
      data => {
        this.toast.success("Пользователь был добавлен", "Успешно");

      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }

  clearWindow() {
    this.user = {firstname: null, middlename: null, lastname: null, login: null, password: null, roles: []};
    this.selectedRoles = [];
  }

}
