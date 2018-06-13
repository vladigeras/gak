package ru.iate.gak.util;

import ru.iate.gak.domain.Status;

public class StatusUtil {
    public static String getRussianStringFromStatusEnum(Status status) {
        String result = "";
        if (status != null) {
            switch (status.name().toUpperCase()) {
                case "ACTIVE": result = "Активный"; break;
                case "SPEAKING_TIME": result = "Выступление"; break;
                case "QUESTION_TIME": result = "Вопросы"; break;
                case "REWIEW_TIME": result = "Зачитывание отзывов"; break;
                case "LASTWORD_TIME": result = "Заключительное слово"; break;
                case "DONE": result = "Завершен"; break;
            }
        }
        return result;
    }
}
