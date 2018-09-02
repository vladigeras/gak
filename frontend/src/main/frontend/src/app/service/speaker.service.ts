import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class SpeakerService {

  constructor(private http: HttpClient) {
  }

  saveSpeakersList(speakersList) {
    return this.http.post("/speakers/", speakersList);
  }

  getSpeakersListOfGroupOfDay(group, date) {
    if (date == null) {
      return this.http.get("/speakers/" + "?group=" + group);
    } else {
      return this.http.get("/speakers/" + "?group=" + group + "&date=" + date);
    }
  }

  getProtocolsForTodaySpeakersOfGroup(group) {
    let a = document.createElement("a");
    a.href = "/speakers/zippedProtocols" + "?group=" + group;
    a.click()
  }
}


