import java.util.Locale;
import java.util.Random;

public class EmergencyRoomSimulation {
    private static final int[] SIZES = {1000, 5000, 10000, 50000, 100000, 500000};
    private static final int TRIALS = 10;
    private static final long SEED = 47L;

    public static void main(String[] args) {
        System.out.println("=== Emergency Room Heap Assignment Runner ===");
        runCorrectnessTests();
        experimentInsertionPerformance();
        experimentPeekPerformance();
        experimentRemovalPerformance();
        experimentMixedSimulation();
        experimentTieCaseCorrectness();
        System.out.println("=== Completed ===");
    }

    private static void runCorrectnessTests() {
        System.out.println("\n[Correctness] Running heap unit checks...");
        EmergencyRoomHeapTest.runAll();
        System.out.println("[Correctness] All tests passed.");
    }

    private static void experimentInsertionPerformance() {
        System.out.println("\n[Experiment 1] Insertion performance");
        System.out.println("n, avg_total_ms, avg_per_insert_us");

        for (int n : SIZES) {
            long totalNanos = 0L;
            for (int t = 0; t < TRIALS; t++) {
                Random random = new Random(SEED + t);
                EmergencyRoomHeap heap = new EmergencyRoomHeap(n);

                long start = System.nanoTime();
                for (int i = 0; i < n; i++) {
                    int severity = random.nextInt(10) + 1;
                    heap.insertPatient("P" + i, "Patient" + i, severity);
                }
                long end = System.nanoTime();
                totalNanos += (end - start);
            }

            double avgMs = nanosToMillis(totalNanos / (double) TRIALS);
            double avgPerInsertUs = (totalNanos / (double) TRIALS) / n / 1_000.0;
            System.out.println(format("%d, %.3f, %.3f", n, avgMs, avgPerInsertUs));
        }
    }

    private static void experimentPeekPerformance() {
        System.out.println("\n[Experiment 2] Peek performance");
        System.out.println("n, avg_total_ms_for_100k_peeks, avg_per_peek_ns");

        final int peeks = 100_000;
        for (int n : SIZES) {
            long totalNanos = 0L;
            for (int t = 0; t < TRIALS; t++) {
                Random random = new Random(SEED + 100 + t);
                EmergencyRoomHeap heap = new EmergencyRoomHeap(n);
                for (int i = 0; i < n; i++) {
                    int severity = random.nextInt(10) + 1;
                    heap.insertPatient("P" + i, "Patient" + i, severity);
                }

                long start = System.nanoTime();
                for (int i = 0; i < peeks; i++) {
                    heap.peekNextPatient();
                }
                long end = System.nanoTime();
                totalNanos += (end - start);
            }

            double avgMs = nanosToMillis(totalNanos / (double) TRIALS);
            double avgPeekNs = (totalNanos / (double) TRIALS) / peeks;
            System.out.println(format("%d, %.3f, %.3f", n, avgMs, avgPeekNs));
        }
    }

    private static void experimentRemovalPerformance() {
        System.out.println("\n[Experiment 3] Removal performance");
        System.out.println("n, avg_total_ms, avg_per_removal_us");

        for (int n : SIZES) {
            long totalNanos = 0L;
            for (int t = 0; t < TRIALS; t++) {
                Random random = new Random(SEED + 200 + t);
                EmergencyRoomHeap heap = new EmergencyRoomHeap(n);
                for (int i = 0; i < n; i++) {
                    int severity = random.nextInt(10) + 1;
                    heap.insertPatient("P" + i, "Patient" + i, severity);
                }

                long start = System.nanoTime();
                while (!heap.isEmpty()) {
                    heap.treatNextPatient();
                }
                long end = System.nanoTime();
                totalNanos += (end - start);
            }

            double avgMs = nanosToMillis(totalNanos / (double) TRIALS);
            double avgPerRemovalUs = (totalNanos / (double) TRIALS) / n / 1_000.0;
            System.out.println(format("%d, %.3f, %.3f", n, avgMs, avgPerRemovalUs));
        }
    }

    private static void experimentMixedSimulation() {
        System.out.println("\n[Experiment 4] Mixed ER simulation (50% insert / 20% peek / 30% treat)");
        System.out.println("ops, avg_total_ms, final_heap_size");

        final int operations = 200_000;
        long totalNanos = 0L;
        int finalSize = 0;

        for (int t = 0; t < TRIALS; t++) {
            Random random = new Random(SEED + 300 + t);
            EmergencyRoomHeap heap = new EmergencyRoomHeap();

            long start = System.nanoTime();
            for (int i = 0; i < operations; i++) {
                double pick = random.nextDouble();

                if (pick < 0.50) {
                    int severity = random.nextInt(10) + 1;
                    heap.insertPatient("P" + i + "_T" + t, "Patient" + i, severity);
                } else if (pick < 0.70) {
                    if (!heap.isEmpty()) {
                        heap.peekNextPatient();
                    }
                } else {
                    if (!heap.isEmpty()) {
                        heap.treatNextPatient();
                    }
                }
            }
            long end = System.nanoTime();

            totalNanos += (end - start);
            finalSize += heap.size();
        }

        double avgMs = nanosToMillis(totalNanos / (double) TRIALS);
        int avgFinalSize = finalSize / TRIALS;
        System.out.println(format("%d, %.3f, %d", operations, avgMs, avgFinalSize));
    }

    private static void experimentTieCaseCorrectness() {
        System.out.println("\n[Experiment 5] Tie-case correctness");

        EmergencyRoomHeap heap = new EmergencyRoomHeap();
        heap.insertPatient("P100", "Alice", 8);
        heap.insertPatient("P101", "Bob", 8);
        heap.insertPatient("P102", "Carla", 8);
        heap.insertPatient("P103", "Dan", 8);

        StringBuilder order = new StringBuilder();
        while (!heap.isEmpty()) {
            if (order.length() > 0) {
                order.append(" -> ");
            }
            order.append(heap.treatNextPatient().getPatientId());
        }

        System.out.println("Expected treatment order: P100 -> P101 -> P102 -> P103");
        System.out.println("Observed treatment order: " + order);
    }

    private static double nanosToMillis(double nanos) {
        return nanos / 1_000_000.0;
    }

    private static String format(String template, Object... args) {
        return String.format(Locale.US, template, args);
    }
}
