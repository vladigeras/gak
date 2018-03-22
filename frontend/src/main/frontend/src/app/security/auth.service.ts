import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CookieService} from 'ngx-cookie-service';
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {waitString} from "../app.module";

@Injectable()
export class AuthService {

  principalReady = new EventEmitter();
  @BlockUI() blockUI: NgBlockUI;

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  login(loginDto) {
    return this.http.post("/auth/login", loginDto);
  }

  isLoggedIn() {
    return currentPrincipal.roles.length > 0
  }

  logout() {
    return this.http.get("/auth/logout");
  }

  getCurrentPrincipal() {
    let cookie = this.cookieService.get("X-AUTH-TOKEN");
    if (cookie && !this.isLoggedIn()) {
      this.blockUI.start(waitString);
      this.http.get("/auth/principal").subscribe(
        (data: any) => {
          currentPrincipal.id = data.id;
          currentPrincipal.name = data.name;
          currentPrincipal.roles = data.roles;
          this.principalReady.emit();
          this.blockUI.stop();
        },
        error => {
          this.blockUI.stop();
        }
      )
    }
  }
}

export var currentPrincipal = {id: null, name: null, roles: []};

