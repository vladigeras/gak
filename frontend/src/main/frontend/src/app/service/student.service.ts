import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {ResponseContentType, ResponseType} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class StudentService {

  constructor(private http: HttpClient) { }

  getAvailableGroups() {
    return this.http.get("/groups/get");
  }

  getStudentsOfGroup(group) {
    return this.http.get("/students/ofGroup" + "?group=" + group);
  }

  saveStudent(studentDto) {
    return this.http.post("/students/save", studentDto);
  }

  saveFiles(studentId, reportFile, presentationFile) {
    let params = new FormData();
    params.append("student", studentId);
    params.append("reportFile", reportFile);
    params.append("presentationFile", presentationFile);
    return this.http.post("/students/files", params)
  }

  readFile(studentId, isReport) {
    let a = document.createElement("a");
    a.href = "/students/readFile" + "?student=" + studentId + "&isReport=" + isReport;
    a.click()
  }
}
