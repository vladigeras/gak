package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.GroupEntity;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, String> {

    @Query(value = "SELECT g FROM GroupEntity g ORDER BY lower(g.title) ASC")
    List<GroupEntity> getAllOrderByTitleAsc();
}
