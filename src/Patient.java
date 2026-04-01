import java.util.Objects;

public class Patient {
    private final String patientId;
    private final String name;
    private final int severity;
    private final long arrivalOrder;

    public Patient(String patientId, String name, int severity, long arrivalOrder) {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("patientId must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (severity < 0) {
            throw new IllegalArgumentException("severity must be >= 0");
        }
        if (arrivalOrder < 0) {
            throw new IllegalArgumentException("arrivalOrder must be >= 0");
        }

        this.patientId = patientId;
        this.name = name;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getSeverity() {
        return severity;
    }

    public long getArrivalOrder() {
        return arrivalOrder;
    }

    @Override
    public String toString() {
        return "Patient{id='" + patientId + "', name='" + name + "', severity=" + severity
                + ", arrivalOrder=" + arrivalOrder + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient patient)) {
            return false;
        }
        return severity == patient.severity
                && arrivalOrder == patient.arrivalOrder
                && patientId.equals(patient.patientId)
                && name.equals(patient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, name, severity, arrivalOrder);
    }
}
