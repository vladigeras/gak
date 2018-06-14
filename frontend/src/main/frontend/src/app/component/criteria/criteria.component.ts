import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {CriteriaService} from "../../service/criteria.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../../app.module";
import {SocketService} from "../../service/socket.service";
import {currentPrincipal} from "../../security/auth.service";
import {Constants} from "../../constants";

@Component({
  selector: 'app-criteria',
  templateUrl: './criteria.component.html',
  styleUrls: ['./criteria.component.scss']
})
export class CriteriaComponent implements OnInit {

  principal = currentPrincipal;
  activeSpeaker = {id: null, fio: null};
  criteriaToActiveSpeaker = [];
  resultMark = 0; //default
  @BlockUI() blockUI: NgBlockUI;

  constructor(private toast: ToastsManager, private criteriaService: CriteriaService, private socketService: SocketService) {

    socketService.activeSpeakerReady.subscribe(speaker => {
      if (speaker != null) {
        this.activeSpeaker = {
          id: speaker.id,
          fio: speaker.student.lastname + " " + speaker.student.firstname + " " + speaker.student.middlename
        };
      }
    });
  }

  ngOnInit() {
    this.getDefaultCriteria()
  }

  getDefaultCriteria() {
    this.blockUI.start(waitString);
    this.criteriaService.getDefaultCriteria().subscribe(
      (data: any) => {
        if (data.length == 0) {
          this.toast.warning("Оптимальные (общие) критерии не заполнены на сервере", "Внимание")
        }
        data.forEach(c => {
          this.criteriaToActiveSpeaker.push({    //fill list an empty questions
            index: this.criteriaToActiveSpeaker.length + 1,
            title: c.title,
            rating: 0,
            comment: null
          })
        });
        this.blockUI.stop();
      },
      error => {
        this.blockUI.stop()
      }
    )
  }

  saveCriteria() {
    if (this.activeSpeaker != null) {
      let criteria = [];
      this.criteriaToActiveSpeaker.forEach(c => criteria.push(c));    //add questions

      if (this.resultMark != null) {      //because result mark will be one more questions too with fixed title
        criteria.push({
          index: this.criteriaToActiveSpeaker.length + 1,
          title: Constants.criteriaResult,
          rating: this.resultMark,
          comment: null
        });
      }

      let criteriaDtoWithResult = {
        criteriaDtoList: criteria,
        speakerId: this.activeSpeaker.id
      };
      this.blockUI.start(waitString);
      this.criteriaService.saveCriteriaWithData(criteriaDtoWithResult).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Критерии и оценки для студента сохранены", "Успешно")
        },
        error => {
          this.blockUI.stop();
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        }
      )
    }
  }

  removeCriteria(index) {
    let ind = this.criteriaToActiveSpeaker.findIndex(c => {
      return c.index == index
    });
    this.criteriaToActiveSpeaker.splice(ind, 1);
  }

  createCriteria() {
    this.criteriaToActiveSpeaker.push({
      index: this.criteriaToActiveSpeaker.length + 1,
      title: null,
      rating: 0,
      comment: null
    })
  }
}
