import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }

  getAvailableRoles() {
    return this.http.get("/users/roles");
  }

  addUser(userDto) {
    return this.http.post("/users/add", userDto);
  }

  getUsers() {
    return this.http.get("/users/get");
  }

  updateUser(userDto) {
    return this.http.post("/users/update", userDto);
  }
}
