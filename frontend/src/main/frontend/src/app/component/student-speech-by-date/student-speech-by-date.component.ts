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

  constructor(private toast: ToastsManager, private studentService: StudentService, private speakerService: SpeakerService) {
  }

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

  /**
   * Method for getting speakers list of group.
   * 1. Get all student of group.
   * 2. Get speakers of this group by date
   * 3. Delete from result of step 1 elements, which was get by step 2.
   */
  fillSpeakersList() {
    let group = this.selectedGroup[0].itemName;
    if (group != null) {
      this.studentsOfSelectedGroup = [];
      this.resultStudentMainList = [];
      this.resultStudentSubList = [];
      this.mainDate = null;
      this.subDate = null;
      this.getStudentsOfGroup(group, () => this.getSpeakersListOfGroup(group));
    }
  }

  getStudentsOfGroup(group, callback) {
    this.studentService.getStudentsOfGroup(this.selectedGroup[0].itemName).subscribe(
      (data: any) => {
        data.forEach(student => {
          let studentObj = {
            id: student.id,
            name: student.lastname + " " + student.firstname + " " + student.middlename
          };
          this.studentsOfSelectedGroup.push(studentObj)
        });
        if (typeof callback === "function") callback();
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      }
    )
  }

  getSpeakersListOfGroup(group) {
    this.speakerService.getSpeakersListOfGroup(group).subscribe(
      (data: any) => {
        let dateList = [];
        data.forEach(speaker => {
          if (!dateList.includes(speaker.date)) dateList.push(speaker.date);
        });
        data.forEach(speaker => {
          let speakerObj = {
            id: speaker.student.id,
            name: speaker.student.lastname + " " + speaker.student.firstname + " " + speaker.student.middlename
          };
          switch (speaker.date) {
            case dateList[0]: {
              this.resultStudentMainList.push(speakerObj);
              break;
            }
            case dateList[1]: {
              this.resultStudentSubList.push(speakerObj);
              break;
            }
          }
          let indexOfDeletedElement = this.studentsOfSelectedGroup.findIndex(i => i.id == speakerObj.id);
          this.studentsOfSelectedGroup.splice(indexOfDeletedElement, 1);

        });
        if (dateList[0] != null) this.mainDate = dateList[0];
        if (dateList[1] != null) this.subDate = dateList[1];
      },
      error => {
        if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
        else this.toast.error(error.error, "Ошибка");
      })
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
        data => {
          this.toast.success("Данные были сохранены", "Успешно");
          this.fillSpeakersList();
        },
        error => {
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        }
      );
    }
  }
}
