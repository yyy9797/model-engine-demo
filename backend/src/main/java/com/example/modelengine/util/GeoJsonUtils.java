package com.example.modelengine.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

public class GeoJsonUtils {
    public static Geometry fromGeoJson(String geoJson) throws Exception {
        GeoJsonReader reader = new GeoJsonReader();
        return reader.read(geoJson);
    }
    public static String toGeoJson(Geometry geometry) {
        GeoJsonWriter writer = new GeoJsonWriter();
        return writer.write(geometry);
    }
}
