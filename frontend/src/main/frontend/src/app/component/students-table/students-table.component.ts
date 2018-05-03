import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../../app.module";
import {HelperService} from "../../service/helper.service";

declare var $: any;

@Component({
  selector: 'app-students-table',
  templateUrl: './students-table.component.html',
  styleUrls: ['./students-table.component.scss']
})
export class StudentsTableComponent implements OnInit {
  availableGroups = [];
  selectedGroup = [];
  selectedMentor = [];
  selectedReviewer = [];
  selectedStudent = {
    id: null,
    firstname: null,
    middlename: null,
    lastname: null,
    gender: null,
    title: null,
    executionPlace: null,
    group: null,
    mentor: null,
    reviewer: null
  };
  isAddingNewStudent;
  students = [];
  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };
  @BlockUI() blockUI: NgBlockUI;

  constructor(private toast: ToastsManager, private studentService: StudentService) {
  }

  ngOnInit() {
    this.getAvailableGroups();
  }

  showEmptyModalForAdd() {
    this.clearSelected()
    this.isAddingNewStudent = true;
    $('#studentAddModal').modal({backdrop: 'static', keyboard: false});
  }

  getAvailableGroups() {
    this.blockUI.start(waitString);
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
    )
  }

  getStudentsOfGroup() {
    if (this.selectedGroup[0] != undefined) {
      this.students = [];
      this.blockUI.start(waitString);
      this.studentService.getStudentsOfGroup(this.selectedGroup[0].itemName).subscribe(
        (data: any) => {
          data.forEach(student => {
            let isHaveReport = (student.report != null);
            let isHavePresentation = (student.presentation != null);
            this.students.push({
              id: student.id,
              fio: student.lastname + " " + student.firstname + " " + student.middlename,
              gender: HelperService.convertGenderToRussian(student.gender),
              title: student.title,
              executionPlace: student.executionPlace,
              mentor: student.mentor.lastname + " " + student.mentor.firstname + " " + student.mentor.middlename,
              mentorId: student.mentor.id,
              reviewer: student.reviewer.lastname + " " + student.reviewer.firstname + " " + student.reviewer.middlename,
              reviewerId: student.reviewer.id,
              report: isHaveReport,
              presentation: isHavePresentation
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
  }

  reloadTable() {
    this.students = [...this.students];
  }

  selectStudentToUpdate(event) {
    if (event.type === "click") {
      if (event.cellIndex != 5 && event.cellIndex != 6) {   //this is a buttons for files
        this.clearSelected();

        let row = event.row;
        let fio = row.fio;
        fio = fio.trim().split(" ");
        this.selectedStudent.id = row.id;
        this.selectedStudent.lastname = fio[0];
        this.selectedStudent.firstname = fio[1];
        this.selectedStudent.middlename = fio[2];
        this.selectedStudent.gender = row.gender;
        this.selectedStudent.title = row.title;
        this.selectedStudent.executionPlace = row.executionPlace;
        this.selectedMentor.push({
          id: 1,
          itemName: row.mentor,
          userId: row.mentorId,
        });
        this.selectedReviewer.push({
          id: 1,
          itemName: row.reviewer,
          userId: row.reviewerId
        });
        this.isAddingNewStudent = false;
        $('#studentAddModal').modal({backdrop: 'static', keyboard: false});
      }
    }
  }

  readFile(row, isReport: boolean) {
    let studentId = row.id;
    let tab = window.open();
    this.studentService.readFile(studentId, isReport).subscribe(fileData => {
      const fileUrl = URL.createObjectURL(fileData);
      tab.location.href = fileUrl;
    });
  }

  clearSelected() {
    this.selectedStudent = {
      id: null,
      firstname: null,
      middlename: null,
      lastname: null,
      gender: null,
      title: null,
      executionPlace: null,
      group: null,
      mentor: null,
      reviewer: null
    };
    this.selectedReviewer = [];
    this.selectedMentor = [];
  }
}
