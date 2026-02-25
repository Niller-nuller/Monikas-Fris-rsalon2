package org.example.monikasfrisrsalon2.c_model;

import java.util.HashMap;
import java.util.Map;

public class TreatmentRegistry {

    private static final Map<TreatmentType, Treatment> DEFINITIONS = new HashMap<>();

    static {
        DEFINITIONS.put(TreatmentType.Bleaching, new Treatment(3, "Bleaching", 45, 500, TreatmentType.Bleaching));
    }

    public void getDefinition (TreatmentType type) {
        DEFINITIONS.get(type);
    }
}
