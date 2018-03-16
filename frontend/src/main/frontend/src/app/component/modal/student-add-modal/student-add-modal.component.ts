import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {UserService} from "../../../service/user.service";
import {StudentService} from "../../../service/student.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {WAIT_STRING} from "../../../app.module";

declare var $: any;

@Component({
  selector: 'app-student-add-modal',
  templateUrl: './student-add-modal.component.html',
  styleUrls: ['./student-add-modal.component.scss']
})
export class StudentAddModalComponent implements OnInit {

  @Input() student = {
    id: null,
    firstname: null,
    middlename: null,
    lastname: null,
    title: null,
    group: null,
    mentor: null,
    reviewer: null
  };
  presentationFile: File = null;
  reportFile: File = null;
  @Input() isAddingNewStudent = true;
  availableGroups = [];
  @Input() selectedGroup = null;
  availableMentors = [];
  @Input() selectedMentor = null;
  availableReviewers = [];
  @Input() selectedReviewer = null;
  @Output() studentSaved = new EventEmitter();
  @BlockUI() blockUI: NgBlockUI;

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
    this.blockUI.start(WAIT_STRING);
    this.studentService.getAvailableGroups().subscribe(
      (data: any) => {
        data.forEach(group => this.availableGroups.push(
          {
            id: this.availableGroups.length + 1,
            itemName: group.title
          }
        ));
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop();
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
    this.blockUI.start(WAIT_STRING);
    this.userService.getUsersByRole("MENTOR").subscribe(
      (data: any) => {
        data.forEach(user => this.availableMentors.push(
          {
            id: this.availableGroups.length + 1,
            userId: user.id,
            itemName: user.lastname + " " + user.firstname + " " + user.middlename
          }
        ));
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop();
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    );
    this.blockUI.start(WAIT_STRING);
    this.userService.getUsersByRole("REVIEWER").subscribe(
      (data: any) => {
        data.forEach(user => this.availableReviewers.push(
          {
            id: this.availableGroups.length + 1,
            userId: user.id,
            itemName: user.lastname + " " + user.firstname + " " + user.middlename
          }
        ));
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
    this.student = {
      id: null,
      firstname: null,
      middlename: null,
      lastname: null,
      title: null,
      group: null,
      mentor: null,
      reviewer: null
    };
    // this.selectedGroup = [];
    this.selectedMentor = [];
    this.selectedReviewer = [];
    this.reportFile = null;
    this.presentationFile = null;
  }

  save() {
    let group = null;
    if (this.selectedGroup[0] != undefined) {
      group = {title: this.selectedGroup[0].itemName};
    }
    let mentor = null;
    if (this.selectedMentor[0] != undefined) {
      mentor = {id: this.selectedMentor[0].userId};
    }
    let reviewer = null;
    if (this.selectedReviewer[0] != undefined) {
      reviewer = {id: this.selectedReviewer[0].userId};
    }
    this.student.group = group;
    this.student.mentor = mentor;
    this.student.reviewer = reviewer;
    this.blockUI.start(WAIT_STRING);
    this.studentService.saveStudent(this.student).subscribe(
      studentId => {
        if (studentId != null) {
          this.blockUI.start(WAIT_STRING);
          this.studentService.saveFiles(studentId, this.reportFile, this.presentationFile).subscribe(
            data => {
              this.blockUI.stop();
              this.toast.success("Успешно");
              $('#studentAddModal').modal('hide');
              this.studentSaved.emit();
            },
            error => {
              this.blockUI.stop();
              if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
              else this.toast.error(error.error, "Ошибка");
            })
        } else {
          this.blockUI.stop();
          this.toast.error("Произошла ошибка")
        }
      },
      error => {
        this.blockUI.stop();
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      })
  }

  delete() {
    if (this.student.id != null) {
      this.blockUI.start(WAIT_STRING);
      this.studentService.deleteStudent(this.student.id).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Успешно");
          $('#studentAddModal').modal('hide');
          this.studentSaved.emit();
        },
        error => {
          this.blockUI.stop();
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        })
    }
  }

  getReportFile(event) {
    this.reportFile = event.target.files[0];
  }

  getPresentationFile(event) {
    this.presentationFile = event.target.files[0];
  }
}

