package ru.iate.gak.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.iate.gak.domain.Speaker;
import ru.iate.gak.domain.Student;
import ru.iate.gak.service.SpeakerService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TelegramBotComponent extends TelegramLongPollingBot {

    @Value("${bot.token}")
    String telegramBotApiKey;

    @Value("${bot.username}")
    String telegramBotName;

    @Autowired
    private SpeakerService speakerService;

    @Override
    public String getBotToken() {
        return telegramBotApiKey;
    }

    @Override
    public String getBotUsername() {
        return telegramBotName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(chatId);
            String text = message.getText();
            switch (text) {
                case "/start":
                    response.setText("Hello! This is a bot-support of iate.obninsk.ru GAK system! Enjoy!");
                    break;

                case "/speakers":
                    //get speakers of current day
                    StringBuilder responseText = new StringBuilder();
                    LocalDateTime dateInUTC = convertCalendarToUTCDate(getStartOfLocalDateInSeconds(message.getDate()));
                    List<Speaker> speakers = speakerService.getSpeakerListOfCurrentGroupOfDay("ИС-Б14", dateInUTC);
                    if (!speakers.isEmpty()) {
                        speakers.forEach(speaker -> {
                            Student student = speaker.getStudent();
                            if (student != null) {
                                responseText.append((student.getMiddlename() != null)
                                        ? speaker.getOrderOfSpeaking() + ". " + student.getLastname() + " " + student.getFirstname() + " " + student.getMiddlename() + "\n"
                                        : speaker.getOrderOfSpeaking() + ". " + student.getLastname() + " " + student.getFirstname() + "\n");
                            }
                        });
                    } else responseText.append("Today no speakers :(");
                    response.setText(responseText.toString());
                    break;

                default:
                    response.setText("Unknown command");
            }
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.getStackTrace();
            }
        }
    }

    private Calendar getStartOfLocalDateInSeconds(Integer localDateInSeconds) {
        Long localDateInMillis = Long.valueOf(localDateInSeconds)*1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(localDateInMillis));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private LocalDateTime convertCalendarToUTCDate(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneOffset.UTC);
    }
}
