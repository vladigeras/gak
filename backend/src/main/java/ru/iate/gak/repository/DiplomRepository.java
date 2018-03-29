package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.StudentEntity;

@Repository
public interface DiplomRepository extends JpaRepository<DiplomEntity, Long> {
    DiplomEntity getByStudent(StudentEntity studentEntity);
}
