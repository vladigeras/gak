package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.UserEntity;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {

    List<CommissionEntity> getCommissionEntitiesByListId(Integer listId);

    CommissionEntity getByUser(UserEntity userEntity);
}
