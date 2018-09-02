package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.model.Status;
import ru.iate.gak.service.SpeakerService;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SpeakerService speakerService;

    @MessageMapping("/activeSpeaker")
    public void saveActiveSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.ACTIVE));
            this.messagingTemplate.convertAndSend("/active", speakerDto);
        }
    }

    @MessageMapping("/doneSpeaker")
    public void saveDoneSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.DONE));
            this.messagingTemplate.convertAndSend("/done", speakerDto);
        }
    }
    @MessageMapping("/speakingStatus")
    public void saveSpeakingStatusSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.SPEAKING_TIME));
            this.messagingTemplate.convertAndSend("/otherStatus", speakerDto);
        }
    }
    @MessageMapping("/questionStatus")
    public void saveQuestionStatusSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.QUESTION_TIME));
            this.messagingTemplate.convertAndSend("/otherStatus", speakerDto);
        }
    }

    @MessageMapping("/reviewStatus")
    public void saveReviewStatusSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.REWIEW_TIME));
            this.messagingTemplate.convertAndSend("/otherStatus", speakerDto);
        }
    }
    @MessageMapping("/lastWordStatus")
    public void saveLastWordStatusSpeaker(Long speakerId) {
        if (speakerId > 0) {
            SpeakerDto speakerDto = new SpeakerDto(speakerService.updateDiplomStatus(speakerId, Status.LASTWORD_TIME));
            this.messagingTemplate.convertAndSend("/otherStatus", speakerDto);
        }
    }
}
