package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.StatusEntity;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, String> {
}
