package ru.iate.gak.service;

import ru.iate.gak.dto.CriteriaDtoListWithResult;
import ru.iate.gak.model.CriteriaEntity;
import ru.iate.gak.model.GeneralCriteriaEntity;

import java.util.List;

public interface CriteriaService {
    List<GeneralCriteriaEntity> getDefaultCriteria(Integer listId);

    List<CriteriaEntity> getCriteriaByDiplomId(Long diplomId);



    void saveResultToSpeaker(Integer rating, Long speakerId);

    void saveCriteriaListWithData(Long userId, CriteriaDtoListWithResult criteriaDtoListWithResult);
}
