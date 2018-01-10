import {Injectable} from '@angular/core';

@Injectable()
export class HelperService {

  public static convertOriginalToRole(role) {
    switch (role) {
      case "ADMIN": return "Администратор";
      case "PRESIDENT": return "Председатель";
      case "MEMBER": return "Член ГАК";
      case "SECRETARY": return "Секретарь";
      case "MENTOR": return "Руководитель";
      case "REVIEWER": return "Рецензент";
    }
  }

  public static convertRoleToOriginal(role) {
    switch (role) {
      case "Администратор": return "ADMIN";
      case "Председатель": return "PRESIDENT";
      case "Член ГАК": return "MEMBER";
      case "Секретарь": return "SECRETARY";
      case "Руководитель": return "MENTOR";
      case "Рецензент": return "REVIEWER";
    }
  }
}
