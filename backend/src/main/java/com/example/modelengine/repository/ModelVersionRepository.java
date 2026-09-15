package com.example.modelengine.repository;

import com.example.modelengine.domain.ModelVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ModelVersionRepository extends JpaRepository<ModelVersion, UUID> {}
