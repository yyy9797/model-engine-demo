package com.example.modelengine.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "dictionaries")
public class Dictionary {
    @Id
    private java.util.UUID id = java.util.UUID.randomUUID();
    @Column(unique = true)
    private String code;
    private String name;
    @Column(columnDefinition = "jsonb")
    private String valuesJson;

    public java.util.UUID getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValuesJson() { return valuesJson; }
    public void setValuesJson(String valuesJson) { this.valuesJson = valuesJson; }
}
