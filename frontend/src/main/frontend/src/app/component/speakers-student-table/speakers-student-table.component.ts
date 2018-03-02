import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";
import {SpeakerService} from "../../service/speaker.service";
import * as moment from 'moment';
import {QuestionService} from "../../service/question.service";
import {CURRENT_PRINCIPAL} from "../../security/auth.service";

declare var $: any;

@Component({
  selector: 'app-speakers-student-table',
  templateUrl: './speakers-student-table.component.html',
  styleUrls: ['./speakers-student-table.component.scss']
})
export class SpeakersStudentTableComponent implements OnInit {

  principal = CURRENT_PRINCIPAL;
  activeSpeaker = ACTIVE_SPEAKER;
  selectedSpeaker = null;
  availableGroups = [];
  today = moment().startOf('day');

  criteria = [];

  selectedGroup = [];
  speakerStudents = [];
  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  constructor(private toast: ToastsManager, private studentService: StudentService, private speakerService: SpeakerService,
              private  questionService: QuestionService) {
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

  showSetActiveStudentModal(speaker) {
    $('#setActiveStudentConfirmModal').modal('show');
    this.selectedSpeaker = speaker;
  }

  setActiveStudent() {
    if (this.selectedSpeaker != null) {
      ACTIVE_SPEAKER = {
        id: this.selectedSpeaker.id,
        fio: this.selectedSpeaker.fio
      };
      this.getQuestionsOfSpeaker();
      $('#setActiveStudentConfirmModal').modal('hide');
    }
  }

  getSpeakersStudentsOfGroup() {
    if (this.selectedGroup[0] != undefined) {
      ACTIVE_SPEAKER = null;
      this.speakerStudents = [];
      this.speakerService.getSpeakersListOfGroupOfDay(this.selectedGroup[0].itemName, this.today.unix() * 1000).subscribe(
        (data: any) => {
          data.forEach(speakerStudent => {
            this.speakerStudents.push({
              id: speakerStudent.id,
              fio: speakerStudent.student.lastname + " " + speakerStudent.student.firstname + " " + speakerStudent.student.middlename,
              title: speakerStudent.student.title
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
  }

  reloadTable() {
    this.speakerStudents = [...this.speakerStudents];
  }

  createQuestion() {
    this.criteria.push({
      index: this.criteria.length + 1,
      text: ""
    })
  }

  removeQuestion(index) {
    let ind = this.criteria.findIndex(q => {
      return q.index == index
    });
    this.criteria.splice(ind, 1);
  }

  saveQuestions() {
    if (this.selectedGroup[0] != undefined) {
      let questions = [];
      this.criteria.forEach(q => {
        questions.push({id: q.id, questionText: q.text})
      });
      this.questionService.saveQuestions(ACTIVE_SPEAKER.id, questions).subscribe(
        data => {
          this.toast.success("Сохранено", "Успешно")
        }
      );
    }
  }

  getQuestionsOfSpeaker() {
    if (this.selectedGroup[0] != undefined) {
      this.criteria = [];
      this.questionService.getQuestionsOfSpeaker(ACTIVE_SPEAKER.id).subscribe(
        (data: any) => {
          data.forEach(q => {
            this.criteria.push({
              id: q.id,
              index: this.criteria.length + 1,
              text: q.questionText
            })
          });
        }
      )
    }
  }

  downloadProtocols() {
    if (this.selectedGroup[0] != undefined) {
      this.speakerService.getProtocolsForGroup(this.selectedGroup[0].itemName);
    }
  }
}

export var ACTIVE_SPEAKER = null;
