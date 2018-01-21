import {Component, Input, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {UserService} from "../../../service/user.service";
import {StudentService} from "../../../service/student.service";

declare var $: any;

@Component({
  selector: 'app-student-add-modal',
  templateUrl: './student-add-modal.component.html',
  styleUrls: ['./student-add-modal.component.scss']
})
export class StudentAddModalComponent implements OnInit {

  @Input() student = {id: null, firstname: null, middlename: null, lastname: null, title: null, group: null, mentor: null, reviewer: null};
  @Input() isAddingNewStudent = true;
  availableGroups = [];
  @Input() selectedGroup = null;
  availableMentors = [];
  @Input() selectedMentor = null;
  availableReviewers = [];
  @Input() selectedReviewer = null;

  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  mentorSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите руководителя"
  };

  reviewerSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите рецензента"
  };

  constructor(private toast: ToastsManager, private userService: UserService, private studentService: StudentService) {
  }

  ngOnInit() {
    this.studentService.getAvailableGroups().subscribe(
      (data: any) => {
        data.forEach(group => this.availableGroups.push(
          {
            id: this.availableGroups.length + 1,
            itemName: group.title
          }
        ))
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
    this.userService.getUsersByRole("MENTOR").subscribe(
      (data: any) => {
        data.forEach(user => this.availableMentors.push(
          {
            id: this.availableGroups.length + 1,
            userId: user.id,
            itemName: user.lastname + " " + user.firstname + " " + user.middlename
          }
        ))
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
    this.userService.getUsersByRole("REVIEWER").subscribe(
      (data: any) => {
        data.forEach(user => this.availableReviewers.push(
          {
            id: this.availableGroups.length + 1,
            userId: user.id,
            itemName: user.lastname + " " + user.firstname + " " + user.middlename
          }
        ))
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
  }

  clearWindow() {
    this.student = {id: null, firstname: null, middlename: null, lastname: null, title: null, group: null, mentor: null, reviewer: null};
    this.selectedGroup = [];
    this.selectedMentor = [];
    this.selectedReviewer = [];
  }

  save() {
    let group = {title: this.selectedGroup[0].itemName};
    let mentor = {id: this.selectedMentor[0].userId};
    let reviewer = {id: this.selectedReviewer[0].userId};
    this.student.group = group;
    this.student.mentor = mentor;
    this.student.reviewer = reviewer;
    this.studentService.saveStudent(this.student).subscribe(
      data => {
        this.toast.success("Успешно");
        $('#studentAddModal').modal('hide');
        this.clearWindow();
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }
}

