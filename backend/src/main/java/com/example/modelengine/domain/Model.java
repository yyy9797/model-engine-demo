package com.example.modelengine.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "models")
public class Model {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private String description;
    @Column(nullable = false)
    private String status; // draft/published/archived

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
