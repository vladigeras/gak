import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";
import {SpeakerService} from "../../service/speaker.service";
import * as moment from 'moment';
import {QuestionService} from "../../service/question.service";

declare var $: any;

@Component({
  selector: 'app-speakers-student-table',
  templateUrl: './speakers-student-table.component.html',
  styleUrls: ['./speakers-student-table.component.scss']
})
export class SpeakersStudentTableComponent implements OnInit {

  activeSpeaker = null;
  selectedSpeaker = null;
  availableGroups = [];
  today = moment().startOf('day');

  questionsToActiveStudent = [];

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
      this.activeSpeaker = {
        id: this.selectedSpeaker.id,
        fio: this.selectedSpeaker.fio
      };
      this.getQuestionsOfSpeaker();
      $('#setActiveStudentConfirmModal').modal('hide');
    }
  }

  getSpeakersStudentsOfGroup() {
    if (this.selectedGroup[0] != undefined) {
      this.activeSpeaker = null;
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
    this.questionsToActiveStudent.push({
      index: this.questionsToActiveStudent.length + 1,
      text: ""
    })
  }

  removeQuestion(index) {
    let ind = this.questionsToActiveStudent.findIndex(q => {
      return q.index == index
    });
    this.questionsToActiveStudent.splice(ind, 1);
  }

  saveQuestions() {
    if (this.selectedGroup[0] != undefined) {
      let questions = [];
      this.questionsToActiveStudent.forEach(q => {
        questions.push({id: q.id, questionText: q.text})
      });
      this.questionService.saveQuestions(this.activeSpeaker.id, questions).subscribe(
        data => {this.toast.success("Сохранено", "Успешно")}
      );
    }
  }

  getQuestionsOfSpeaker() {
    if (this.selectedGroup[0] != undefined) {
      this.questionsToActiveStudent = [];
      this.questionService.getQuestionsOfSpeaker(this.activeSpeaker.id).subscribe(
        (data: any) => {
          data.forEach(q => {
            this.questionsToActiveStudent.push({
              id: q.id,
              index: this.questionsToActiveStudent.length + 1,
              text: q.questionText
            })
          });
        }
      )
    }
  }
}
