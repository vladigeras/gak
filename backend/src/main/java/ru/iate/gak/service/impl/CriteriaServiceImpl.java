package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.Criteria;
import ru.iate.gak.domain.GeneralCriteria;
import ru.iate.gak.dto.CriteriaDto;
import ru.iate.gak.dto.CriteriaDtoListWithResult;
import ru.iate.gak.model.*;
import ru.iate.gak.repository.*;
import ru.iate.gak.service.CriteriaService;
import ru.iate.gak.util.StringUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private GeneralCriteriaRepository generalCriteriaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    @Transactional
    public List<GeneralCriteria> getDefaultCriteria(Integer listId) {
        return generalCriteriaRepository.getAllByListId(listId).stream().map(GeneralCriteria::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveResultToSpeaker(Integer rating, Long speakerId) {
        if (rating > 0 && speakerId > 0) {
            SpeakerEntity speaker = speakerRepository.getOne(speakerId);
            if (speaker == null) throw new RuntimeException("Произошла ошибка");
            if (speaker.getStudent() != null) {
                if (speaker.getStudent().getDiplom() != null) {
                    speaker.getStudent().getDiplom().setResultMark(rating);
                    speakerRepository.save(speaker);
                }
            }
        }
    }

    /**
     * Save criteria to speaker-diplom as result of speaking by any criteria chosen by commission.
     * Delete old criteria by commission and save new with new data.
     * @param userId - id of user, which will be a commission man
     * @param criteriaDtoListWithResult - data, which includes criteria and speaker's id
     */
    @Override
    @Transactional
    public void saveCriteriaListWithData(Long userId, CriteriaDtoListWithResult criteriaDtoListWithResult) {
        if (criteriaDtoListWithResult != null) {
            List<Criteria> criteriaList = criteriaDtoListWithResult.criteriaDtoList.stream().map(CriteriaDto::toCriteria).collect(Collectors.toList());

            if (criteriaDtoListWithResult.speakerId > 0 && userId > 0) {
                UserEntity userEntity = userRepository.findOne(userId);
                if (userEntity == null) throw new RuntimeException("Произошла ошибка");

                SpeakerEntity speakerEntity = speakerRepository.getOne(criteriaDtoListWithResult.speakerId);
                if (speakerEntity != null) {
                    if (speakerEntity.getStudent() != null) {
                        DiplomEntity diplomEntity = speakerEntity.getStudent().getDiplom();

                        if (diplomEntity != null) {
                            CommissionEntity commissionEntity = commissionRepository.getByUser(userEntity);
                            if (commissionEntity!= null) {

                                ////delete old criteria of these commission and save new with new data
                                criteriaRepository.deleteByCommission(commissionEntity);

                                criteriaList.forEach(c -> {
                                    if (!StringUtil.isStringNullOrEmptyTrim(c.getTitle()) && c.getRating() != null) {
                                        CriteriaEntity criteriaEntity = new CriteriaEntity();
                                        criteriaEntity.setComment(c.getComment());
                                        criteriaEntity.setCommission(commissionEntity);
                                        criteriaEntity.setDiplom(diplomEntity);
                                        criteriaEntity.setRating(c.getRating());
                                        criteriaEntity.setTitle(c.getTitle());
                                        criteriaRepository.save(criteriaEntity);
                                    }
                                });
                            } else throw new RuntimeException("Пользователь не найден в списке коммисии");
                        }
                    }
                }
            } else throw new RuntimeException("Произошла ошибка");
        }
    }
}
