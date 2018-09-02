import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }

  getAvailableRoles() {
    return this.http.get("/users/roles/all");
  }

  addUser(userDto) {
    return this.http.post("/users/", userDto);
  }

  getUsers() {
    return this.http.get("/users/");
  }

  updateUser(userDto) {
    return this.http.put("/users/", userDto);
  }

  getUsersByRole(role) {
    return this.http.get("/users/roles" + "?role=" + role);
  }
}
