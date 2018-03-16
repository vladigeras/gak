package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.StudentEntity;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> getAllByGroupAndDeletedTimeIsNull(GroupEntity groupEntity);
}
