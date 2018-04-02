import {ActivatedRouteSnapshot, CanActivate} from "@angular/router";
import {Injectable} from "@angular/core";
import {currentPrincipal} from "./auth.service";

@Injectable()
export class GuardService implements CanActivate {

  canActivate(route: ActivatedRouteSnapshot) {
    let needRoles = route.data['roles'] as Array<string>;
    if (currentPrincipal != null && currentPrincipal.id != null) {
      for (let r of currentPrincipal.roles) {
        if (!needRoles || needRoles.indexOf(r) != -1) return true;
      }
      return false;
    }
    else return false;
  }

}
