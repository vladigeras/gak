package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.CriteriaEntity;

import java.util.List;

@Repository
public interface CriteriaRepository extends JpaRepository<CriteriaEntity, Long> {

    List<CriteriaEntity> getCriteriaEntitiesByDiplomId(Long diplomId);



    void deleteByCommission(CommissionEntity commissionEntity);
}
