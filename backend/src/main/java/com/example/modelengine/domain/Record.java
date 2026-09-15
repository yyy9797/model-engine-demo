package com.example.modelengine.domain;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "records")
public class Record {
    @Id
    private java.util.UUID id = java.util.UUID.randomUUID();
    @ManyToOne
    private Model model;
    @ManyToOne
    private ModelVersion modelVersion;
    @Column(columnDefinition = "jsonb")
    private String data;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Geometry geom;

    public java.util.UUID getId() { return id; }
    public Model getModel() { return model; }
    public void setModel(Model model) { this.model = model; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public Geometry getGeom() { return geom; }
    public void setGeom(Geometry geom) { this.geom = geom; }
}
