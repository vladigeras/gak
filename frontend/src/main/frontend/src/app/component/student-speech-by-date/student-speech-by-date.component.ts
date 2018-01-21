import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";

@Component({
  selector: 'app-student-speech-by-date',
  templateUrl: './student-speech-by-date.component.html',
  styleUrls: ['./student-speech-by-date.component.scss']
})
export class StudentSpeechByDateComponent implements OnInit {

  availableGroups = [];
  selectedGroup = null;
  resultStudentList = [];
  studentsOfSelectedGroup = [];
  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  constructor(private toast: ToastsManager, private studentService: StudentService) {}

  ngOnInit() {
    this.getAvailableGroups();
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
    this.studentsOfSelectedGroup = [];
    this.studentService.getStudentsOfGroup(this.selectedGroup[0].itemName).subscribe(
      (data: any) => {
        data.forEach(student => {
          this.studentsOfSelectedGroup.push(
            {
              id: student.id,
              name: student.lastname + " " + student.firstname + " " + student.middlename
            }
          )
        })
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }

  dragStudent(event) {
    this.resultStudentList.push(event.dragData);
    let indexOfDeletedElement = this.studentsOfSelectedGroup.indexOf(event.dragData);
    this.studentsOfSelectedGroup.splice(indexOfDeletedElement, 1);
  }
}
