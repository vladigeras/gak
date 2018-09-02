import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class StudentService {

  constructor(private http: HttpClient) { }

  getAvailableGroups() {
    return this.http.get("/groups/");
  }

  getStudentsOfGroup(group) {
    return this.http.get("/students/groups" + "?group=" + group);
  }

  saveStudent(studentDto) {
    return this.http.post("/students/", studentDto);
  }

  saveFiles(studentId, reportFile, presentationFile) {
    let params = new FormData();
    params.append("student", studentId);
    params.append("reportFile", reportFile);
    params.append("presentationFile", presentationFile);
    return this.http.post("/students/files", params)
  }

  readFile(studentId, isReport) {
    return this.http.get("/students/readFile" + "?student=" + studentId + "&isReport=" + isReport, { responseType: 'blob'})
      .map(res => {
        return new Blob([res], { type: 'application/pdf', });
      });
  }

  deleteStudent(studentId) {
    return this.http.delete("/students/" + studentId)
  }
}
