import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CriteriaService {

  constructor(private http: HttpClient) { }

  getDefaultCriteria() {
    return this.http.get("/criteria/getDefault");
  }

  saveResultMarkFromUserToSpeaker(rating, speakerId) {
    let data = {rating: rating, speakerId: speakerId};
    return this.http.post("/criteria/saveResult", data);
  }

  saveCriteriaWithData(criteriaList) {
    return this.http.post("/criteria/save", criteriaList);
  }
}
