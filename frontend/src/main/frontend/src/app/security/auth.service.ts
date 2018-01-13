import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CookieService} from 'ngx-cookie-service';

@Injectable()
export class AuthService {

  principal = {id: null, name: null, roles: []};

  constructor(private http: HttpClient, private cookieService: CookieService) {
  }

  login(loginDto) {
    return this.http.post("/auth/login", loginDto);
  }

  isLoggedIn() {
    return this.principal.roles.length > 0
  }

  logout() {
    return this.http.get("/auth/logout");
  }

  getCurrentPrincipal() {
    let cookie = this.cookieService.get("X-AUTH-TOKEN");
    if (cookie && !this.isLoggedIn()) {
      this.http.get("/auth/principal").subscribe(
        data => {},
        error => {}
      )
    }
  }
}
