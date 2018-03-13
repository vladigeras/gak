import {Injectable} from '@angular/core';

@Injectable()
export class HelperService {

  public static convertOriginalToRole(role) {
    switch (role.toUpperCase()) {
      case "ADMIN": return "Администратор";
      case "PRESIDENT": return "Председатель";
      case "MEMBER": return "Член ГАК";
      case "SECRETARY": return "Секретарь";
      case "MENTOR": return "Руководитель";
      case "REVIEWER": return "Рецензент";
    }
  }

  public static convertRoleToOriginal(role) {
    switch (role.toUpperCase()) {
      case "АДМИНИСТРАТОР": return "ADMIN";
      case "ПРЕДСЕДАТЕЛЬ": return "PRESIDENT";
      case "ЧЛЕН ГАК": return "MEMBER";
      case "СЕКРЕТАРЬ": return "SECRETARY";
      case "РУКОВОДИТЕЛЬ": return "MENTOR";
      case "РЕЦЕНЗЕНТ": return "REVIEWER";
    }
  }
}
