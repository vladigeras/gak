import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class SpeakerService {

  constructor(private http: HttpClient) {
  }

  saveSpeakersList(speakersList) {
    return this.http.post("/speakers/save", speakersList);
  }

  getSpeakersListOfGroup(group) {
    return this.http.get("/speakers/ofGroup" + "?group= " + group);
  }
}

