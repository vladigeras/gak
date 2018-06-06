import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CriteriaService {

  constructor(private http: HttpClient) { }

  getDefaultCriteria() {
    return this.http.get("/criteria/getDefault");
  }

  getAllCriteriaByDiplomId() {
    return this.http.get("/criteria/getCriteria");
  }




  saveResultMarkFromUserToSpeaker(rating, speakerId) {
    let data = {rating: rating, speakerId: speakerId};
    return this.http.post("/criteria/saveResult", data);
  }

  saveCriteriaWithData(criteriaDtoWithResult) {
    return this.http.post("/criteria/save", criteriaDtoWithResult);
  }
}
