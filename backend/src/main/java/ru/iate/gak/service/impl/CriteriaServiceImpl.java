package ru.iate.gak.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.dto.CriteriaDto;
import ru.iate.gak.dto.CriteriaDtoListWithResult;
import ru.iate.gak.model.*;
import ru.iate.gak.repository.*;
import ru.iate.gak.service.CriteriaService;
import ru.iate.gak.util.StringUtil;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CriteriaServiceImpl implements CriteriaService {

    private static final Logger logger = LoggerFactory.getLogger(CriteriaServiceImpl.class);

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
    public List<GeneralCriteriaEntity> getDefaultCriteria(Integer listId) {
        return generalCriteriaRepository.getAllByListId(listId);
    }

    @Override
    public List<CriteriaEntity> getCriteriaByDiplomId(Long diplomId) {
        return criteriaRepository.getCriteriaEntitiesByDiplomId(diplomId);
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

                    logger.info("Диплому " + speaker.getStudent().getDiplom().getId() + " выставлена оценка " + rating);
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
            List<CriteriaDto> criteriaList = criteriaDtoListWithResult.criteriaDtoList;

            if (criteriaDtoListWithResult.speakerId > 0 && userId > 0) {
				UserEntity userEntity = userRepository.findById(userId)
						.orElseThrow(() -> new RuntimeException("Пользователь с  id = " + userId + " не найден"));

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
                                    if (!StringUtil.isStringNullOrEmptyTrim(c.title) && c.rating != null) {
                                        CriteriaEntity criteriaEntity = new CriteriaEntity();
                                        criteriaEntity.setComment(c.comment);
                                        criteriaEntity.setCommission(commissionEntity);
                                        criteriaEntity.setDiplom(diplomEntity);
                                        criteriaEntity.setRating(c.rating);
                                        criteriaEntity.setTitle(c.title);
                                        criteriaRepository.save(criteriaEntity);
                                    }
                                });

                                logger.info("Член комиссии " + commissionEntity.getUser().getLogin() + " сохранил оценки по критериям " +
                                        "для диплома " + diplomEntity.getId());

                            } else throw new RuntimeException("Пользователь не найден в списке коммисии");
                        }
                    }
                }
            } else throw new RuntimeException("Произошла ошибка");
        }
    }
}
