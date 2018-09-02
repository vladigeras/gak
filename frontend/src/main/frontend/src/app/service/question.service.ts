import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class QuestionService {

  constructor(private http: HttpClient) {
  }

  saveQuestions(speakerId, questions) {
    return this.http.post("/questions/" + "?speakerId=" + speakerId, questions);
  }

  getQuestionsOfSpeaker(speakerId) {
    return this.http.get("/questions/speaker" + "?speakerId=" + speakerId);
  }
}

