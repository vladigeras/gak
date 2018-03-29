import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DiplomService {

  constructor(private http: HttpClient) {
  }

  getDiplomBySpeakerId(speakerId) {
    return this.http.get("/diploms/bySpeaker" + "?id=" + speakerId);
  }

}

