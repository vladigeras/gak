package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.GeneralCriteriaEntity;

@Repository
public interface GeneralCriteriaRepository extends JpaRepository<GeneralCriteriaEntity, Long> {
}
