package com.example.modelengine.repository;

import com.example.modelengine.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<Record, UUID> {}
