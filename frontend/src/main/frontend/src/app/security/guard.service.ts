import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {AuthService, currentPrincipal} from "./auth.service";

@Injectable()
export class GuardService implements CanActivate {

  principal = currentPrincipal;

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot) {
    let needRoles = route.data['roles'] as Array<string>;
    if (this.principal != null && this.principal.id != null) {
      for (let r of this.principal.roles) {
        if (!needRoles || r == "ADMIN" || needRoles.indexOf(r) != -1) return true;    //ADMIN has access to all resources
      }
      this.router.navigate(['']);
      return false;
    }
    else return false;
  }

}
