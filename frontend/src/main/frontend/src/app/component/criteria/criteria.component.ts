import {Component, OnInit} from '@angular/core';
import {ToastsManager} from "ng2-toastr";
import {CriteriaService} from "../../service/criteria.service";
import {ACTIVE_SPEAKER} from "../speakers-student-table/speakers-student-table.component";

@Component({
  selector: 'app-criteria',
  templateUrl: './criteria.component.html',
  styleUrls: ['./criteria.component.scss']
})
export class CriteriaComponent implements OnInit {

  activeSpeaker = ACTIVE_SPEAKER;
  criteriaToActiveSpeaker = [];
  resultMark;

  constructor(private toast: ToastsManager, private criteriaService: CriteriaService) { }

  ngOnInit() {
    this.getDefaultCriteria()
  }

  getDefaultCriteria() {
    this.criteriaService.getDefaultCriteria().subscribe(
      (data: any) => {
        data.forEach(c => {
          this.criteriaToActiveSpeaker.push({    //fill list an empty criteria
            index: this.criteriaToActiveSpeaker.length + 1,
            title: c.title,
            rating: null,
            comment: null
          })
        });
      }
    )
  }

  saveCriteria() {

  }

  saveResult() {
    if (ACTIVE_SPEAKER != null) {
      this.criteriaService.saveResultMarkFromUserToSpeaker(this.resultMark, ACTIVE_SPEAKER.id).subscribe(
        () => {
          this.toast.success("Оценка сохранена", "Успешно")
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
