import {Injectable} from '@angular/core';

@Injectable()
export class HelperService {

  public static convertRole(role) {
    switch (role) {
      case "ADMIN": return "Администратор";
      case "PRESIDENT": return "Председатель";
      case "MEMBER": return "Член ГАК";
      case "SECRETARY": return "Секретарь";
      case "MENTOR": return "Руководитель";
      case "REVIEWER": return "Рецензент";
    }
  }
}
