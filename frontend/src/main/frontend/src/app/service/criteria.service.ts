import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CriteriaService {

  constructor(private http: HttpClient) {
  }

  getDefaultCriteria() {
    return this.http.get("/criteria/defaults");
  }

  getAllCriteriaByDiplomId(diplomId) {
    return this.http.get("/criteria/diplom" + "?diplomId=" + diplomId);
  }


  saveResultMarkFromUserToSpeaker(rating, speakerId) {
    let data = {rating: rating, speakerId: speakerId};
    return this.http.post("/criteria/result", data);
  }

  saveCriteriaWithData(criteriaDtoWithResult) {
    return this.http.post("/criteria/", criteriaDtoWithResult);
  }
}
