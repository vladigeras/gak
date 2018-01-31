package ru.iate.gak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iate.gak.model.TimestampEntity;

@Repository
public interface TimestampRepository extends JpaRepository<TimestampEntity, Long> {
}
