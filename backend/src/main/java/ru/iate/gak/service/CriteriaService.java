package ru.iate.gak.service;

import ru.iate.gak.domain.Criteria;
import ru.iate.gak.domain.GeneralCriteria;
import ru.iate.gak.dto.CriteriaDtoListWithResult;

import java.util.List;

public interface CriteriaService {
    List<GeneralCriteria> getDefaultCriteria(Integer listId);

    List<Criteria> getCriteriaByDiplomId(Long diplomId);



    void saveResultToSpeaker(Integer rating, Long speakerId);

    void saveCriteriaListWithData(Long userId, CriteriaDtoListWithResult criteriaDtoListWithResult);
}
