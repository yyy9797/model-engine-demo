package com.example.modelengine.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "model_versions")
public class ModelVersion {
    @Id
    private UUID id = UUID.randomUUID();
    @ManyToOne
    private Model model;
    private Integer versionNumber;
    @Column(columnDefinition = "jsonb")
    private String schemaJson;
    private String changeDescription;

    public UUID getId() { return id; }
    public Model getModel() { return model; }
    public void setModel(Model model) { this.model = model; }
    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    public String getSchemaJson() { return schemaJson; }
    public void setSchemaJson(String schemaJson) { this.schemaJson = schemaJson; }
}
