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

  public static convertStatusToRussian(status) {
    if (status == null) return null;
    switch (status.toUpperCase()) {
      case "ACTIVE": return "Активный";
      case "DONE": return "Завершено";
    }
  }

  public static convertResultMarkToString(resultMark) {
    if (resultMark == null) return null;
    switch (resultMark.toString()) {
      case "5": return "5 (отлично)";
      case "4": return "4 (хорошо)";
      case "3": return "3 (удовлетворительно)";
      case "2": return "2 (неудовлетворительно)";
    }
  }
}
