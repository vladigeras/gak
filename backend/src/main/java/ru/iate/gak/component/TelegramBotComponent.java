package ru.iate.gak.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.iate.gak.domain.Diplom;
import ru.iate.gak.domain.Speaker;
import ru.iate.gak.domain.Student;
import ru.iate.gak.service.DiplomService;
import ru.iate.gak.service.GroupService;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.util.StatusUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    @Autowired
    private GroupService groupService;

    @Autowired
    private DiplomService diplomService;

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
            response.setChatId(message.getChatId());
            String inputText = message.getText();

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

            if (inputText.equals("/start")) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(new InlineKeyboardButton().setText("speakers").setCallbackData("speakers"));
                rows.add(row);
                markupKeyboard.setKeyboard(rows);
                response.setReplyMarkup(markupKeyboard);
                response.setText("Select an action");
            } else response.setText("Unknown command");
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.getStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            SendMessage response = new SendMessage();
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());

            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

            String data = update.getCallbackQuery().getData();

            LocalDateTime messageDateInUTC = convertCalendarToUTCDate(getStartOfLocalDateInSeconds(update.getCallbackQuery().getMessage().getDate()));
            StringBuilder responseText = new StringBuilder();

            switch (data) {
                case "speakers":
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    row.add(new InlineKeyboardButton().setText("all").setCallbackData("all"));

                    groupService.getGroups().forEach(group -> {
                        row.add(new InlineKeyboardButton().setText(group.getTitle()).setCallbackData(group.getTitle()));
                    });
                    rows.add(row);
                    markupKeyboard.setKeyboard(rows);

                    response.setReplyMarkup(markupKeyboard);
                    response.setText("Is speakers from ALL group or specific?");
                    break;

                case "all":
                    //get all speakers of current day
                    StringBuilder finalResponseText = responseText;
                    speakerService.getAllSpeakersListAllGroupsOfDay(messageDateInUTC).forEach((group, speakerList) -> {
                        if (!speakerList.isEmpty()) {
                            finalResponseText.append("--- ").append(group).append(" ---").append("\n");
                            finalResponseText.append(getStringFromSpeakersList(speakerList));
                            finalResponseText.append("\n\n");
                        }
                    });
                    response.setText(finalResponseText.toString());
                    break;

                default:
                    //get speakers of current day of specific group
                    List<Speaker> speakers = speakerService.getSpeakerListOfCurrentGroupOfDay(data, messageDateInUTC);
                    responseText = getStringFromSpeakersList(speakers);
                    response.setText(responseText.toString());
                    break;
            }

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.getStackTrace();
            }
        }
    }

    private Calendar getStartOfLocalDateInSeconds(Integer localDateInSeconds) {
        Long localDateInMillis = Long.valueOf(localDateInSeconds) * 1000;
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

    private StringBuilder getStringFromSpeakersList(List<Speaker> speakerList) {
        StringBuilder responseText = new StringBuilder();
        if (!speakerList.isEmpty()) {
            speakerList.forEach(speaker -> {
                Student student = speaker.getStudent();
                if (student != null) {
                    responseText.append((student.getMiddlename() != null)
                            ? speaker.getOrderOfSpeaking() + ". " + student.getLastname() + " " + student.getFirstname() + " " + student.getMiddlename() + "\n"
                            : speaker.getOrderOfSpeaking() + ". " + student.getLastname() + " " + student.getFirstname() + "\n");

                    Diplom diplom = diplomService.getDiplomBySpeakerId(speaker.getId());

                    if (diplom != null) {
                        String status = StatusUtil.getRussianStringFromStatusEnum(diplom.getStatus());

                        if (!status.isEmpty()) {
                            responseText.append("(").append(status).append(")").append("\n");
                        }
                    }

                }
            });
        } else responseText.append("Today no speakers :(");

        return responseText;
    }
}
