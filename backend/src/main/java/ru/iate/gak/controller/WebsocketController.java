package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.iate.gak.domain.Status;
import ru.iate.gak.dto.SpeakerDto;
import ru.iate.gak.service.SpeakerService;

@Controller
public class WebsocketController {

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
}
