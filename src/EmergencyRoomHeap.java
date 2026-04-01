import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EmergencyRoomHeap {
    private final ArrayList<Patient> heap;
    private long nextArrivalOrder;

    public EmergencyRoomHeap() {
        this.heap = new ArrayList<>();
        this.nextArrivalOrder = 0L;
    }

    public EmergencyRoomHeap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0");
        }
        this.heap = new ArrayList<>(initialCapacity);
        this.nextArrivalOrder = 0L;
    }

    public Patient insertPatient(String patientId, String name, int severity) {
        Patient patient = new Patient(patientId, name, severity, nextArrivalOrder++);
        heap.add(patient);
        heapifyUp(heap.size() - 1);
        return patient;
    }

    public Patient insertPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("patient must not be null");
        }
        heap.add(patient);
        heapifyUp(heap.size() - 1);

        if (patient.getArrivalOrder() >= nextArrivalOrder) {
            nextArrivalOrder = patient.getArrivalOrder() + 1;
        }
        return patient;
    }

    public Patient peekNextPatient() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("No patients in queue");
        }
        return heap.get(0);
    }

    public Patient treatNextPatient() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("No patients in queue");
        }
        Patient top = heap.get(0);
        int lastIndex = heap.size() - 1;
        swap(0, lastIndex);
        heap.remove(lastIndex);

        if (!heap.isEmpty()) {
            heapifyDown(0);
        }
        return top;
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public long getNextArrivalOrder() {
        return nextArrivalOrder;
    }

    public List<Patient> snapshotInternalArray() {
        return List.copyOf(heap);
    }

    private void heapifyUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (hasHigherPriority(heap.get(current), heap.get(parent))) {
                swap(current, parent);
                current = parent;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int index) {
        int current = index;
        int size = heap.size();
        while (true) {
            int left = current * 2 + 1;
            int right = current * 2 + 2;
            int largest = current;

            if (left < size && hasHigherPriority(heap.get(left), heap.get(largest))) {
                largest = left;
            }
            if (right < size && hasHigherPriority(heap.get(right), heap.get(largest))) {
                largest = right;
            }
            if (largest == current) {
                break;
            }

            swap(current, largest);
            current = largest;
        }
    }

    private boolean hasHigherPriority(Patient a, Patient b) {
        if (a.getSeverity() != b.getSeverity()) {
            return a.getSeverity() > b.getSeverity();
        }
        return a.getArrivalOrder() < b.getArrivalOrder();
    }

    private void swap(int i, int j) {
        Patient temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
