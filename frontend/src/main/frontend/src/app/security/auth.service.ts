import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CookieService} from 'ngx-cookie-service';

@Injectable()
export class AuthService {

  principalReady = new EventEmitter();

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
      this.http.get("/auth/principal").subscribe(
        (data: any) => {
          currentPrincipal.id = data.id;
          currentPrincipal.name = data.name;
          currentPrincipal.roles = data.roles;
          this.principalReady.emit()
        }
      )
    }
  }
}

export var currentPrincipal = {id: null, name: null, roles: []};

