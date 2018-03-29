package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Diplom;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.repository.DiplomRepository;
import ru.iate.gak.repository.SpeakerRepository;
import ru.iate.gak.service.DiplomService;

import javax.transaction.Transactional;

@Service
public class DiplomServiceImpl implements DiplomService{

    @Autowired
    private DiplomRepository diplomRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Override
    @Transactional
    public Diplom getDiplomBySpeakerId(Long id) {
        SpeakerEntity speakerEntity = speakerRepository.getOne(id);
        if (speakerEntity == null) throw new RuntimeException("Произошла ошибка");

        if (speakerEntity.getStudent() != null) {
            DiplomEntity diplomEntity = diplomRepository.getByStudent(speakerEntity.getStudent());
            if (diplomEntity == null) throw new RuntimeException("Диплом не найден");

            return new Diplom(diplomEntity);
        } else throw new RuntimeException("Произошла ошибка");
    }
}
