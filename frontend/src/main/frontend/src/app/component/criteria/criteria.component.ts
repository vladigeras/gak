import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {CriteriaService} from "../../service/criteria.service";
import {ACTIVE_SPEAKER} from "../speakers-student-table/speakers-student-table.component";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {WAIT_STRING} from "../../app.module";

@Component({
  selector: 'app-criteria',
  templateUrl: './criteria.component.html',
  styleUrls: ['./criteria.component.scss']
})
export class CriteriaComponent implements OnInit {

  activeSpeaker = ACTIVE_SPEAKER;
  criteriaToActiveSpeaker = [];
  resultMark;
  @BlockUI() blockUI: NgBlockUI;

  constructor(private toast: ToastsManager, private criteriaService: CriteriaService) { }

  ngOnInit() {
    this.getDefaultCriteria()
  }

  getDefaultCriteria() {
    this.blockUI.start(WAIT_STRING);
    this.criteriaService.getDefaultCriteria().subscribe(
      (data: any) => {
        if (data.length == 0) {
          this.toast.warning("Оптимальные (общие) критерии не заполнены на сервере", "Внимание")
        }
        data.forEach(c => {
          this.criteriaToActiveSpeaker.push({    //fill list an empty criteria
            index: this.criteriaToActiveSpeaker.length + 1,
            title: c.title,
            rating: null,
            comment: null
          })
        });
        this.blockUI.stop();
      },
      error => {this.blockUI.stop()}
    )
  }

  saveCriteria() {
    if (ACTIVE_SPEAKER != null) {
      let criteriaDtoWithResult = {
        criteriaDtoList: this.criteriaToActiveSpeaker,
        speakerId: ACTIVE_SPEAKER.id
      };
      this.blockUI.start(WAIT_STRING);
      this.criteriaService.saveCriteriaWithData(criteriaDtoWithResult).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Критерии и оценки сохранены", "Успешно")
        },
        error => {
          this.blockUI.stop();
          if (error.error.message != undefined) this.toast.error(error.error.message, "Ошибка");
          else this.toast.error(error.error, "Ошибка");
        }
      )
    }
  }

  saveResult() {
    if (ACTIVE_SPEAKER != null) {
      this.blockUI.start(WAIT_STRING);
      this.criteriaService.saveResultMarkFromUserToSpeaker(this.resultMark, ACTIVE_SPEAKER.id).subscribe(
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
      rating: null,
      comment: null
    })
  }
}
