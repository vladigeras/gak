import {Component, OnInit, ViewChild} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {StudentService} from "../../service/student.service";
import {SpeakerService} from "../../service/speaker.service";
import * as moment from 'moment';
import {QuestionService} from "../../service/question.service";
import {currentPrincipal} from "../../security/auth.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../../app.module";
import {SocketService, stomp} from "../../service/socket.service";
import {Status} from "../../status";
import {DiplomService} from "../../service/diplom.service";
import {HelperService} from "../../service/helper.service";
import {Router} from "@angular/router";

declare var $: any;

@Component({
  selector: 'app-speakers-student-table',
  templateUrl: './speakers-student-table.component.html',
  styleUrls: ['./speakers-student-table.component.scss']
})
export class SpeakersStudentTableComponent implements OnInit {

  @ViewChild('studentSpeakerTable') table: any;
  principal = currentPrincipal;
  activeSpeaker = {id: null, fio: null};
  selectedSpeaker = null;
  availableGroups = [];
  today = moment().startOf('day');
  questions = [];
  selectedGroup = [];
  speakerStudents = [];
  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };
  socket = stomp;
  @BlockUI() blockUI: NgBlockUI;
  countLabs = 0;
  rating = 5;

  constructor(private toast: ToastsManager, private studentService: StudentService, private speakerService: SpeakerService,
              private questionService: QuestionService, private socketService: SocketService,
              private diplomService: DiplomService,
              private router: Router) {

    socketService.activeSpeakerReady.subscribe(speaker => {
      if (speaker != null) {
        if (this.selectedGroup[0] != undefined) {
          // this.setStatusToStudentInList(speaker, Status[Status.ACTIVE]);
          this.activeSpeaker = {
            id: speaker.id,
            fio: speaker.student.lastname + " " + speaker.student.firstname + " " + speaker.student.middlename
          };
          this.getSpeakersStudentsOfGroup();
          this.getQuestionsOfSpeaker();
          // this.getDiplomInfoOfSpeaker(speaker.id);
        }
      }
    });

    socketService.doneSpeakerReady.subscribe(speaker => {
      if (speaker != null) {
        // this.setStatusToStudentInList(speaker, Status[Status.DONE]);
        // this.getDiplomInfoOfSpeaker(speaker.id);
        this.getSpeakersStudentsOfGroup();
      }
    })
  }

  ngOnInit() {
    this.getAvailableGroups();
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

  showSetActiveStudentModal(speaker) {
    $('#setActiveStudentConfirmModal').modal('show');
    this.selectedSpeaker = speaker;
  }

  setActiveStudent() {
    if (this.selectedSpeaker != null) {
      this.socket.send("/app/activeSpeaker", {}, this.selectedSpeaker.id);
      $('#setActiveStudentConfirmModal').modal('hide');
    }
  }

  getSpeakersStudentsOfGroup() {
    if (this.selectedGroup[0] != undefined) {
      this.blockUI.start(waitString);
      // this.activeSpeaker = null;
      this.speakerStudents = [];
      this.speakerService.getSpeakersListOfGroupOfDay(this.selectedGroup[0].itemName, this.today.unix() * 1000).subscribe(
        (data: any) => {
          data.forEach(speakerStudent => {
            if (speakerStudent.student != null) {
              this.speakerStudents.push({
                id: speakerStudent.id,
                studentId: speakerStudent.student.id,
                fio: speakerStudent.student.lastname + " " + speakerStudent.student.firstname + " " + speakerStudent.student.middlename,
                title: speakerStudent.student.title,
                status: speakerStudent.student.status,
                expanded: null,    //this row was closed yet
                report: null,
                presentation: null,
                diplom: null
              });
            }
            this.getDiplomInfoOfSpeaker(speakerStudent.id);
          });
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
    this.speakerStudents = [...this.speakerStudents];
  }

  createQuestion() {
    this.questions.push({
      index: this.questions.length + 1,
      text: ""
    })
  }

  removeQuestion(index) {
    let ind = this.questions.findIndex(q => {
      return q.index == index
    });
    this.questions.splice(ind, 1);
  }

  saveQuestions() {
    if (this.selectedGroup[0] != undefined) {
      let questions = [];
      this.questions.forEach(q => {
        questions.push({id: q.id, questionText: q.text})
      });
      this.blockUI.start(waitString);
      this.questionService.saveQuestions(this.activeSpeaker.id, questions).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Сохранено", "Успешно")
        },
        error => {
          this.blockUI.stop()
        }
      );
    }
  }

  getQuestionsOfSpeaker() {
    if (this.selectedGroup[0] != undefined && this.principal.roles.indexOf('SECRETARY') != -1) {
      this.questions = [];
      this.blockUI.start(waitString);
      this.questionService.getQuestionsOfSpeaker(this.activeSpeaker.id).subscribe(
        (data: any) => {
          data.forEach(q => {
            this.questions.push({
              id: q.id,
              index: this.questions.length + 1,
              text: q.questionText
            })
          });
          this.blockUI.stop();
        },
        error => {
          this.blockUI.stop()
        }
      )
    }
  }

  downloadProtocolsOfTodaySpeakers() {
    if (this.selectedGroup[0] != undefined) {
      this.speakerService.getProtocolsForTodaySpeakersOfGroup(this.selectedGroup[0].itemName);
    }
  }

  getRowClass(row) {  //see classes in styles.scss file
    return {
      'active-student-row': row.status === Status[Status.ACTIVE],
      'done-student-row': row.status === Status[Status.DONE]
    };
  }

  setStatusToStudentInList(speaker, status: String) {
    this.speakerStudents[speaker.orderOfSpeaking-1].status = status;
    this.reloadTable();
  }

  toggleExpandRow(event) {
    if (event.type === "click") {
      if (event.cellIndex != 2 && event.cellIndex != 3) this.table.rowDetail.toggleExpandRow(event.row);    //cellIndex 2 = button for set active
    }
  }

  getDiplomInfoOfSpeaker(speakerId) {
    this.blockUI.start(waitString);
    this.diplomService.getDiplomBySpeakerId(speakerId).subscribe(
      (diplom: any) => {
        //replace existing object in list with new subdata
        let indexOfElement = this.speakerStudents.findIndex(s => s.id === speakerId);
        let existingObject = this.speakerStudents[indexOfElement];
        let newObject = {
          id: existingObject.id,
          studentId: existingObject.studentId,
          fio: existingObject.fio,
          title: existingObject.title,
          status: existingObject.status,
          diplom: {
            mentorFio: diplom.mentor.lastname + " " + diplom.mentor.firstname + " " + diplom.mentor.middlename,
            reviewerFio: diplom.reviewer.lastname + " " + diplom.reviewer.firstname + " " + diplom.reviewer.middlename,
            statusString: HelperService.convertStatusToRussian(diplom.status),
            executionPlace: diplom.executionPlace,
            resultMark: HelperService.convertResultMarkToString(diplom.resultMark),
          },
          expanded: true,   //this row was expanded now
          report: diplom.report != null,
          presentation: diplom.presentation != null
        };
        this.speakerStudents[indexOfElement] = newObject;
        this.reloadTable();
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop()
      }
    );
  }

  readFile(row, isReport: boolean) {
    let studentId = row.studentId;
    let tab = window.open();
    this.studentService.readFile(studentId, isReport).subscribe(fileData => {
      const fileUrl = URL.createObjectURL(fileData);
      tab.location.href = fileUrl;
    });
  }

  isPrincipalContainsRole(role: String) : boolean {
    if (role == null) return false;
    return this.principal.roles.indexOf(role) != -1;
  }

  getFlagLabs(event) {
    this.countLabs = event;
  }

  setDoneStudent() {
    if (this.activeSpeaker != null) {
      this.socket.send("/app/doneSpeaker", {}, this.activeSpeaker.id);
      $('#setActiveStudentConfirmModal').modal('hide');
      this.countLabs = 0;
    }
  }
}



