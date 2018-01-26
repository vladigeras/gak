import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";
import {SpeakerService} from "../../service/speaker.service";
import * as moment from 'moment';

@Component({
  selector: 'app-student-speech-by-date',
  templateUrl: './student-speech-by-date.component.html',
  styleUrls: ['./student-speech-by-date.component.scss']
})
export class StudentSpeechByDateComponent implements OnInit {

  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  availableGroups = [];
  selectedGroup = [];
  studentsOfSelectedGroup = [];
  resultStudentMainList = [];
  resultStudentSubList = [];

  mainDate = null;
  subDate = null;

  constructor(private toast: ToastsManager, private studentService: StudentService, private speakerService: SpeakerService) {}

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
    if (this.selectedGroup[0] != null) {
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
  }

  getSpeakersListOfGroup() {
    if (this.selectedGroup[0] != null) {
      this.speakerService.getSpeakersListOfGroup(this.selectedGroup[0].itemName).subscribe(
        (data: any) => {
          data.forEach(speaker => {
          })
        },
        error => {
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        }
      )
    }
  }

  dragStudent(event, isToMainTable) {
    if (isToMainTable) {
      this.resultStudentMainList.push(event.dragData);
    } else {
      this.resultStudentSubList.push(event.dragData);
    }
    let indexOfDeletedElement = this.studentsOfSelectedGroup.indexOf(event.dragData);
    this.studentsOfSelectedGroup.splice(indexOfDeletedElement, 1);
  }

  deleteFromTable(element, isFromMainTable) {
    if (isFromMainTable) {
      let indexOfDeletedElement = this.resultStudentMainList.indexOf(element);
      this.resultStudentMainList.splice(indexOfDeletedElement, 1);
    } else {
      let indexOfDeletedElement = this.resultStudentSubList.indexOf(element);
      this.resultStudentSubList.splice(indexOfDeletedElement, 1);
    }
    this.studentsOfSelectedGroup.push(element);
  }

  save() {
    let speakersDto = [];
    if (this.selectedGroup[0] != null) {
      this.studentsOfSelectedGroup.forEach(s => {
        //this speakers will be remove from group speaker list from server, because they haven't a date (null)
        speakersDto.push({
          student: s
        })
      });
      this.resultStudentMainList.forEach((s, i) => {
        //this speakers will be remove and save again with new fields
        speakersDto.push({
          listId: 1,
          orderOfSpeaking: i + 1,
          date: moment(this.mainDate).unix() * 1000,
          student: s
        })
      });
      this.resultStudentSubList.forEach((s, i) => {
        //and this too
        speakersDto.push({
          listId: 1,
          orderOfSpeaking: i + 1,
          date: moment(this.subDate).unix() * 1000,
          student: s
        })
      });
      this.speakerService.saveSpeakersList(speakersDto).subscribe(
        data => { this.toast.success("Данные были сохранены", "Успешно")},
        error => {
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        }
      );
    }
  }
}
