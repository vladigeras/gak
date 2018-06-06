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
import {CommissionService} from "../../service/commission.service";
import {CriteriaService} from "../../service/criteria.service";

declare var $: any;

@Component({
  selector: 'app-summary-table',
  templateUrl: './summary-table.component.html',
  styleUrls: ['./summary-table.component.scss']
})
export class SummaryTableComponent implements OnInit {

  @ViewChild('studentSpeakerTable') table: any;
  principal = currentPrincipal;
  activeSpeaker = {id: null, fio: null};
  selectedSpeaker = null;
  availableGroups = [];
  today = moment().startOf('day');
  criteria = [];
  selectedGroup = [];
  speakerStudents = [];
  commissionsList = [];

  groupSelectDropdownSettings = {
    singleSelection: true,
    enableCheckAll: false,
    text: "Выберите группу"
  };

  resultMark = null;
  listId  = null;
  speakerId = null;
  @BlockUI() blockUI: NgBlockUI;


  constructor(private toast: ToastsManager, private studentService: StudentService, private speakerService: SpeakerService,
              private questionService: QuestionService, private socketService: SocketService,
              private diplomService: DiplomService,
              private commissionService: CommissionService,
              private criteriaService: CriteriaService,
              private router: Router) {


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





  getSpeakersStudentsOfGroup() {
    if (this.selectedGroup[0] != undefined) {
      this.activeSpeaker = null;
      this.speakerStudents = [];
      this.blockUI.start(waitString);
      this.speakerService.getSpeakersListOfGroupOfDay(this.selectedGroup[0].itemName, this.today.unix() * 1000).subscribe(
        (data: any) => {

          data.forEach(speakerStudent => {
            if (speakerStudent.student != null) {
              this.speakerStudents.push({
                id: speakerStudent.id,
                studentId: speakerStudent.student.id,
                listId: speakerStudent.listId,
                fio: speakerStudent.student.lastname + " " + speakerStudent.student.firstname + " " + speakerStudent.student.middlename,
                title: speakerStudent.student.title,
                status: speakerStudent.student.status,
                expanded: null,    //this row was closed yet
                report: null,
                presentation: null,
                diplom: null

              });
            }
            this.getDiplomInfoOfSpeaker(speakerStudent.id, speakerStudent.listId  );
          });
          this.blockUI.stop();
          this.listId = this.speakerStudents[0].listId;
          this.getComissionsByListId();

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

  reloadCommissionTable() {
    this.commissionsList = [...this.commissionsList];
  }


  getComissionsByListId(){
  if(this.listId != null){
    this.commissionService.getCommissionsByListId(this.listId).subscribe (
      (commission: any) =>{
        commission.forEach(com =>{
          this.commissionsList.push({
            id: com.id,
            listId: com.listId,
            userId: com.user.firstname,
          });
          this.reloadCommissionTable();
        })

      }
    )


  }

  }

  setCommision(listId){
    //this.listId = listId;
    //this.getComissionsByListId();
    this.reloadCommissionTable();
  }

  saveResult(speakerId) {
    this.speakerId = speakerId;

      if (this.principal.roles.indexOf('PRESIDENT') != -1) {
        this.blockUI.start(waitString);
        this.criteriaService.saveResultMarkFromUserToSpeaker(this.resultMark, this.speakerId).subscribe(
          data => {
            this.blockUI.stop();
            this.toast.success("Оценка сохранена", "Успешно")
          },
          error => {
            this.blockUI.stop();
            if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
            else this.toast.error(error.error, "Ошибка");
          })
      }
    this.speakerId = null;

  }


  getQuestionsOfSpeaker() {
    if (this.selectedGroup[0] != undefined && this.principal.roles.indexOf('SECRETARY') != -1) {
      this.criteria = [];
      this.blockUI.start(waitString);
      this.questionService.getQuestionsOfSpeaker(this.activeSpeaker.id).subscribe(
        (data: any) => {
          data.forEach(q => {
            this.criteria.push({
              id: q.id,
              index: this.criteria.length + 1,
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







  getDiplomInfoOfSpeaker(speakerId, listId) {
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
          presentation: diplom.presentation != null,
          listId : listId
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


  setStatusToStudentInList(speaker, status: String) {
    let indexOfElement = this.speakerStudents.findIndex(s => s.id === speaker.id);
    let object = this.speakerStudents[indexOfElement];
    object.status = status;
    this.speakerStudents[indexOfElement] = object;
    this.reloadTable();
  }

  toggleExpandRow(event) {
    if (event.type === "click") {
      if (event.cellIndex != 2 && event.cellIndex != 3) this.table.rowDetail.toggleExpandRow(event.row);    //cellIndex 2 = button for set active
    }
  }

  isPrincipalContainsRole(role: String) : boolean {
    if (role == null) return false;
    return this.principal.roles.indexOf(role) != -1;
  }

  compareString(str1: String , str2: String) {
    if (str1 != null){
      if( str1 === str2)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else return false;

  }





}
