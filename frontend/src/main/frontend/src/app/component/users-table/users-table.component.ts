import {Component, Input, OnInit, Output} from '@angular/core';
import {UserService} from "../../service/user.service";
import {ToastsManager} from "ng2-toastr";
import {HelperService} from "../../service/helper.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../../app.module";

declare var $: any;

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.scss']
})
export class UsersTableComponent implements OnInit {
  users = [];
  headers = [
    {prop: "lastname", name: "Фамилия"},
    {prop: "firstname", name: "Имя"},
    {prop: "middlename", name: "Отчество"},
    {prop: "gender", name: "Пол"},
    {prop: "login", name: "Логин"},
    {prop: "roles", name: "Роль"}
  ];
  selectedUser = {
    id: null,
    firstname: null,
    middlename: null,
    lastname: null,
    gender: null,
    login: null,
    password: null,
    roles: []
  };
  roles = [];
  isAddingNewUser;
  @BlockUI() blockUI: NgBlockUI;

  constructor(private toast: ToastsManager, private userService: UserService) {
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    this.users = [];
    this.blockUI.start(waitString);
    this.userService.getUsers().subscribe(
      (data: any[]) => {
        data.forEach(u => {
          let roles = "";
          u.roles.forEach(r => {
            roles = roles + HelperService.convertOriginalToRole(r) + ", ";
          });
          roles = roles.trim().replace(/.$/, "");
          this.users.push({
            id: u.id,
            firstname: u.firstname,
            lastname: u.lastname,
            middlename: u.middlename,
            gender: HelperService.convertGenderToRussian(u.gender),
            login: u.login,
            roles: roles
          });
        });
        this.reloadTable();
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop();
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }

  reloadTable() {
    this.users = [...this.users];
  }

  selectUserToUpdate(event) {
    let row = event.selected[0];
    if (JSON.stringify(this.selectedUser) === JSON.stringify(row)) {
      return;
    }

    this.clearSelected();

    this.selectedUser = row;
    row.roles.split(", ").forEach(r => {
      let originalRole = HelperService.convertRoleToOriginal(r);
      this.roles.push({
        id: this.roles.length + 1,
        original: originalRole,
        itemName: r
      })
    });
    this.isAddingNewUser = false;
    $('#userAddModal').modal({backdrop: 'static', keyboard: false});
  }

  showEmptyModalForAdd() {
    this.clearSelected();
    this.isAddingNewUser = true;
    $('#userAddModal').modal({backdrop: 'static', keyboard: false});
  }

  clearSelected() {
    this.selectedUser = {
      id: null,
      firstname: null,
      middlename: null,
      lastname: null,
      gender: null,
      login: null,
      password: null,
      roles: []
    };
    this.roles = [];
  }
}
