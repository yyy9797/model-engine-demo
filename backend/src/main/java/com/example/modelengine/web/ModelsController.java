package com.example.modelengine.web;

import com.example.modelengine.domain.Model;
import com.example.modelengine.domain.ModelVersion;
import com.example.modelengine.domain.Record;
import com.example.modelengine.repository.ModelRepository;
import com.example.modelengine.repository.ModelVersionRepository;
import com.example.modelengine.repository.RecordRepository;
import com.example.modelengine.util.GeoJsonUtils;
import org.locationtech.jts.geom.Geometry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/models")
public class ModelsController {
    private final ModelRepository modelRepo;
    private final ModelVersionRepository versionRepo;
    private final RecordRepository recordRepo;

    public ModelsController(ModelRepository modelRepo, ModelVersionRepository versionRepo, RecordRepository recordRepo) {
        this.modelRepo = modelRepo;
        this.versionRepo = versionRepo;
        this.recordRepo = recordRepo;
    }

    @GetMapping
    public List<Model> list() { return modelRepo.findAll(); }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,Object> payload){
        Model m = new Model();
        m.setCode((String)payload.get("code"));
        m.setName((String)payload.get("name"));
        m.setStatus("draft");
        modelRepo.save(m);
        if(payload.containsKey("schema")){
            ModelVersion v = new ModelVersion();
            v.setModel(m);
            v.setVersionNumber(1);
            v.setSchemaJson(payload.get("schema").toString());
            versionRepo.save(v);
        }
        return ResponseEntity.ok(m);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publish(@PathVariable UUID id) {
        Model m = modelRepo.findById(id).orElseThrow();
        m.setStatus("published");
        modelRepo.save(m);
        return ResponseEntity.ok(Map.of("message","published"));
    }

    @PostMapping("/{id}/records")
    public ResponseEntity<?> createRecord(@PathVariable UUID id, @RequestBody Map<String,Object> payload) throws Exception {
        Model m = modelRepo.findById(id).orElseThrow();
        Record r = new Record();
        r.setModel(m);
        if(payload.containsKey("data")) r.setData(payload.get("data").toString());
        if(payload.containsKey("geom")){
            String geoJson = payload.get("geom").toString();
            Geometry g = GeoJsonUtils.fromGeoJson(geoJson);
            r.setGeom(g);
        }
        recordRepo.save(r);
        return ResponseEntity.ok(r);
    }
}
