package ru.iate.gak.service;

import ru.iate.gak.domain.GeneralCriteria;

import java.util.List;

public interface CriteriaService {
    List<GeneralCriteria> getDefaultCriteria(Integer listId);
}
