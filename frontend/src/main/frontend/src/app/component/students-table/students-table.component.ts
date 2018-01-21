import { Component, OnInit } from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";

declare var $: any;

@Component({
  selector: 'app-students-table',
  templateUrl: './students-table.component.html',
  styleUrls: ['./students-table.component.scss']
})
export class StudentsTableComponent implements OnInit {

  availableGroups = [];
  selectedGroup = null;
  headers = [
    {prop: "fio", name: "ФИО"},
    {prop: "title", name: "Тема работы"},
    {prop: "mentor", name: "Руководитель"},
    {prop: "reviewer", name: "Рецензент"}
  ];
  students = [];
  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  constructor(private toast: ToastsManager, private studentService: StudentService) { }

  ngOnInit() {
    this.getAvailableGroups();
  }

  showEmptyModalForAdd() {
    $('#studentAddModal').modal('show');
  }

  getAvailableGroups() {
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
    )
  }

  getStudentsOfGroup() {
    this.students = [];
    this.studentService.getStudentsOfGroup(this.selectedGroup[0].itemName).subscribe(
      (data: any) => {
        data.forEach(student => {
          this.students.push({
              id: student.id,
              fio: student.lastname + " " + student.firstname + " " + student.middlename,
              title: student.title,
              mentor: student.mentor.lastname + " " + student.mentor.firstname + " " + student.mentor.middlename,
              mentorId: student.mentor.id,
              reviewer: student.reviewer.lastname + " " + student.reviewer.firstname + " " + student.reviewer.middlename,
              reviewerId: student.reviewer.id
            });
        });
        this.reloadTable();
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }

  reloadTable() {
    this.students = [...this.students];
  }
}
