package mx.com.ferbo.util;

public enum PeriodoISR {
	DI("Diario"),
    S("Semanal"),
    DE("Decenal"),
    Q("Quincenal"),
    M("Mensual");

    private final String description;

    PeriodoISR(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public static PeriodoISR fromAbbreviation(String abbreviation) {
        for (PeriodoISR status : PeriodoISR.values()) {
            if (status.name().equalsIgnoreCase(abbreviation)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No se encontró un Status con la abreviación: " + abbreviation);
    }
}
