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
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.dto.StudentDto;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.service.DiplomService;
import ru.iate.gak.service.GroupService;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.util.StatusUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
                row.add(new InlineKeyboardButton().setText("Просмотр выступающих").setCallbackData("speakers"));
                rows.add(row);
                markupKeyboard.setKeyboard(rows);
                response.setReplyMarkup(markupKeyboard);
                response.setText("Выберите действие");
            } else response.setText("Неизвестная команда");
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
                    row.add(new InlineKeyboardButton().setText("Все").setCallbackData("all"));

                    groupService.getGroups().forEach(group -> {
                        row.add(new InlineKeyboardButton().setText(group.getTitle()).setCallbackData(group.getTitle()));
                    });
                    rows.add(row);
                    markupKeyboard.setKeyboard(rows);

                    response.setReplyMarkup(markupKeyboard);
                    response.setText("Все выступающие или выбранной группы?");
                    break;

                case "all":
                    //get all speakers of current day
                    StringBuilder finalResponseText = responseText;
                    Map<String, List<SpeakerDto>> allSpeakersListAllGroupsOfDay = speakerService.getAllSpeakersListAllGroupsOfDay(messageDateInUTC);

                    final boolean[] isOneSpeakerGroup = {false};
                    allSpeakersListAllGroupsOfDay.forEach((group, speakerList) -> {
                        if (!speakerList.isEmpty()) {
                            isOneSpeakerGroup[0] = true;
                            finalResponseText.append("--- ").append(group).append(" ---").append("\n");
                            finalResponseText.append(getStringFromSpeakersList(speakerList));
                            finalResponseText.append("\n\n");
                        }
                    });
                    if (!isOneSpeakerGroup[0]) finalResponseText.append("Сегодня никто не выступает :(");
                    response.setText(finalResponseText.toString());
                    break;

                default:
                    //get speakers of current day of specific group
                    List<SpeakerDto> speakers = speakerService.getSpeakerListOfCurrentGroupOfDay(data, messageDateInUTC);
                    if (!speakers.isEmpty()) {
                        responseText = getStringFromSpeakersList(speakers);
                    } else responseText.append("Сегодня никто не выступает :(");
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

    private StringBuilder getStringFromSpeakersList(List<SpeakerDto> speakerList) {
        StringBuilder responseText = new StringBuilder();
        speakerList.forEach(speaker -> {
            StudentDto studentDto = speaker.student;
            if (studentDto != null) {
                responseText.append((studentDto.middlename != null)
                        ? speaker.orderOfSpeaking + ". " + studentDto.lastname + " " + studentDto.firstname + " " + studentDto.middlename + "\n"
                        : speaker.orderOfSpeaking + ". " + studentDto.lastname + " " + studentDto.firstname + "\n");

                DiplomEntity diplom = diplomService.getDiplomBySpeakerId(speaker.id);

                if (diplom != null) {
                    String status = StatusUtil.getRussianStringFromStatusEnum(diplom.getStatus());

                    if (!status.isEmpty()) {
                        responseText.append("(").append(status).append(")").append("\n");
                    }
                }

            }
        });

        return responseText;
    }
}
