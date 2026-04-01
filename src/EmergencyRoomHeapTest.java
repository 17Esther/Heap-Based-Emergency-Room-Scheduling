import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public final class EmergencyRoomHeapTest {
    private EmergencyRoomHeapTest() {
    }

    public static void runAll() {
        testSeverityPriority();
        testTieBreakByArrivalOrder();
        testPeekAndSize();
        testTreatUntilEmpty();
        testEmptyQueueExceptions();
    }

    private static void testSeverityPriority() {
        EmergencyRoomHeap heap = new EmergencyRoomHeap();
        heap.insertPatient("P1", "Ana", 2);
        heap.insertPatient("P2", "Ben", 5);
        heap.insertPatient("P3", "Cia", 3);

        Patient treated = heap.treatNextPatient();
        assertEquals("P2", treated.getPatientId(), "Highest severity patient should be first");
    }

    private static void testTieBreakByArrivalOrder() {
        EmergencyRoomHeap heap = new EmergencyRoomHeap();
        heap.insertPatient("P1", "Ana", 4);
        heap.insertPatient("P2", "Ben", 4);
        heap.insertPatient("P3", "Cia", 4);

        List<String> order = new ArrayList<>();
        while (!heap.isEmpty()) {
            order.add(heap.treatNextPatient().getPatientId());
        }
        assertEquals(List.of("P1", "P2", "P3"), order, "Tie-break should prioritize earlier arrivals");
    }

    private static void testPeekAndSize() {
        EmergencyRoomHeap heap = new EmergencyRoomHeap();
        heap.insertPatient("P1", "Ana", 1);
        heap.insertPatient("P2", "Ben", 7);
        heap.insertPatient("P3", "Cia", 2);

        assertEquals(3, heap.size(), "Size should track insertions");
        assertEquals("P2", heap.peekNextPatient().getPatientId(), "Peek should return highest priority");
        assertEquals(3, heap.size(), "Peek should not remove the patient");
    }

    private static void testTreatUntilEmpty() {
        EmergencyRoomHeap heap = new EmergencyRoomHeap();
        heap.insertPatient("P1", "Ana", 1);
        heap.insertPatient("P2", "Ben", 2);

        heap.treatNextPatient();
        heap.treatNextPatient();

        assertTrue(heap.isEmpty(), "Heap should be empty after treating all patients");
        assertEquals(0, heap.size(), "Size should be zero");
    }

    private static void testEmptyQueueExceptions() {
        EmergencyRoomHeap heap = new EmergencyRoomHeap();

        assertThrows(NoSuchElementException.class, heap::peekNextPatient, "Peek on empty heap should throw");
        assertThrows(NoSuchElementException.class, heap::treatNextPatient, "Treat on empty heap should throw");
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if ((expected == null && actual != null) || (expected != null && !expected.equals(actual))) {
            throw new AssertionError(message + " | expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertThrows(Class<? extends Throwable> exceptionType, Runnable action, String message) {
        try {
            action.run();
        } catch (Throwable ex) {
            if (exceptionType.isInstance(ex)) {
                return;
            }
            throw new AssertionError(message + " | wrong exception type: " + ex.getClass().getName(), ex);
        }
        throw new AssertionError(message + " | expected exception: " + exceptionType.getName());
    }
}
