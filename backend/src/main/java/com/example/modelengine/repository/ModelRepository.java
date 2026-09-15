package com.example.modelengine.repository;

import com.example.modelengine.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ModelRepository extends JpaRepository<Model, UUID> {}
