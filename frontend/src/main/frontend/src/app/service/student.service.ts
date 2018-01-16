import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable()
export class StudentService {

  constructor(private http: HttpClient) { }

  getAvailableGroups() {
    return this.http.get("/groups/get");
  }

  getStudentsOfGroup(group) {
    return this.http.get("/students/ofGroup" + "?group=" + group);
  }

}
