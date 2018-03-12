import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {ToastsManager} from "ng2-toastr";
import {HelperService} from "../../service/helper.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {WAIT_STRING} from "../../app.module";

declare var $: any;

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.scss']
})
export class UsersTableComponent implements OnInit {
  users = [];
  headers = [
    {prop: "firstname", name: "Имя"},
    {prop: "lastname", name: "Фамилия"},
    {prop: "middlename", name: "Отчество"},
    {prop: "login", name: "Логин"},
    {prop: "roles", name: "Роль"}
  ];
  selectedUser = {firstname: null, middlename: null, lastname: null, login: null, password: null, roles: []};
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
    this.blockUI.start(WAIT_STRING);
    this.userService.getUsers().subscribe(
      (data: any[]) => {
        data.forEach(u => {
          let roles = "";
          u.roles.forEach(r => {
            roles = roles + HelperService.convertOriginalToRole(r) + ", ";
          });
          roles = roles.trim().replace(/.$/, "");
          this.users.push({
            firstname: u.firstname,
            lastname: u.lastname,
            middlename: u.middlename,
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
    this.roles = [];
    let row = event.selected[0];
    row.roles.replace("/\s/ig", "").split(",").forEach(r => {
      let originalRole = HelperService.convertRoleToOriginal(r);
      this.roles.push({
        id: this.roles.length + 1,
        original: originalRole,
        itemName: r
      })
    });
    this.selectedUser = row;
    this.isAddingNewUser = false;
    $('#userAddModal').modal('show');
  }

  showEmptyModalForAdd() {
    this.selectedUser = {firstname: null, middlename: null, lastname: null, login: null, password: null, roles: []};
    this.roles = [];
    this.isAddingNewUser = true;
    $('#userAddModal').modal('show');
  }
}
