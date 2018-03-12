import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CookieService} from 'ngx-cookie-service';
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {WAIT_STRING} from "../app.module";

@Injectable()
export class AuthService {

  principalReady = new EventEmitter();
  @BlockUI() blockUI: NgBlockUI;

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  login(loginDto) {
    return this.http.post("/auth/login", loginDto);
  }

  isLoggedIn() {
    return CURRENT_PRINCIPAL.roles.length > 0
  }

  logout() {
    return this.http.get("/auth/logout");
  }

  getCurrentPrincipal() {
    let cookie = this.cookieService.get("X-AUTH-TOKEN");
    if (cookie && !this.isLoggedIn()) {
      this.blockUI.start(WAIT_STRING);
      this.http.get("/auth/principal").subscribe(
        (data: any) => {
          CURRENT_PRINCIPAL.id = data.id;
          CURRENT_PRINCIPAL.name = data.name;
          CURRENT_PRINCIPAL.roles = data.roles;
          this.principalReady.emit()
          this.blockUI.stop();
        },
        error => {
          this.blockUI.stop();
        }
      )
    }
  }
}

export var CURRENT_PRINCIPAL = {id: null, name: null, roles: []};

