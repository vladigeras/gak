package ru.iate.gak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iate.gak.dto.QuestionDto;
import ru.iate.gak.security.GakSecured;
import ru.iate.gak.security.Roles;
import ru.iate.gak.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/save", consumes = "application/json")
    @GakSecured(roles = {Roles.SECRETARY})
    public void saveQuestionsList(@RequestParam(name = "speakerId", required = true) Long id,
                                  @RequestBody List<QuestionDto> questions) {
        if (id <= 0) throw new RuntimeException("Неверное значение для спикера");
        questionService.saveQuestions(id, questions.stream().map(QuestionDto::toQuestion).collect(Collectors.toList()));
    }

    @GetMapping(value = "/ofSpeaker")
    @GakSecured(roles = {Roles.SECRETARY})
    public List<QuestionDto> getQuestionsOfSpeaker(@RequestParam(name = "speakerId", required = true) Long id) {
        if (id <= 0) throw new RuntimeException("Неверное значение для спикера");
        return questionService.getQuestionsOfSpeaker(id).stream().map(QuestionDto::new).collect(Collectors.toList());
    }
}
