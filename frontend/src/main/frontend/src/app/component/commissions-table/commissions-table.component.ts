import { Component, OnInit } from '@angular/core';
import {HelperService} from "../../service/helper.service";
import {ToastsManager} from "ng2-toastr";
import {CommissionService} from "../../service/commission.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../../app.module";

declare var $: any;

@Component({
  selector: 'app-commissions-table',
  templateUrl: './commissions-table.component.html',
  styleUrls: ['./commissions-table.component.scss']
})
export class CommissionsTableComponent implements OnInit {

  commissions = [];
  commissionDtoForTransferRole = null;
  @BlockUI() blockUI: NgBlockUI;


  constructor(private toast: ToastsManager, private commissionService: CommissionService) { }

  ngOnInit() {
    this.getCommissionsList();
  }

  getCommissionsList() {
    this.blockUI.start(waitString);
    this.commissions = [];
    this.commissionService.getCommissionsByListId(1).subscribe(
      (data: any[]) => {
        data.forEach(c => {
          let roles = "";
          c.user.roles.forEach(r => {
            roles = roles + HelperService.convertOriginalToRole(r) + ", ";
          });
          roles = roles.trim().replace(/.$/, "");
          this.commissions.push({
            id: c.id,
            firstname: c.user.firstname,
            lastname: c.user.lastname,
            middlename: c.user.middlename,
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

  showTransferRoleModal(commissionDto) {
    $('#transferRoleConfirmModal').modal('show');
    this.commissionDtoForTransferRole = commissionDto;
  }

  transferRole() {
    if (this.commissionDtoForTransferRole != null) {
      this.blockUI.start(waitString);
      this.commissionService.transferPresidentRole(this.commissionDtoForTransferRole).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Временная передача роли проведена", "Успешно");
          $('#transferRoleConfirmModal').modal('hide');
          this.getCommissionsList();
        },
        error => {this.blockUI.stop()}
      )
    }
  }

  reloadTable() {
    this.commissions = [...this.commissions];
  }
}
