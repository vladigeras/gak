package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.CommissionEntity;

@Repository
public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {
}
