package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iate.gak.domain.GeneralCriteria;
import ru.iate.gak.repository.GeneralCriteriaRepository;
import ru.iate.gak.service.CriteriaService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private GeneralCriteriaRepository generalCriteriaRepository;

    @Override
    @Transactional
    public List<GeneralCriteria> getDefaultCriteria(Integer listId) {
        return generalCriteriaRepository.getAllByListId(listId).stream().map(GeneralCriteria::new).collect(Collectors.toList());
    }
}
