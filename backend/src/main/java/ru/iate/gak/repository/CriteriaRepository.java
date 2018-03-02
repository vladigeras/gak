package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.CriteriaEntity;

@Repository
public interface CriteriaRepository extends JpaRepository<CriteriaEntity, Long> {

    void deleteByCommission(CommissionEntity commissionEntity);
}
